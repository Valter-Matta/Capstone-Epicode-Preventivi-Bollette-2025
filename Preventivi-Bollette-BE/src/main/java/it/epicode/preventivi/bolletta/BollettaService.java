package it.epicode.preventivi.bolletta;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@Service
@Validated
@RequiredArgsConstructor
public class BollettaService {
	private final BollettaRepository bollettaRepository;


	public List<Bolletta> findAll() {
		return bollettaRepository.findAll();
	}

	public Bolletta salva(Bolletta bolletta) {
		return bollettaRepository.save(bolletta);
	}
}
