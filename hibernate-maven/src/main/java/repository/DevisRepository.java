package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevisRepository extends JpaRepository<model.Devis, Long> {
    List<model.Devis> findByDemande_Id(Long demandeId);
    List<model.Devis> findByDemande_Client_Id(Long clientId);
    List<model.Devis> findByType_Id(Long typeId);

    @Query("SELECT DISTINCT d FROM Devis d LEFT JOIN FETCH d.demande dm LEFT JOIN FETCH dm.client LEFT JOIN FETCH d.type LEFT JOIN FETCH d.details")
    List<model.Devis> findAllWithDetails();

    @Query("SELECT DISTINCT d FROM Devis d LEFT JOIN FETCH d.demande dm LEFT JOIN FETCH dm.client LEFT JOIN FETCH d.type LEFT JOIN FETCH d.details WHERE d.id = :id")
    Optional<model.Devis> findByIdWithDetails(@Param("id") Long id);
}
