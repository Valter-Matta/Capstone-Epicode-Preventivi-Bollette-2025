package it.epicode.preventivi.offerte;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offerte")
@RequiredArgsConstructor
public class OffertaController {

	private final OffertaEnergiaRepository offertaEnergiaRepository;
	private final OffertaGasRepository offertaGasRepository;

	@GetMapping("/energia")
	public List<OffertaEnergia> getOfferteEnergia() {
		return offertaEnergiaRepository.findAll();
	}

	@GetMapping("/gas")
	public List<OffertaGas> getOfferteGas() {
		return offertaGasRepository.findAll();
	}
}
