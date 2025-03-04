package it.epicode.preventivi.confrontoPrezzi;

import it.epicode.preventivi.bolletta.TipoBolletta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropostaRisparmioResponse {
	private TipoBolletta tipo;
	private double nuovoPrezzo;
	private double risparmioStimato;
	private String suggerimento;
}
