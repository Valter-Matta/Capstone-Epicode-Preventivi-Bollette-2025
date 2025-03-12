package it.epicode.preventivi.ocr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.preventivi.spread.SpreadEnergia;
import it.epicode.preventivi.spread.SpreadEnergiaRepository;
import it.epicode.preventivi.spread.SpreadGas;
import it.epicode.preventivi.spread.SpreadGasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OcrService {
	private final RestTemplate restTemplate;
	private final SpreadGasRepository spreadGasRepository;
	private final SpreadEnergiaRepository spreadEnergiaRepository;

	public Map<String, String> extractBillDetails(byte[] fileData, String fileName, String fileType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("apikey", "K85937216788957");

		// Determiniamo se è un PDF o un'immagine
		boolean isPdf = fileType.equalsIgnoreCase("application/pdf");

		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("file", new ByteArrayResource(fileData) {
			@Override
			public String getFilename() {
				return fileName; // Mantiene il nome originale
			}
		});

		requestBody.add("language", "ita");
		requestBody.add("isOverlayRequired", "true");

		// Se è un PDF, specifico il filetype manualmente
		if (isPdf) {
			requestBody.add("filetype", "PDF");
		}

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			"https://api.ocr.space/parse/image",
			HttpMethod.POST,
			requestEntity,
			String.class
		);

		System.out.println("OCR Response: " + response.getBody());
		return processBillData(response.getBody());
	}

	private Map<String, String> processBillData(String ocrResponse) {
		Map<String, String> extractedData = new HashMap<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(ocrResponse);

			if (rootNode.has("ParsedResults")) {
				JsonNode parsedResults = rootNode.get("ParsedResults").get(0);
				if (parsedResults.has("TextOverlay")) {
					JsonNode textOverlay = parsedResults.get("TextOverlay");
					StringBuilder fullText = new StringBuilder();

					for (JsonNode line : textOverlay.get("Lines")) {
						fullText.append(line.get("LineText").asText()).append(" ");
					}

					String normalizedText = fullText.toString().toLowerCase();
					extractedData = extractValues(normalizedText);
				}
			}
		} catch (Exception e) {
			extractedData.put("Errore", "Errore nell'elaborazione OCR");
		}

		return extractedData;
	}

	private double getMarketPrice(int month, int year, boolean isGas) {
		if (isGas) {
			return spreadGasRepository.findByMonthAndYear(month, year)
				.map(SpreadGas::getPrezzo)
				.orElse(0.0);
		} else {
			return spreadEnergiaRepository.findByMonthAndYear(month, year)
				.map(SpreadEnergia::getPrezzo)
				.orElse(0.0);
		}
	}

	private boolean isGasBill(String text) {
		return text.toLowerCase().matches(".*\\b(gas|smc|fornitura di gas naturale|metano)\\b.*");
	}

	private Map<String, String> extractValues(String text) {
		Map<String, String> extractedData = new HashMap<>();

		extractedData.put("Totale fattura", findNumericValue(text, new String[]{
			"totale fattura", "importo totale", "importo da pagare", "importo complessivo", "totale bolletta"
		}));

		extractedData.put("Spesa Materia Energia", findSpesaMateriaGas(text, new String[]{
			"spesa materia gas", "spesa materia energia", "costo fornitura", "energia elettrica", "spesa energia"
		}));

		extractedData.put("Consumo fatturato", findNumericValue(text, new String[]{
			"consumo fatturato", "consumo totale", "consumo bolletta", "progressivo annuo", "smc", "kwh", "quantità consumo"
		}));

		extractedData.put("Periodo fatturazione", findValue(text, new String[]{
			"periodo di fatturazione", "fatturazione periodo", "mese di riferimento", "mese fattura"
		}));

		System.out.println("Dati estratti: " + extractedData);

		return extractedData;
	}



	private String findValue(String text, String[] possibleLabels) {
		for (String label : possibleLabels) {
			Pattern pattern = Pattern.compile(label + ".*?([A-Za-zàèéìòù]+\\s*\\d{4})", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		}
		return "N/A";
	}

	private String findNumericValue(String text, String[] possibleLabels) {
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
	private String findSpesaMateriaGas(String text, String[] possibleLabels) {
		String labelPattern = String.join("|", possibleLabels);

		// Nuova regex che cattura SOLO il valore giusto (evita valori tra parentesi e IVA)
		Pattern pattern = Pattern.compile(
			"(?i)(" + labelPattern + ")\\s*[:\\-]?\\s*(?!\\(su €|IVA)[^\\d€]*€?\\s*(\\d{1,3}(?:[.,]\\d{2})?)"
		);

		System.out.println("TESTO OCR COMPLETO:\n" + text);
		System.out.println("Label usati per il match: " + labelPattern);

		Matcher matcher = pattern.matcher(text);
		String lastMatch = "N/A";

		while (matcher.find()) {
			System.out.println("✅ Match trovato: " + matcher.group(2) + " (Label: " + matcher.group(1) + ")");
			lastMatch = matcher.group(2).replace(",", ".").trim();

			// Se il valore è quello giusto, fermati subito
			if (Double.parseDouble(lastMatch) < 200) { // Escludiamo valori come 282,35
				return lastMatch;
			}
		}

		if (!lastMatch.equals("N/A")) {
			return lastMatch;
		}

		// Se trova l'etichetta ma non un numero valido, segnala l'errore
		for (String label : possibleLabels) {
			if (text.toLowerCase().contains(label.toLowerCase())) {
				System.out.println("⚠️ Etichetta trovata nel testo ma senza valore numerico: " + label);
				return "Errore: etichetta trovata ma senza importo.";
			}
		}

		System.out.println("❌ Nessun valore trovato per Spesa Materia Energia!");
		return "N/A";
	}



	private String findEnergyCost(String text, String label) {
		Pattern pattern = Pattern.compile(label + ".*?€?\\s*([0-9]+(?:[.,][0-9]+)?)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).replace(",", ".").trim();
		}
		return "N/A";
	}


	private int extractMonth(String periodo) {
		String[] months = {"gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno",
			"luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre"};

		for (int i = 0; i < months.length; i++) {
			if (periodo.toLowerCase().contains(months[i])) {
				return i + 1;
			}
		}

		Pattern pattern = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
		Matcher matcher = pattern.matcher(periodo);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(2)); // Ritorna il mese (MM)
		}

		return 0;
	}

	private int extractYear(String periodo) {
		Pattern pattern = Pattern.compile("(\\d{4})");
		Matcher matcher = pattern.matcher(periodo);
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
	}

	private double parseDouble(String value) {
		if (value == null || value.equals("N/A")) return 0.0;
		return Double.parseDouble(value.replace(",", "."));
	}
}
