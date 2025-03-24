package it.epicode.preventivi.spread;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SpreadRunner implements CommandLineRunner {
	private final SpreadService gasService;
	private final SpreadService energiaService;

	@Override
	@Order (1)
	public void run (String... args) throws Exception {
		//ultimi 5 mesi energia
/*
		SpreadEnergia energiaFebbraio = new SpreadEnergia(0.132, LocalDate.of(2025, 2, 28));
		SpreadEnergia energiaGennaio = new SpreadEnergia(0.143, LocalDate.of(2025, 1, 30));
		SpreadEnergia energiaDicembre = new SpreadEnergia(0.158, LocalDate.of(2024, 12, 30));
		SpreadEnergia energiaNovembre = new SpreadEnergia(0.145, LocalDate.of(2024, 11, 30));
		SpreadEnergia energiaOttobre = new SpreadEnergia(0.123, LocalDate.of(2024, 10, 30));
		energiaService.saveEnergySpread(energiaFebbraio);
		energiaService.saveEnergySpread(energiaGennaio);
		energiaService.saveEnergySpread(energiaDicembre);
		energiaService.saveEnergySpread(energiaNovembre);
		energiaService.saveEnergySpread(energiaOttobre);


		//ultimi 5 mesi gas

		SpreadGas gasFebbraio = new SpreadGas(0.493, LocalDate.of(2025, 2, 28));
		SpreadGas gasGennaio = new SpreadGas(0.509, LocalDate.of(2025, 1, 30));
		SpreadGas gasDicembre = new SpreadGas(0.533, LocalDate.of(2024, 12, 30));
		SpreadGas gasNovembre = new SpreadGas(0.509, LocalDate.of(2024, 11, 30));
		SpreadGas gasOttobre = new SpreadGas(0.483, LocalDate.of(2024, 10, 30));
		gasService.saveGasSpread(gasFebbraio);
		gasService.saveGasSpread(gasGennaio);
		gasService.saveGasSpread(gasDicembre);
		gasService.saveGasSpread(gasNovembre);
		gasService.saveGasSpread(gasOttobre);
*/
	}


}
