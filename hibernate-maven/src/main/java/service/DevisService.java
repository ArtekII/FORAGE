package service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.Devis;
import model.Demande;
import model.Statut;
import model.StatutDemande;
import repository.DevisRepository;
import service.DemandeService;
import repository.StatutRepository;
import repository.StatutDemandeRepository;

@Service
public class DevisService {
    private final DevisRepository devisRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutRepository statutRepository;
    private final DemandeService demandeService;
    private final DemandeStatutService demandeStatutService;

    public DevisService(
            DevisRepository devisRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutRepository statutRepository,
            DemandeService demandeService,
            DemandeStatutService demandeStatutService) {
        this.devisRepository = devisRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutRepository = statutRepository;
        this.demandeService = demandeService;
        this.demandeStatutService = demandeStatutService;
    }

    public List<Devis> getDevis() {
        return devisRepository.findAllWithDetails();
    }

    public Devis getDevisById(Long id) {
        return devisRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Devis introuvable: " + id));
    }

    @Transactional
    public Devis createDevis(Devis devis) {
        if (devis.getDemande() == null) {
            throw new IllegalArgumentException("Le devis doit être lié à une demande.");
        }
        if (devis.getDemande().getId() == null) {
            throw new IllegalArgumentException("La demande liée au devis doit avoir un id valide.");
        }

        Demande managedDemande = demandeService.getDemandeById(devis.getDemande().getId());
        devis.setDemande(managedDemande);

        if (devis.getType() == null) {
            throw new IllegalArgumentException("Le devis doit avoir un type.");
        }
        Long typeId = devis.getType().getId();
        if (typeId == null) {
            throw new IllegalArgumentException("Type id du devis est invalide.");
        }

        Devis savedDevis = devisRepository.save(devis);

        Demande demande = savedDevis.getDemande();
        Long statutId = StatutMappingUtils.getIdStatutFromTypeId(typeId);
        Statut statut = statutRepository.getReferenceById(statutId);
        StatutDemande statutDemande = new StatutDemande(demande, statut, savedDevis.getDateEmission());
        statutDemandeRepository.save(statutDemande);
        demandeStatutService.recalculateDureeTravailForDemande(demande.getId());

        return savedDevis;
    }
}
