package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public void remplirAlertes(List<Demande> demandes) {
        if (demandes == null || demandes.isEmpty()) {
            return;
        }

        List<AlerteParametre> parametres = getParametres();
        for (Demande demande : demandes) {
            demande.setAlertes(getAlertesForDemande(demande, parametres));
        }
    }

    private List<AlerteEvaluation> getAlertesForDemande(Demande demande, List<AlerteParametre> parametres) {
        List<StatutDemande> historique = statutDemandeRepository.findByDemande_IdOrderByDateStatutAscWithDetails(demande.getId());
        List<AlerteEvaluation> alertes = new ArrayList<>();

        for (AlerteParametre parametre : parametres) {
            Long statutDepartId = parametre.getStatutDepart().getId();
            Long statutArriveeId = parametre.getStatutArrivee().getId();
            double duree = calculerDureeEntreStatutsAvecHistorique(historique, statutDepartId, statutArriveeId);

            if (parametre.contientDuree(duree)) {
                alertes.add(new AlerteEvaluation(demande, parametre, duree));
            }
        }

        alertes.sort(Comparator
                .comparingInt((AlerteEvaluation alerte) -> getPrioriteNiveau(alerte.getNiveau())).reversed()
                .thenComparing(AlerteEvaluation::getStatutDepartLibelle)
                .thenComparing(AlerteEvaluation::getStatutArriveeLibelle)
                .thenComparing(AlerteEvaluation::getIntervalleMinutes1));
        return alertes;
    }

    private double calculerDureeEntreStatutsAvecHistorique(List<StatutDemande> historique, Long statutDepartId, Long statutArriveeId) {
        if (historique == null || historique.size() < 2 || statutDepartId == null || statutArriveeId == null) {
            return 0;
        }

        List<StatutDemande> statuts = new ArrayList<>(historique);
        statuts.sort(Comparator.comparing(StatutDemande::getDateStatut));

        double dureeMax = 0;
        boolean intervalleTrouve = false;

        for (int i = 0; i < statuts.size(); i++) {
            StatutDemande depart = statuts.get(i);
            if (!hasStatutId(depart, statutDepartId)) {
                continue;
            }

            double duree = 0;
            for (int j = i + 1; j < statuts.size(); j++) {
                StatutDemande courant = statuts.get(j);
                duree += courant.getDureeTravail();
                if (hasStatutId(courant, statutArriveeId)) {
                    intervalleTrouve = true;
                    dureeMax = Math.max(dureeMax, duree);
                    break;
                }
            }
        }

        if (intervalleTrouve) {
            return dureeMax;
        }
        return calculDureeTravail.calculerDureeEntreStatuts(statuts, statutDepartId, statutArriveeId);
    }

    private boolean hasStatutId(StatutDemande statutDemande, Long statutId) {
        return statutDemande != null
                && statutDemande.getStatut() != null
                && statutId.equals(statutDemande.getStatut().getId());
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
