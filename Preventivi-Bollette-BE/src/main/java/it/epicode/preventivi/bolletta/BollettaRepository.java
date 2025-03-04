package it.epicode.preventivi.bolletta;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BollettaRepository extends JpaRepository<Bolletta, Long> {
}
