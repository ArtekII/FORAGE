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

    public long calculerDureeTotal(List<StatutDemande> statutDemandes) {
        if (statutDemandes == null || statutDemandes.size() < 2) {
            return 0;
        }

        statutDemandes.sort(Comparator.comparing(StatutDemande::getDateStatut));
        long total = 0;

        for (int i = 1; i < statutDemandes.size(); i++) {
            total += calculerDureeTravail(statutDemandes.get(i - 1), statutDemandes.get(i));
        }

        return total;
    }

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

    public long calculerDureeEntreStatuts(List<StatutDemande> statutDemandes, Long statutDepartId, Long statutArriveeId) {
        if (statutDemandes == null || statutDemandes.size() < 2 || statutDepartId == null || statutArriveeId == null) {
            return 0;
        }

        statutDemandes.sort(Comparator.comparing(StatutDemande::getDateStatut));
        long dureeMax = 0;

        for (int i = 0; i < statutDemandes.size(); i++) {
            StatutDemande depart = statutDemandes.get(i);
            if (!hasStatutId(depart, statutDepartId)) {
                continue;
            }

            for (int j = i + 1; j < statutDemandes.size(); j++) {
                StatutDemande arrivee = statutDemandes.get(j);
                if (hasStatutId(arrivee, statutArriveeId)) {
                    long duree = calculerMinutesOuvrees(depart.getDateStatut(), arrivee.getDateStatut());
                    dureeMax = Math.max(dureeMax, duree);
                    break;
                }
            }
        }

        return dureeMax;
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

    private boolean hasStatutId(StatutDemande statutDemande, Long statutId) {
        return statutDemande != null
                && statutDemande.getStatut() != null
                && statutId.equals(statutDemande.getStatut().getId());
    }

    private LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.isAfter(b) ? a : b;
    }

    private LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        return a.isBefore(b) ? a : b;
    }
}
