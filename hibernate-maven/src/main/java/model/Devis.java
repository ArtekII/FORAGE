package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @Column(name = "observation")
    private String observation;

    @Column(name = "date_emission", nullable = false, updatable = false)
    private LocalDateTime dateEmission;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Details> details = new ArrayList<>();

    @Transient
    private double montantTotal;

    public Devis() {}

    public Devis(Demande demande, Type type) {
        this.demande = demande;
        this.type = type;
    }

    public Devis(Demande demande, Type type, LocalDateTime dateEmission, String observation) {
        this.demande = demande;
        this.type = type;
        this.dateEmission = dateEmission;
        this.observation = observation;
    }

    @PrePersist
    private void initDateEmission() {
        if (demande == null) {
            throw new IllegalStateException("Le devis doit être lié à une demande avant d'être persisté.");
        }
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

    public Client getClient() {
        return demande == null ? null : demande.getClient();
    }

    public Long getId() {
        return id;
    }

    public Demande getDemande() {
        return demande;
    }

    public Type getType() {
        return type;
    }

    public String getObservation() {
        return observation;
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

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setDateEmission(LocalDateTime dateEmission) {
        this.dateEmission = dateEmission;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }
}
