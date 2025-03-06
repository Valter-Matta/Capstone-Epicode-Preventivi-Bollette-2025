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


	@PostMapping
	public ResponseEntity<Double> calcolaPreventivo (@RequestBody Map<String, String> params) {
		String tipo = params.get("tipo");
		double spesaMateriaCliente = Double.parseDouble(params.get("spesaMateriaCliente"));
		int consumo = Integer.parseInt(params.get("consumo"));
		int mese = Integer.parseInt(params.get("mese"));
		int anno = Integer.parseInt(params.get("anno"));

		double spread = preventivoService.calcolaSpread(tipo, spesaMateriaCliente, consumo, mese, anno);
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