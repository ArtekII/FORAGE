package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_status", nullable = false, unique = true)
    private String nomStatus;

    protected Status() {}

    public Status(String nomStatus) {
        this.nomStatus = nomStatus;
    }

    public Long getId() {
        return id;
    }

    public String getNomStatus() {
        return nomStatus;
    }

    public void setNomStatus(String nomStatus) {
        this.nomStatus = nomStatus;
    }
}
