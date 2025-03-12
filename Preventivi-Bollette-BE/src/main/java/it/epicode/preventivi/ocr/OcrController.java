package it.epicode.preventivi.ocr;

import it.epicode.preventivi.spread.SpreadEnergia;
import it.epicode.preventivi.spread.SpreadEnergiaRepository;
import it.epicode.preventivi.spread.SpreadGas;
import it.epicode.preventivi.spread.SpreadGasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/ocr")
@RequiredArgsConstructor
public class OcrController {

	private final OcrService ocrService;
	private final SpreadEnergiaRepository spreadEnergiaRepository;
	private final SpreadGasRepository spreadGasRepository;

	@PostMapping(value = "/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> extractBillData(@RequestParam("file") MultipartFile file) {
		System.out.println("File ricevuto: " + file.getOriginalFilename());
		System.out.println("Tipo MIME: " + file.getContentType());

		try {
			byte[] fileData = file.getBytes();
			String fileName = file.getOriginalFilename();
			String fileType = file.getContentType();

			// Estrai i dati dalla bolletta
			Map<String, String> extractedData = ocrService.extractBillDetails(fileData, fileName, fileType);

			// Estrai il periodo di fatturazione
			String periodoFatturazione = extractedData.getOrDefault("Periodo fatturazione", "N/A");

			// Determina il tipo di bolletta (energia o gas)
			String tipoBolletta = determineBillType(extractedData);
			System.out.println("Tipo bolletta determinato: " + tipoBolletta);

			// Recupera il prezzo di mercato dal database
			double prezzoMercato = findMarketPrice(periodoFatturazione, tipoBolletta);
			System.out.println("Prezzo mercato trovato: " + prezzoMercato);

			// Crea la response
			Map<String, Object> response = new HashMap<>(extractedData);
			response.put("Tipo Bolletta", tipoBolletta);
			response.put("Prezzo Mercato", prezzoMercato);

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Errore nella lettura del file"));
		}
	}


	private String determineBillType(Map<String, String> extractedData) {
		String spesa = extractedData.get("Spesa Materia Energia");
		String consumo = extractedData.get("Consumo fatturato");

		if (spesa != null && !spesa.equals("N/A")) {
			return "energia";
		} else if (consumo != null && !consumo.equals("N/A")) {
			return "gas";
		}
		return "N/A";
	}


	private double findMarketPrice(String periodo, String tipoBolletta) {
		if (periodo.equals("N/A") || tipoBolletta.equals("N/A")) {
			return 0.0;
		}

		int mese = convertMonthToNumber(periodo.split(" ")[0]);
		int anno = Integer.parseInt(periodo.split(" ")[1]);

		System.out.println("Cercando prezzo di mercato per " + tipoBolletta + " - Mese: " + mese + ", Anno: " + anno);

		if (tipoBolletta.equals("energia")) {
			return spreadEnergiaRepository.findByMonthAndYear(mese, anno)
				.map(SpreadEnergia::getPrezzo)
				.orElse(0.0);
		} else {
			return spreadGasRepository.findByMonthAndYear(mese, anno)
				.map(SpreadGas::getPrezzo)
				.orElse(0.0);
		}
	}



	private int convertMonthToNumber(String monthName) {
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
