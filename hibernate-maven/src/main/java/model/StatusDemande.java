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
@Table(name = "status_demande")
public class StatusDemande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "date_status", nullable = false, updatable = false)
    private LocalDateTime dateStatus;

    public StatusDemande() {}

    public StatusDemande(Demande demande, Status status) {
        this.demande = demande;
        this.status = status;
    }

    @PrePersist
    private void initDate() {
        if (dateStatus == null) {
            dateStatus = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Demande getDemande() {
        return demande;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getDateStatus() {
        return dateStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDateStatus(LocalDateTime dateStatus) {
        this.dateStatus = dateStatus;
    }
}
