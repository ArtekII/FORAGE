package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.AlerteEvaluation;
import model.AlerteParametre;
import model.Demande;
import model.DemandeAlerte;
import model.StatutDemande;
import repository.AlerteParametreRepository;
import repository.DemandeRepository;
import repository.StatutDemandeRepository;

@Service
@Transactional(readOnly = true)
public class AlerteService {
    private final AlerteParametreRepository alerteParametreRepository;
    private final DemandeRepository demandeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final CalculDureeTravail calculDureeTravail;

    public AlerteService(
            AlerteParametreRepository alerteParametreRepository,
            DemandeRepository demandeRepository,
            StatutDemandeRepository statutDemandeRepository,
            CalculDureeTravail calculDureeTravail) {
        this.alerteParametreRepository = alerteParametreRepository;
        this.demandeRepository = demandeRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.calculDureeTravail = calculDureeTravail;
    }

    public List<AlerteParametre> getParametres() {
        return alerteParametreRepository.findAllWithStatuts();
    }

    public List<DemandeAlerte> getDemandesAvecAlertes() {
        List<AlerteParametre> parametres = getParametres();
        List<DemandeAlerte> resultats = new ArrayList<>();

        for (Demande demande : demandeRepository.findAllWithDetails()) {
            List<AlerteEvaluation> alertes = getAlertesForDemande(demande, parametres);
            if (!alertes.isEmpty()) {
                resultats.add(new DemandeAlerte(demande, alertes));
            }
        }

        return resultats;
    }

    public List<DemandeAlerte> getToutesDemandesAvecAlertes() {
        List<AlerteParametre> parametres = getParametres();
        List<DemandeAlerte> resultats = new ArrayList<>();

        for (Demande demande : demandeRepository.findAllWithDetails()) {
            resultats.add(new DemandeAlerte(demande, getAlertesForDemande(demande, parametres)));
        }

        return resultats;
    }

    public List<AlerteEvaluation> getAlertesForDemande(Long demandeId) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable: " + demandeId));
        return getAlertesForDemande(demande, getParametres());
    }

    private List<AlerteEvaluation> getAlertesForDemande(Demande demande, List<AlerteParametre> parametres) {
        List<StatutDemande> historique = statutDemandeRepository.findByDemande_IdOrderByDateStatutAscWithDetails(demande.getId());
        Map<String, AlerteEvaluation> meilleuresAlertes = new LinkedHashMap<>();

        for (AlerteParametre parametre : parametres) {
            Long statutDepartId = parametre.getStatutDepart().getId();
            Long statutArriveeId = parametre.getStatutArrivee().getId();
            long duree = calculDureeTravail.calculerDureeEntreStatuts(historique, statutDepartId, statutArriveeId);

            if (duree > parametre.getDureeMinutesAsLong()) {
                String key = statutDepartId + "-" + statutArriveeId;
                AlerteEvaluation candidate = new AlerteEvaluation(demande, parametre, duree);
                AlerteEvaluation existante = meilleuresAlertes.get(key);
                if (existante == null || compareAlertes(candidate, existante) > 0) {
                    meilleuresAlertes.put(key, candidate);
                }
            }
        }

        List<AlerteEvaluation> alertes = new ArrayList<>(meilleuresAlertes.values());
        alertes.sort(Comparator
                .comparingInt((AlerteEvaluation alerte) -> getPrioriteNiveau(alerte.getNiveau())).reversed()
                .thenComparing(AlerteEvaluation::getStatutDepartLibelle)
                .thenComparing(AlerteEvaluation::getStatutArriveeLibelle));
        return alertes;
    }

    private int compareAlertes(AlerteEvaluation a, AlerteEvaluation b) {
        int prioriteCompare = Integer.compare(getPrioriteNiveau(a.getNiveau()), getPrioriteNiveau(b.getNiveau()));
        if (prioriteCompare != 0) {
            return prioriteCompare;
        }
        return Long.compare(a.getSeuilMinutes(), b.getSeuilMinutes());
    }

    private int getPrioriteNiveau(String niveau) {
        if (niveau == null) {
            return 0;
        }
        return switch (niveau.trim().toUpperCase()) {
            case "ROUGE" -> 2;
            case "JAUNE" -> 1;
            default -> 0;
        };
    }
}
