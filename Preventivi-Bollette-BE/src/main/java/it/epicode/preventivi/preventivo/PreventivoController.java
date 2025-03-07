package it.epicode.preventivi.preventivo;

import it.epicode.preventivi.confrontoPrezzi.ConfrontoPrezziResponse;
import it.epicode.preventivi.confrontoPrezzi.PropostaRisparmioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping ("/api/preventivi")
@RequiredArgsConstructor
public class PreventivoController {
	private final PreventivoService preventivoService;


	@PostMapping("/calcola")
	public ResponseEntity<Double> calcolaSpread(@RequestBody PreventivoRequest request) {
		// Estrarre mese e anno dal formato "YYYY-MM"
		String[] dateParts = request.getMeseAnno().split("-");
		int anno = Integer.parseInt(dateParts[0]);
		int mese = Integer.parseInt(dateParts[1]);

		double spread = preventivoService.calcolaSpread(
			request.getTipo(),
			request.getSpesaMateriaCliente(),
			request.getConsumo(),
			mese,
			anno
		);

		return ResponseEntity.ok(spread);
	}

	@GetMapping ("/confronta/{bollettaId}")
	public ResponseEntity<ConfrontoPrezziResponse> confrontaPrezzi (@PathVariable Long bollettaId) {
		return ResponseEntity.ok(preventivoService.confrontaPrezzi(bollettaId));
	}

	@GetMapping ("/proposta-risparmio/{bollettaId}")
	public ResponseEntity<PropostaRisparmioResponse> generaPropostaRisparmio (@PathVariable Long bollettaId) {
		return ResponseEntity.ok(preventivoService.generaPropostaRisparmio(bollettaId));
	}
}