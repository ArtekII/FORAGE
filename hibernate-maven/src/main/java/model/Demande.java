package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "demande")
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String reference;

    @Column(name = "nom_client", nullable = false)
    private String nomClient;

    @Column(name = "date_demande", nullable = false, updatable = false)
    private LocalDateTime dateDemande;

    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String commune;
    @Column(nullable = false)
    private String fokontany;

    protected Demande() {}

    public Demande(String nomClient, String region, String district, String commune, String fokontany) {
        this.nomClient = nomClient;
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.fokontany = fokontany;
    }

    public Demande(String reference, String nomClient, LocalDateTime dateDemande,
            String region, String district, String commune, String fokontany) {
        this.reference = reference;
        this.nomClient = nomClient;
        this.dateDemande = dateDemande;
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.fokontany = fokontany;
    }

    @PrePersist
    private void initDefaults() {
        if (dateDemande == null) {
            dateDemande = LocalDateTime.now();
        }
        if (reference == null || reference.isBlank()) {
            reference = generateReference();
        }
    }

    private String generateReference() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "DEM-" + timestamp + "-" + random;
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getNomClient() {
        return nomClient;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public String getCommune() {
        return commune;
    }

    public String getDistrict() {
        return district;
    }

    public String getFokontany() {
        return fokontany;
    }

    public String getRegion() {
        return region;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setFokontany(String fokontany) {
        this.fokontany = fokontany;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
