package it.epicode.preventivi.preventivo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreventivoResponse {
	private Long id;
	private double spread;
	private double prezzoMercato;
}
