package service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import model.StatutDemande;
import org.springframework.stereotype.Service;

@Service
public class CalculDureeTravail {
    private static final LocalTime DEBUT_JOURNEE = LocalTime.of(8, 0);
    private static final LocalTime FIN_JOURNEE = LocalTime.of(16, 0);

    public long calculerMinutesOuvrees(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null || !debut.isBefore(fin)) {
            return 0;
        }

        LocalDate dateCourante = debut.toLocalDate();
        LocalDate dateFin = fin.toLocalDate();
        long minutes = 0;

        while (!dateCourante.isAfter(dateFin)) {
            if (estJourOuvrable(dateCourante)) {
                LocalDateTime intervalDebut = dateCourante.atTime(DEBUT_JOURNEE);
                LocalDateTime intervalFin = dateCourante.atTime(FIN_JOURNEE);

                if (dateCourante.equals(debut.toLocalDate())) {
                    intervalDebut = max(intervalDebut, debut);
                }
                if (dateCourante.equals(fin.toLocalDate())) {
                    intervalFin = min(intervalFin, fin);
                }

                if (intervalDebut.isBefore(intervalFin)) {
                    minutes += ChronoUnit.MINUTES.between(intervalDebut, intervalFin);
                }
            }
            dateCourante = dateCourante.plusDays(1);
        }

        return minutes;
    }

    public double calculerDureeTravail(StatutDemande precedent, StatutDemande actuel) {
        if (precedent == null || actuel == null) {
            return 0;
        }
        return calculerMinutesOuvrees(precedent.getDateStatut(), actuel.getDateStatut());
    }

    public void recalculerDurees(List<StatutDemande> statutDemandes) {
        if (statutDemandes == null || statutDemandes.isEmpty()) {
            return;
        }

        statutDemandes.sort(Comparator.comparing(StatutDemande::getDateStatut));

        StatutDemande precedent = null;
        for (StatutDemande courant : statutDemandes) {
            if (precedent == null) {
                courant.setDureeTravail(0);
            } else {
                courant.setDureeTravail(calculerDureeTravail(precedent, courant));
            }
            precedent = courant;
        }
    }

    private boolean estJourOuvrable(LocalDate date) {
        DayOfWeek jour = date.getDayOfWeek();
        return jour != DayOfWeek.SATURDAY && jour != DayOfWeek.SUNDAY;
    }

    private LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.isAfter(b) ? a : b;
    }

    private LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        return a.isBefore(b) ? a : b;
    }
}
