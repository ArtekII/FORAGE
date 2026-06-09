package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatutRepository extends JpaRepository<model.Statut, Long> {
    Optional<model.Statut> findByLibelle(String libelle);
    Optional<model.Statut> findBySigle(String sigle);
}
