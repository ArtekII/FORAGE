package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<model.Status, Long> {
    Optional<model.Status> findByLibelle(String libelle);
}