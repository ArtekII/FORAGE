package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "statut")
public class Statut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sigle", nullable = false, unique = true)
    private String sigle;

    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    protected Statut() {}

    public Statut(Long id, String sigle, String libelle) {
        this.id = id;
        this.sigle = sigle;
        this.libelle = libelle;
    }

    public Statut(String sigle, String libelle) {
        this.sigle = sigle;
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getSigle() {
        return sigle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getNomStatut() {
        return libelle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    

    public void setNomStatut(String libelle) {
        this.libelle = libelle;
    }
}
