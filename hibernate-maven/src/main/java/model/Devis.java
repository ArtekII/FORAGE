package model;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "devis")
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statut_id", nullable = false)
    private Statut statut;

    @Column(name = "date_emission", nullable = false, updatable = false)
    private LocalDateTime dateEmission;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Details> details = new ArrayList<>();

    @Transient
    private double montantTotal;

    public Devis() {}

    public Devis(Client client, Statut statut) {
        this.client = client;
        this.statut = statut;
    }

    public Devis(Client client, LocalDateTime dateEmission) {
        this.client = client;
        this.dateEmission = dateEmission;
    }

    @PrePersist
    private void initDateEmission() {
        if (dateEmission == null) {
            dateEmission = LocalDateTime.now();
        }
    }

    public void addDetail(Details detail) {
        details.add(detail);
        detail.setDevis(this);
    }

    public void removeDetail(Details detail) {
        details.remove(detail);
        detail.setDevis(null);
    }

    public double calculateMontantTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Details d : details) {
            if (d.getMontantParLigne() != null) {
                sum = sum.add(d.getMontantParLigne());
            }
        }
        return sum.doubleValue();
    }

    public String getFormattedDateEmission() {
        if (dateEmission == null) {
            return "";
        }
        return dateEmission.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Statut getStatut() {
        return statut;
    }

    public LocalDateTime getDateEmission() {
        return dateEmission;
    }

    public double getMontantTotal() {
        return calculateMontantTotal();
    }
    
    public List<Details> getDetails() {
        return details;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public void setDateEmission(LocalDateTime dateEmission) {
        this.dateEmission = dateEmission;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }
}
