package it.epicode.preventivi.confrontoPrezzi;

import it.epicode.preventivi.bolletta.TipoBolletta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfrontoPrezziResponse {
	private TipoBolletta tipo;
	private double spreadPagato;
	private double prezzoMercato;
	private double risparmioPotenziale;
}
