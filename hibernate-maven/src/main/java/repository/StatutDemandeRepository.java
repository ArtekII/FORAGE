package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StatutDemandeRepository extends JpaRepository<model.StatutDemande, Long> {
    List<model.StatutDemande> findByDemande_IdOrderByDateStatutAsc(Long demandeId);
    Optional<model.StatutDemande> findTopByDemande_IdOrderByDateStatutDesc(Long demandeId);
    @Query("SELECT sd FROM StatutDemande sd JOIN FETCH sd.demande d JOIN FETCH d.client LEFT JOIN FETCH sd.statut ORDER BY sd.dateStatut DESC")
    List<model.StatutDemande> findAllWithDetails();

    @Query("SELECT sd FROM StatutDemande sd JOIN FETCH sd.demande d JOIN FETCH d.client LEFT JOIN FETCH sd.statut WHERE sd.id = :id")
    Optional<model.StatutDemande> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT sd FROM StatutDemande sd JOIN FETCH sd.statut s WHERE sd.demande.id = :demandeId ORDER BY sd.dateStatut ASC")
    List<model.StatutDemande> findByDemande_IdOrderByDateStatutAscWithDetails(@Param("demandeId") Long demandeId);

}
