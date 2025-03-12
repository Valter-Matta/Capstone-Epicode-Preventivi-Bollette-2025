package it.epicode.preventivi.offerte;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offerte_gas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffertaGas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String descrizione;
	private double prezzoSmc;
	private String fornitore;
	private String logoUrl;
}
