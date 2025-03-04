package it.epicode.preventivi.spread;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "spread_gas")
public class SpreadGas {
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE)
	private Long id;
	private double prezzo;
	private LocalDate data;


	public SpreadGas (double v, LocalDate d) {
		this.prezzo = v;
		this.data = d;
	}
}