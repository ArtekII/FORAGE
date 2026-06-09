package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.District;

public interface CommuneRepository extends JpaRepository<model.Commune, Long> {
    Optional<model.Commune> findByLibelleAndDistrict_Id(String libelle, Long districtId);
    List<model.Commune> findByDistrict_Id(Long districtId);
}
