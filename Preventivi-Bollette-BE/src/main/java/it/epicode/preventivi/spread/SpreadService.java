package it.epicode.preventivi.spread;

import it.epicode.preventivi.postResponse.PostResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

@Service
@Validated
@RequiredArgsConstructor
public class SpreadService {
	private final SpreadGasRepository gasRepository;
	private final SpreadEnergiaRepository energiaRepository;


	public PostResponse saveEnergySpread (@Valid SpreadEnergia request) {
		SpreadEnergia entity = new SpreadEnergia();
		entity.setPrezzo(request.getPrezzo());
		entity.setData(request.getData());
		entity = energiaRepository.save(entity);
		PostResponse response = new PostResponse();
		response.setId(entity.getId());
		return response;
	}

	public PostResponse saveGasSpread (@Valid SpreadGas request) {
		SpreadGas entity = new SpreadGas();
		entity.setPrezzo(request.getPrezzo());
		entity.setData(request.getData());
		entity = gasRepository.save(entity);
		PostResponse response = new PostResponse();
		response.setId(entity.getId());
		return response;
	}
}
