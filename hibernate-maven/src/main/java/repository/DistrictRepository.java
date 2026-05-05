package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<model.District, Long> {
    Optional<model.District> findByLibelleAndRegion_Id(String libelle, Long regionId);
    List<model.District> findByRegion_Id(Long regionId);
}
