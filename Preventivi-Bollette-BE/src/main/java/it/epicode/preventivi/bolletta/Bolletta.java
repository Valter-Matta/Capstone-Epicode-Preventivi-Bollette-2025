package it.epicode.preventivi.bolletta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "bollette_energia")
public class Bolletta {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated (EnumType.STRING)
	private TipoBolletta tipo;

	private double importoTotale;
	private double spesaMateria;
	private int consumo; //kwH o Smc
	private LocalDate periodoFatturazione;


}

