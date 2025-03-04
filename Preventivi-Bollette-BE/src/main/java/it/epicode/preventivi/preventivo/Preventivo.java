package it.epicode.preventivi.preventivo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "Preventivo")
public class Preventivo {
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE)
	private Long id;
   private double spreadApplicato;
   private double consumo;
   private double importoTotale;
   private double spesaMateria;
   private LocalDate periodoFatturazione;
   private String tipo;


}