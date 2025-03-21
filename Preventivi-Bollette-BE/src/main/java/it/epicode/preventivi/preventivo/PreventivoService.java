package it.epicode.preventivi.preventivo;

import it.epicode.preventivi.bolletta.Bolletta;
import it.epicode.preventivi.bolletta.BollettaRepository;
import it.epicode.preventivi.confrontoPrezzi.ConfrontoPrezziResponse;
import it.epicode.preventivi.confrontoPrezzi.PropostaRisparmioResponse;
import it.epicode.preventivi.spread.SpreadEnergia;
import it.epicode.preventivi.spread.SpreadEnergiaRepository;
import it.epicode.preventivi.spread.SpreadGas;
import it.epicode.preventivi.spread.SpreadGasRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class PreventivoService {
	private final BollettaRepository bollettaRepository;
	private final SpreadGasRepository spreadGasRepository;
	private final SpreadEnergiaRepository spreadEnergiaRepository;

	public PreventivoResponse calcolaSpread (String tipo, double spesaMateriaCliente, int consumo, int mese, int anno) {
		double prezzoMercato = 0.0;
		System.out.println("Calcolo spread per tipo: " + tipo);
		System.out.println("Spesa Materia Cliente: " + spesaMateriaCliente);
		System.out.println("Consumo: " + consumo);
		System.out.println("Mese: " + mese + " Anno: " + anno);

		if ("gas".equalsIgnoreCase(tipo)) {
			prezzoMercato = spreadGasRepository.findByMonthAndYear(mese, anno)
				.map(spread -> spread.getPrezzo())
				.orElseThrow(() -> new RuntimeException("Prezzo gas non trovato per il periodo selezionato"));
		} else if ("energia".equalsIgnoreCase(tipo)) {
			prezzoMercato = spreadEnergiaRepository.findByMonthAndYear(mese, anno)
				.map(spread -> spread.getPrezzo())
				.orElseThrow(() -> new RuntimeException("Prezzo energia non trovato per il periodo selezionato"));
		} else {
			throw new IllegalArgumentException("Tipo non valido. Usa 'gas' o 'energia'.");
		}

		double prezzoUnitarioCliente = spesaMateriaCliente / consumo;
		PreventivoResponse response = new PreventivoResponse();
		response.setSpread(prezzoUnitarioCliente);
		response.setPrezzoMercato(prezzoMercato);


		return response;
	}

	public ConfrontoPrezziResponse confrontaPrezzi (Long bollettaId) {
		Bolletta bolletta = bollettaRepository.findById(bollettaId)
			.orElseThrow(() -> new IllegalArgumentException("Bolletta non trovata"));

		LocalDate periodo = bolletta.getDataFineFatturazione();
		Optional<SpreadGas> spreadGasOpt = spreadGasRepository.findByData(periodo);
		Optional<SpreadEnergia> spreadEnergiaOpt = spreadEnergiaRepository.findByData(periodo);

		double prezzoMercato = 0;
		if (bolletta.getTipo().equals("GAS") && spreadGasOpt.isPresent()) {
			prezzoMercato = spreadGasOpt.get().getPrezzo();
		} else if (bolletta.getTipo().equals("ENERGIA") && spreadEnergiaOpt.isPresent()) {
			prezzoMercato = spreadEnergiaOpt.get().getPrezzo();
		} else {
			throw new IllegalArgumentException("Prezzo di mercato non disponibile per il periodo indicato");
		}

		double spreadPagato = bolletta.getSpesaMateria() / bolletta.getConsumo();
		double risparmioPotenziale = (spreadPagato - prezzoMercato) * bolletta.getConsumo();

		return new ConfrontoPrezziResponse(
			bolletta.getTipo(),
			spreadPagato,
			prezzoMercato,
			risparmioPotenziale
		);
	}

	public PropostaRisparmioResponse generaPropostaRisparmio (Long bollettaId) {
		ConfrontoPrezziResponse confronto = confrontaPrezzi(bollettaId);

		double nuovoPrezzo = confronto.getPrezzoMercato() * 0.98; // Sconto ipotetico del 2%
		double risparmioStimato = (confronto.getSpreadPagato() - nuovoPrezzo) * confronto.getRisparmioPotenziale();

		String suggerimento = "Passa a un fornitore con tariffa di mercato aggiornata per risparmiare fino a "
			+ risparmioStimato + "€ all'anno.";

		return new PropostaRisparmioResponse(
			confronto.getTipo(),
			nuovoPrezzo,
			risparmioStimato,
			suggerimento
		);
	}
}
