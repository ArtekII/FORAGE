package model;

import java.time.LocalDateTime;

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

    @Column(name = "date_statut", nullable = false, updatable = false)
    private LocalDateTime dateStatut;

    public StatutDemande() {}

    public StatutDemande(Demande demande, Statut statut) {
        this.demande = demande;
        this.statut = statut;
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
}
