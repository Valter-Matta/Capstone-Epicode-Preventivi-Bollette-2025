package it.epicode.preventivi.spread;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface SpreadEnergiaRepository extends JpaRepository<SpreadEnergia, Long> {
	@Query ("SELECT s FROM SpreadEnergia s WHERE MONTH(s.data) = :month AND YEAR(s.data) = :year")
	Optional<SpreadEnergia> findByMonthAndYear (@Param ("month") int month, @Param ("year") int year);

	Optional<SpreadEnergia> findByData (LocalDate data);
}

