package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusDemandeRepository extends JpaRepository<model.StatusDemande, Long> {
    List<model.StatusDemande> findByDemande_IdOrderByDateStatusAsc(Long demandeId);
    Optional<model.StatusDemande> findTopByDemande_IdOrderByDateStatusDesc(Long demandeId);
}
