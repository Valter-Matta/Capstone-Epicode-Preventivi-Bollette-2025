package it.epicode.preventivi.bolletta;


import it.epicode.preventivi.preventivo.PreventivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/bollette")
@RequiredArgsConstructor
public class BollettaController {
	private final BollettaService bollettaService;
	private final PreventivoService preventivoService;


	@GetMapping
	public List<Bolletta> findAll() {
		return bollettaService.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Bolletta salvaBolletta(@RequestBody BollettaRequest bolletta) {
		Bolletta entity = new Bolletta();
		BeanUtils.copyProperties(bolletta, entity);

		return bollettaService.salva(entity);
	}


}