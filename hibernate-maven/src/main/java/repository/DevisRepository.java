package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevisRepository extends JpaRepository<model.Devis, Long> {
    List<model.Devis> findByClient_Id(Long clientId);
    List<model.Devis> findByStatut_Id(Long statutId);

    @Query("SELECT DISTINCT d FROM Devis d LEFT JOIN FETCH d.client LEFT JOIN FETCH d.details")
    List<model.Devis> findAllWithDetails();

    @Query("SELECT DISTINCT d FROM Devis d LEFT JOIN FETCH d.client LEFT JOIN FETCH d.details WHERE d.id = :id")
    Optional<model.Devis> findByIdWithDetails(@Param("id") Long id);
}
