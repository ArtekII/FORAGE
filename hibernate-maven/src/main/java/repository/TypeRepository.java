package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<model.Type, Long> {
    Optional<model.Type> findByLibelle(String libelle);
}
