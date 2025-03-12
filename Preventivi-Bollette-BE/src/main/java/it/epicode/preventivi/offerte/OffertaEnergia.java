package it.epicode.preventivi.offerte;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offerte_energia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffertaEnergia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String descrizione;
	private double prezzoKwh;
	private String fornitore;
	private String logoUrl;
}
