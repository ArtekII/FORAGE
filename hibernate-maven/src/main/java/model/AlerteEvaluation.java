package model;

public class AlerteEvaluation {
    private final Demande demande;
    private final AlerteParametre parametre;
    private final long dureeMinutes;

    public AlerteEvaluation(Demande demande, AlerteParametre parametre, long dureeMinutes) {
        this.demande = demande;
        this.parametre = parametre;
        this.dureeMinutes = dureeMinutes;
    }

    public Demande getDemande() {
        return demande;
    }

    public AlerteParametre getParametre() {
        return parametre;
    }

    public long getDureeMinutes() {
        return dureeMinutes;
    }

    public long getSeuilMinutes() {
        return parametre.getDureeMinutesAsLong();
    }

    public String getNiveau() {
        return parametre.getNiveau();
    }

    public String getStatutDepartLibelle() {
        return parametre.getStatutDepart().getLibelle();
    }

    public String getStatutArriveeLibelle() {
        return parametre.getStatutArrivee().getLibelle();
    }
}
