package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<model.Region, Long> {
    Optional<model.Region> findByLibelle(String libelle);
}
