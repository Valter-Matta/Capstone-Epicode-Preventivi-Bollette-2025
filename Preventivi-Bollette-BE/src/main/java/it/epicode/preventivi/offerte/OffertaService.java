package it.epicode.preventivi.offerte;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OffertaService {

	private final OffertaEnergiaRepository offertaEnergiaRepository;
	private final OffertaGasRepository offertaGasRepository;

	public List<OffertaEnergia> getOfferteEnergia() {
		return offertaEnergiaRepository.findAll();
	}

	public List<OffertaGas> getOfferteGas() {
		return offertaGasRepository.findAll();
	}
}
