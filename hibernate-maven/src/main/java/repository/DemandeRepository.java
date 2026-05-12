package repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Demande;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<model.Demande, Long> {
    Optional<model.Demande> findByReference(String reference);
    @Query("""
        SELECT d FROM Demande d
        LEFT JOIN FETCH d.client
        WHERE UPPER(TRIM(d.reference)) = UPPER(TRIM(:reference))
    """)
    Optional<Demande> findByReferenceNormalized(@Param("reference") String reference);
    boolean existsByReference(String reference);

    List<model.Demande> findByClient_Id(Long clientId);
    List<model.Demande> findByCommune_Id(Long communeId);
    List<model.Demande> findByCommune_District_Id(Long districtId);
    List<model.Demande> findByCommune_District_Region_Id(Long regionId);
    
    @Query("""
        SELECT d FROM Demande d
        JOIN FETCH d.client c
        JOIN FETCH d.commune co
        WHERE (:reference IS NULL OR :reference = '' OR d.reference LIKE CONCAT('%', :reference, '%'))
        AND (:lieu IS NULL OR :lieu = '' OR d.lieu LIKE CONCAT('%', :lieu, '%'))
        AND (:clientId IS NULL OR c.id = :clientId)
        AND (:communeId IS NULL OR co.id = :communeId)
        AND (:dateDebut IS NULL OR d.dateDemande >= :dateDebut)
        AND (:dateFin IS NULL OR d.dateDemande <= :dateFin)
        ORDER BY d.dateDemande DESC
    """)
    List<Demande> filtreDemandes(
            @Param("reference") String reference,
            @Param("clientId") Long clientId,
            @Param("communeId") Long communeId,
            @Param("lieu") String lieu,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin
    );

    @Query("SELECT DISTINCT d FROM Demande d LEFT JOIN FETCH d.client LEFT JOIN FETCH d.commune c LEFT JOIN FETCH c.district dist LEFT JOIN FETCH dist.region")
    List<model.Demande> findAllWithDetails();
}
