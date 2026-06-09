package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsRepository extends JpaRepository<model.Details, Long> {
    List<model.Details> findByDevis_Id(Long devisId);
}
