package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatutDemandeRepository extends JpaRepository<model.StatutDemande, Long> {
    List<model.StatutDemande> findByDemande_IdOrderByDateStatutAsc(Long demandeId);
    Optional<model.StatutDemande> findTopByDemande_IdOrderByDateStatutDesc(Long demandeId);
}
