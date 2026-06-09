package model;

import java.util.Locale;

public class AlerteEvaluation {
    private final Demande demande;
    private final AlerteParametre parametre;
    private final double dureeMinutes;

    public AlerteEvaluation(Demande demande, AlerteParametre parametre, double dureeMinutes) {
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

    public double getDureeMinutes() {
        return dureeMinutes;
    }

    public String getFormattedDureeHeures() {
        return formatMinutesEnHeures(dureeMinutes);
    }

    public String getFormattedIntervalleHeures() {
        return formatMinutesEnHeures(getIntervalleMinutes1()) + " - " + formatMinutesEnHeures(getIntervalleMinutes2());
    }

    public double getIntervalleMinutes1() {
        return parametre.getIntervalleMinutes1();
    }

    public double getIntervalleMinutes2() {
        return parametre.getIntervalleMinutes2();
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

    private String formatMinutesEnHeures(double minutes) {
        return String.format(Locale.US, "%.2f h", minutes / 60.0);
    }
}
