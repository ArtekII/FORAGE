package repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<model.Client, Long> {
}
