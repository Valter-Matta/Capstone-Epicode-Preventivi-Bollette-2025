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

@RestController
@RequestMapping ("/ocr")
@RequiredArgsConstructor
public class OcrController {

	private final OcrService ocrService;
	private final SpreadEnergiaRepository spreadEnergiaRepository;
	private final SpreadGasRepository spreadGasRepository;

	@PostMapping (value = "/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> extractBillData (@RequestParam ("file") MultipartFile file) {
		System.out.println("📂 File ricevuto: " + file.getOriginalFilename());
		System.out.println("📄 Tipo MIME: " + file.getContentType());

		try {
			byte[] fileData = file.getBytes();
			String fileName = file.getOriginalFilename();
			String fileType = file.getContentType();

			// Estrai i dati dalla bolletta
			Map<String, String> extractedData = ocrService.extractBillDetails(fileData, fileName, fileType);

			// Controllo se l'OCR ha estratto qualcosa
			if (extractedData.isEmpty() || extractedData.containsKey("Errore")) {
				System.out.println("❌ Errore nell'estrazione OCR: " + extractedData.get("Errore"));
				return ResponseEntity.badRequest().body(Map.of("error", "Errore nell'estrazione OCR"));
			}


			// estrai il tipo di bolletta
			String tipoBolletta = extractedData.getOrDefault("Tipo Bolletta", "N/A");


			// Estrai il periodo di fatturazione
			String periodoFatturazione = extractedData.getOrDefault("Periodo fatturazione", "N/A");

			// Recupera il prezzo di mercato dal database
			double prezzoMercato = findMarketPrice(periodoFatturazione, tipoBolletta);
			System.out.println("💰 Prezzo mercato trovato: " + prezzoMercato);

			// Crea la response
			Map<String, Object> response = new HashMap<>(extractedData);
			response.put("Prezzo Mercato", prezzoMercato);

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			System.out.println("❌ Errore nella lettura del file: " + e.getMessage());
			return ResponseEntity.badRequest().body(Map.of("error", "Errore nella lettura del file"));
		}
	}

	private double findMarketPrice (String periodo, String tipoBolletta) {
		if (periodo.equals("N/A") || tipoBolletta.equals("N/A")) {
			return 0.0;
		}

		String[] parts = periodo.split(" ");
		if (parts.length < 2) {
			return 0.0;
		}

		int mese = ocrService.convertMonthToNumber(parts[0]);
		int anno = Integer.parseInt(parts[1]);

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

}
