package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<model.Demande, Long> {
    Optional<model.Demande> findByReference(String reference);
    boolean existsByReference(String reference);
}
