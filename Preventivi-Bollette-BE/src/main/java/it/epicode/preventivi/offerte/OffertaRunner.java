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
				new OffertaEnergia(null, "Energia Verde", "Energia 100% rinnovabile", 0.15, "Enel", "https://www.enel.it/content/dam/enel-it/immagini/social-hub/Homepage.png"),
				new OffertaEnergia(null, "Energia Bloccata", "Prezzo bloccato per 12 mesi", 0.18, "Edison", "https://www.smau.it/_default_upload_bucket/Edison_com_RGB_600.png"),
				new OffertaEnergia(null, "Energia Variabile", "Tariffa indicizzata", 0.13, "Iberdrola", "https://www.h2it.it/wp-content/uploads/2000/10/logo-iberdrola.jpg")
			);
			offertaEnergiaRepository.saveAll(offerteEnergia);
			System.out.println("✅ Offerte Energia inserite nel database!");
		}

		if (offertaGasRepository.count() == 0) {
			List<OffertaGas> offerteGas = List.of(
				new OffertaGas(null, "Gas Domestico", "Tariffa fissa 24 mesi", 0.75, "Eni", "https://www.rundesign.it/wp-content/uploads/2022/04/logo_eni_2022.jpg"),
				new OffertaGas(null, "Gas Risparmio", "Prezzo indicizzato al PSV", 0.69, "Hera", "https://res.cloudinary.com/dlhhugvi8/f_auto,q_auto,w_650/facile.it/logos/fornitori-energia/hera_wt2ii6.png?v=1711381692729"),
				new OffertaGas(null, "Gas Business", "Offerta dedicata alle aziende", 0.80, "A2A", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/A2A_%28Unternehmen%29.svg/800px-A2A_%28Unternehmen%29.svg.png")
			);
			offertaGasRepository.saveAll(offerteGas);
			System.out.println("✅ Offerte Gas inserite nel database!");
		}
	}
}
