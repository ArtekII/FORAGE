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

    @Column(name = "intervalle_minutes_1", nullable = false)
    private double intervalleMinutes1;

    @Column(name = "intervalle_minutes_2", nullable = false)
    private double intervalleMinutes2;

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

    public double getIntervalleMinutes1() {
        return intervalleMinutes1;
    }

    public double getIntervalleMinutes2() {
        return intervalleMinutes2;
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

    public void setIntervalleMinutes1(double intervalleMinutes1) {
        this.intervalleMinutes1 = intervalleMinutes1;
    }

    public void setIntervalleMinutes2(double intervalleMinutes2) {
        this.intervalleMinutes2 = intervalleMinutes2;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public boolean contientDuree(double dureeMinutes) {
        return dureeMinutes >= intervalleMinutes1
                && dureeMinutes <= intervalleMinutes2;
    }
}
