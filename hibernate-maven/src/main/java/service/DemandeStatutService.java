package service;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.StatutDemandeRepository;
import model.StatutDemande;
import model.Statut;

@Service
@Transactional
public class DemandeStatutService {
    private final StatutDemandeRepository statutDemandeRepository;
    private final DemandeService demandeService;
    private final StatutService statutService;
    private final CalculDureeTravail calculDureeTravail;

    public DemandeStatutService(StatutDemandeRepository statutDemandeRepository, DemandeService demandeService, StatutService statutService, CalculDureeTravail calculDureeTravail) {
        this.statutDemandeRepository = statutDemandeRepository;
        this.demandeService = demandeService;
        this.statutService = statutService;
        this.calculDureeTravail = calculDureeTravail;
    }

    public void createDemandeStatus(Long demandeId, Long statutId, String dateChangement, String observation) {
        StatutDemande statutDemande = new StatutDemande();
        statutDemande.setDemande(demandeService.getDemandeById(demandeId));
        statutDemande.setStatut(statutService.getStatutById(statutId));
        statutDemande.setDateStatut(java.time.LocalDateTime.parse(dateChangement));
        statutDemande.setObservation(observation);
        statutDemandeRepository.save(statutDemande);
        recalculateDureeTravailForDemande(demandeId);
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
        Long ancienneDemandeId = statutDemande.getDemande() != null ? statutDemande.getDemande().getId() : null;

        statutDemande.setDemande(demandeService.getDemandeById(demandeId));
        statutDemande.setStatut(statutService.getStatutById(statutId));
        if (dateChangement != null && !dateChangement.isBlank()) {
            statutDemande.setDateStatut(java.time.LocalDateTime.parse(dateChangement));
        }
        if (observation != null) {
            statutDemande.setObservation(observation);
        }
        statutDemandeRepository.save(statutDemande);

        if (ancienneDemandeId != null && !ancienneDemandeId.equals(demandeId)) {
            recalculateDureeTravailForDemande(ancienneDemandeId);
        }
        recalculateDureeTravailForDemande(demandeId);
    }

    public void recalculateDureeTravailForDemande(Long demandeId) {
        if (demandeId == null) {
            return;
        }
        List<StatutDemande> statutDemandes = statutDemandeRepository.findByDemande_IdOrderByDateStatutAscWithDetails(demandeId);
        if (statutDemandes == null || statutDemandes.isEmpty()) {
            return;
        }
        Objects.requireNonNull(statutDemandes);
        calculDureeTravail.recalculerDurees(statutDemandes);
        statutDemandeRepository.saveAll(statutDemandes);
    }

    public List<Statut> getStatutsForDemande(Long demandeId) {
        List<StatutDemande> list = statutDemandeRepository.findByDemande_IdOrderByDateStatutAscWithDetails(demandeId);
        LinkedHashSet<Statut> set = new LinkedHashSet<>();
        for (StatutDemande sd : list) {
            if (sd.getStatut() != null) {
                set.add(sd.getStatut());
            }
        }
        return new ArrayList<>(set);
    }
}
