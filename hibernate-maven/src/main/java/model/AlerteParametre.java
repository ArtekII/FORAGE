package model;

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
@Table(name = "alerte_parametre")
public class AlerteParametre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statut_depart_id", nullable = false)
    private Statut statutDepart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statut_arrivee_id", nullable = false)
    private Statut statutArrivee;

    @Column(name = "duree_minutes", nullable = false)
    private double dureeMinutes;

    @Column(nullable = false)
    private String niveau;

    public AlerteParametre() {}

    public Long getId() {
        return id;
    }

    public Statut getStatutDepart() {
        return statutDepart;
    }

    public Statut getStatutArrivee() {
        return statutArrivee;
    }

    public double getDureeMinutes() {
        return dureeMinutes;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatutDepart(Statut statutDepart) {
        this.statutDepart = statutDepart;
    }

    public void setStatutArrivee(Statut statutArrivee) {
        this.statutArrivee = statutArrivee;
    }

    public void setDureeMinutes(double dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public long getDureeMinutesAsLong() {
        return dureeMinutes == 0 ? 0 : (long) dureeMinutes;
    }
}
