package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.AlerteParametre;

public interface AlerteParametreRepository extends JpaRepository<AlerteParametre, Long> {
    @Query("SELECT ap FROM AlerteParametre ap JOIN FETCH ap.statutDepart JOIN FETCH ap.statutArrivee ORDER BY ap.statutDepart.id, ap.statutArrivee.id, ap.intervalleMinutes1 DESC")
    List<AlerteParametre> findAllWithStatuts();
}
