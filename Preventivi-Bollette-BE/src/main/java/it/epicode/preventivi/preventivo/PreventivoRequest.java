package it.epicode.preventivi.preventivo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreventivoRequest {
	private String tipo;
	private double spesaMateriaCliente;
	private int consumo;
	private String meseAnno;
}