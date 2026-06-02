package service;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.springframework.stereotype.Service;

import repository.StatutDemandeRepository;
import model.StatutDemande;
import model.Statut;

@Service
public class DemandeStatutService {
    private final StatutDemandeRepository statutDemandeRepository;
    private final DemandeService demandeService;
    private final StatutService statutService;

    public DemandeStatutService(StatutDemandeRepository statutDemandeRepository, DemandeService demandeService, StatutService statutService) {
        this.statutDemandeRepository = statutDemandeRepository;
        this.demandeService = demandeService;
        this.statutService = statutService;
    }

    public void createDemandeStatus(Long demandeId, Long statutId, String dateChangement, String observation) {
        StatutDemande statutDemande = new StatutDemande();
        statutDemande.setDemande(demandeService.getDemandeById(demandeId));
        statutDemande.setStatut(statutService.getStatutById(statutId));
        statutDemande.setDateStatut(java.time.LocalDateTime.parse(dateChangement));
        statutDemande.setObservation(observation);
        statutDemandeRepository.save(statutDemande);
    }

    public List<StatutDemande> getDemandeStatuts() {
        return statutDemandeRepository.findAllWithDetails();
    }

    public StatutDemande getDemandeStatutById(long id) {
        return statutDemandeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Statut demande introuvable : " + id));
    }

    public void updateDemandeStatus(long id, Long demandeId, Long statutId, String dateChangement, String observation) {
        StatutDemande statutDemande = getDemandeStatutById(id);
        statutDemande.setDemande(demandeService.getDemandeById(demandeId));
        statutDemande.setStatut(statutService.getStatutById(statutId));
        if (dateChangement != null && !dateChangement.isBlank()) {
            statutDemande.setDateStatut(java.time.LocalDateTime.parse(dateChangement));
        }
        if (observation != null) {
            statutDemande.setObservation(observation);
        }
        statutDemandeRepository.save(statutDemande);
    }

    public List<Statut> getStatutsForDemande(Long demandeId) {
        List<StatutDemande> list = statutDemandeRepository.findByDemande_IdOrderByDateStatutAsc(demandeId);
        LinkedHashSet<Statut> set = new LinkedHashSet<>();
        for (StatutDemande sd : list) {
            if (sd.getStatut() != null) {
                set.add(sd.getStatut());
            }
        }
        return new ArrayList<>(set);
    }
}
