package model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "details")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "devis_id", nullable = false)
    private Devis devis;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "quantite", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantite;

    @Column(name = "unite", nullable = false)
    private String unite;

    @Column(name = "prix_unitaire", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "montant_par_ligne", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal montantParLigne;

    public Details() {}

    public Details(Devis devis, String designation, BigDecimal quantite, String unite, BigDecimal prixUnitaire) {
        this.devis = devis;
        this.designation = designation;
        this.quantite = quantite;
        this.unite = unite;
        this.prixUnitaire = prixUnitaire;
    }

    public Long getId() {
        return id;
    }

    public Devis getDevis() {
        return devis;
    }

    public String getDesignation() {
        return designation;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public String getUnite() {
        return unite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public BigDecimal getMontantParLigne() {
        return montantParLigne;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public void setMontantParLigne(BigDecimal montantParLigne) {
        this.montantParLigne = montantParLigne;
    }
}
