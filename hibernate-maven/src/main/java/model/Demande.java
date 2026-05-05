package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "date_demande", nullable = false, updatable = false)
    private LocalDateTime dateDemande;

    @Column(nullable = false)
    private String lieu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commune_id", nullable = false)
    private Commune commune;

    protected Demande() {}

    public Demande(Client client, String lieu, Commune commune) {
        this.client = client;
        this.lieu = lieu;
        this.commune = commune;
    }

    public Demande(String reference, Client client, LocalDateTime dateDemande, String lieu, Commune commune) {
        this.reference = reference;
        this.client = client;
        this.dateDemande = dateDemande;
        this.lieu = lieu;
        this.commune = commune;
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

    public Client getClient() {
        return client;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public String getLieu() {
        return lieu;
    }

    public Commune getCommune() {
        return commune;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }
}
