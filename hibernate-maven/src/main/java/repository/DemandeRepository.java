package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<model.Demande, Long> {
    Optional<model.Demande> findByReference(String reference);
    boolean existsByReference(String reference);

    List<model.Demande> findByClient_Id(Long clientId);
    List<model.Demande> findByCommune_Id(Long communeId);
    List<model.Demande> findByCommune_District_Id(Long districtId);
    List<model.Demande> findByCommune_District_Region_Id(Long regionId);

    @Query("SELECT DISTINCT d FROM Demande d LEFT JOIN FETCH d.client LEFT JOIN FETCH d.commune c LEFT JOIN FETCH c.district dist LEFT JOIN FETCH dist.region")
    List<model.Demande> findAllWithDetails();
}
