package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

@Entity
@Table(name = "statut_demande")
public class StatutDemande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statut_id", nullable = false)
    private Statut statut;

    @Column(name = "date_statut", nullable = false)
    private LocalDateTime dateStatut;

    private String observation;

    @Column(name = "duree_travail_minutes")
    private double dureeTravail;

    public StatutDemande() {}

    public StatutDemande(Demande demande, Statut statut) {
        this.demande = demande;
        this.statut = statut;
    }

    public StatutDemande(Demande demande, Statut statut, LocalDateTime dateStatut) {
        this.demande = demande;
        this.statut = statut;
        this.dateStatut = dateStatut;
    }

    public StatutDemande(Demande demande, Statut statut, LocalDateTime dateStatut, String observation) {
        this.demande = demande;
        this.statut = statut;
        this.dateStatut = dateStatut;
        this.observation = observation;
    }

    public StatutDemande(Demande demande, Statut statut, LocalDateTime dateStatut, String observation, double dureeTravail) {
        this.demande = demande;
        this.statut = statut;
        this.dateStatut = dateStatut;
        this.observation = observation;
        this.dureeTravail = dureeTravail;
    }

    @PrePersist
    private void initDate() {
        if (dateStatut == null) {
            dateStatut = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Demande getDemande() {
        return demande;
    }

    public Statut getStatut() {
        return statut;
    }

    public LocalDateTime getDateStatut() {
        return dateStatut;
    }

    public String getObservation() {
        return observation;
    }

    public double getDureeTravail() {
        return dureeTravail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public void setDateStatut(LocalDateTime dateStatut) {
        this.dateStatut = dateStatut;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    
    public void setDureeTravail(double dureeTravail) {
        this.dureeTravail = dureeTravail;
    }

    public String getFormattedDureeTravail() {
        if (dureeTravail <= 0) {
            return "-";
        }
        return String.valueOf((long) dureeTravail);
    }

    public String getFormattedDateStatut() {
        if (dateStatut == null) {
            return "";
        }
        return dateStatut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getDateStatutInput() {
        if (dateStatut == null) {
            return "";
        }
        return dateStatut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
