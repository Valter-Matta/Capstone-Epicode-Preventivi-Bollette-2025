package it.epicode.preventivi.ocr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OcrService {

	private final RestTemplate restTemplate;

	public Map<String, String> extractBillDetails (byte[] fileData, String fileName, String fileType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("apikey", "K85937216788957");

		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("file", new ByteArrayResource(fileData) {
			@Override
			public String getFilename () {
				return fileName;
			}
		});

		requestBody.add("language", "ita");
		requestBody.add("isOverlayRequired", "true");

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange(
			"https://api.ocr.space/parse/image",
			HttpMethod.POST,
			requestEntity,
			String.class
		);

		return processBillData(response.getBody());
	}

	private Map<String, String> extractValues (String text) {
		Map<String, String> extractedData = new HashMap<>();

		extractedData.put("Tipo Bolletta", determineBillType(text));
		if (extractedData.get("Tipo Bolletta").equals("gas")) {
			extractedData.put("Totale fattura", findNumericValue(text, new String[]{
				"totale fattura", "importo totale", "importo da pagare", "importo complessivo", "totale bolletta"
			}));

		} else {
			extractedData.put("Totale fattura", findTotaleFattura(text));
		}

		if (extractedData.get("Tipo Bolletta").equals("gas")) {
			extractedData.put("Spesa Materia Energia", findSpesaMateria(text));

		} else {
			extractedData.put("Spesa Materia Energia", findNumericValue(text, new String[]{
				"totale fattura", "importo totale", "importo da pagare", "importo complessivo", "totale bolletta"
			}));
		}


		extractedData.put("Consumo fatturato", findNumericValue(text, new String[]{
			"totale consumi fatturati", "consumo fatturato", "consumo totale", "consumo bolletta", "totale consumi fatturati", "smc", "kwh", "quantità consumo"
		}));

		extractedData.put("Periodo fatturazione", findValue(text, new String[]{
			"periodo di fatturazione", "fatturazione periodo", "mese di riferimento", "mese fattura"
		}));

		System.out.println("Dati estratti: " + extractedData);

		return extractedData;
	}

	private String findTotaleFattura (String text) {
		Pattern pattern = Pattern.compile("(?i)(?:totale fattura|importo da pagare)[:\\s€]*?(\\d{1,3}[,.]\\d{2})");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			return matcher.group(1).replace(",", ".").trim();
		}

		return "0.00";
	}


	private String findValue (String text, String[] possibleLabels) {
		for (String label : possibleLabels) {
			Pattern pattern = Pattern.compile(label + ".*?([A-Za-zàèéìòù]+\\s*\\d{4})", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		}
		return "N/A";
	}

	private Map<String, String> processBillData (String ocrResponse) {
		Map<String, String> extractedData = new HashMap<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(ocrResponse);

			if (rootNode.has("ParsedResults")) {
				StringBuilder fullText = new StringBuilder();
				int pageCount = 0;

				for (JsonNode parsedResults : rootNode.get("ParsedResults")) {
					if (pageCount >= 2) break; // ✅ Interrompe dopo la seconda pagina
					if (parsedResults.has("TextOverlay")) {
						JsonNode textOverlay = parsedResults.get("TextOverlay");
						for (JsonNode line : textOverlay.get("Lines")) {
							fullText.append(line.get("LineText").asText()).append(" ");
						}
					}
					pageCount++; // Incrementa il conteggio delle pagine elaborate
				}

				String normalizedText = fullText.toString().toLowerCase();
				extractedData = extractValues(normalizedText);
			}
		} catch (Exception e) {
			extractedData.put("Errore", "Errore nell'elaborazione OCR");
		}

		return extractedData;
	}

	private String determineBillType (String text) {
		System.out.println("🔍 Analisi testo per tipo bolletta:\n" + text);

		text = text.toLowerCase();
		if (text.contains("fornitura dl gas naturale")) {
			return "gas";
		} else if (text.contains("fornitura dl energia elettrica")) {
			return "energia";
		}

		return "N/A";
	}

	private String findSpesaMateria (String text) {
		Pattern pattern = Pattern.compile("(?i)spesa materia.*?€?\\s*(\\d{1,3}[,.]\\d{2})");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			return matcher.group(1).replace(",", ".").trim();
		}

		return "0.00";
	}

	private String findNumericValue (String text, String[] possibleLabels) {
		for (String label : possibleLabels) {
			// Aggiunto (?i) per case-insensitivity
			Pattern pattern = Pattern.compile("(?i)" + label + ".*?€?\\s*([0-9]+(?:[.,][0-9]+)?)");
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				return matcher.group(1).replace(",", ".").trim();
			}
		}
		return "N/A";
	}

	public int convertMonthToNumber (String monthName) {
		Map<String, Integer> months = new HashMap<>();
		months.put("gennaio", 1);
		months.put("febbraio", 2);
		months.put("marzo", 3);
		months.put("aprile", 4);
		months.put("maggio", 5);
		months.put("giugno", 6);
		months.put("luglio", 7);
		months.put("agosto", 8);
		months.put("settembre", 9);
		months.put("ottobre", 10);
		months.put("novembre", 11);
		months.put("dicembre", 12);

		return months.getOrDefault(monthName.toLowerCase(), 0);
	}
}
