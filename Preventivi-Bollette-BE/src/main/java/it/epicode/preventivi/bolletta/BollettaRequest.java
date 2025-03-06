package it.epicode.preventivi.bolletta;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BollettaRequest {
	private TipoBolletta tipo;
	private double spesaMateria;
	private int consumo; //kwH o Smc
	private LocalDate dataInizioFatturazione;
	private LocalDate dataFineFatturazione;
}