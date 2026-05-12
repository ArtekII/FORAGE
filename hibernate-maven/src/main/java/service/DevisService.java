package service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.Devis;
import model.Demande;
import model.Statut;
import model.StatutDemande;
import repository.DevisRepository;
import repository.StatutRepository;
import repository.StatutDemandeRepository;

@Service
public class DevisService {
    private final DevisRepository devisRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutRepository statutRepository;

    public DevisService(
            DevisRepository devisRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutRepository statutRepository) {
        this.devisRepository = devisRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutRepository = statutRepository;
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
        StatutDemande statutDemande = new StatutDemande(demande, statut);
        statutDemandeRepository.save(statutDemande);

        return savedDevis;
    }
}
