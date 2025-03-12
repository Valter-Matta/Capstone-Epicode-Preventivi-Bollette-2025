package it.epicode.preventivi.offerte;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OffertaRunner implements ApplicationRunner {

	private final OffertaEnergiaRepository offertaEnergiaRepository;
	private final OffertaGasRepository offertaGasRepository;

	@Override
	public void run(org.springframework.boot.ApplicationArguments args) {
		if (offertaEnergiaRepository.count() == 0) {
			List<OffertaEnergia> offerteEnergia = List.of(
				new OffertaEnergia(null, "Energia Verde", "Energia 100% rinnovabile", 0.15, "Enel", "https://example.com/enel-logo.png"),
				new OffertaEnergia(null, "Energia Bloccata", "Prezzo bloccato per 12 mesi", 0.18, "Edison", "https://example.com/edison-logo.png"),
				new OffertaEnergia(null, "Energia Variabile", "Tariffa indicizzata", 0.13, "Iberdrola", "https://example.com/iberdrola-logo.png")
			);
			offertaEnergiaRepository.saveAll(offerteEnergia);
			System.out.println("✅ Offerte Energia inserite nel database!");
		}

		if (offertaGasRepository.count() == 0) {
			List<OffertaGas> offerteGas = List.of(
				new OffertaGas(null, "Gas Domestico", "Tariffa fissa 24 mesi", 0.75, "Eni", "https://example.com/eni-logo.png"),
				new OffertaGas(null, "Gas Risparmio", "Prezzo indicizzato al PSV", 0.69, "Hera", "https://example.com/hera-logo.png"),
				new OffertaGas(null, "Gas Business", "Offerta dedicata alle aziende", 0.80, "A2A", "https://example.com/a2a-logo.png")
			);
			offertaGasRepository.saveAll(offerteGas);
			System.out.println("✅ Offerte Gas inserite nel database!");
		}
	}
}
