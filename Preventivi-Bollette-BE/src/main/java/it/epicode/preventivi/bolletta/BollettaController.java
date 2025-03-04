package it.epicode.preventivi.bolletta;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/bollette")
@RequiredArgsConstructor
public class BollettaController {
	private final BollettaService bollettaService;


	@GetMapping
	public ResponseEntity<List<Bolletta>> findAll() {
		return ResponseEntity.ok(bollettaService.findAll());
	}

	@PostMapping
	public ResponseEntity<Bolletta> salvaBolletta(@RequestBody Bolletta bolletta) {
		return ResponseEntity.ok(bollettaService.salva(bolletta));
	}

}