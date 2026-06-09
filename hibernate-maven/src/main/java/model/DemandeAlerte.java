package model;

import java.util.List;

public class DemandeAlerte {
    private final Demande demande;
    private final List<AlerteEvaluation> alertes;

    public DemandeAlerte(Demande demande, List<AlerteEvaluation> alertes) {
        this.demande = demande;
        this.alertes = alertes;
    }

    public Demande getDemande() {
        return demande;
    }

    public List<AlerteEvaluation> getAlertes() {
        return alertes;
    }
}
