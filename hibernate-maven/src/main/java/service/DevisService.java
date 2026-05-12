package service;

import java.util.List;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.Devis;
import model.Demande;
import model.StatutDemandeEnum;
import model.Statut;
import model.StatutDemande;
import model.TypeDevis;
import repository.DevisRepository;
import repository.StatutRepository;
import repository.StatutDemandeRepository;

@Service
public class DevisService {
    private static final Map<TypeDevis, StatutDemandeEnum> STATUS_BY_TYPE = new EnumMap<>(TypeDevis.class);

    static {
        STATUS_BY_TYPE.put(TypeDevis.ETUDE, StatutDemandeEnum.DEVIS_ETUDE_CREER);
        STATUS_BY_TYPE.put(TypeDevis.FORAGE, StatutDemandeEnum.DEVIS_FORAGE_CREER);
    }

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
        String typeLibelle = devis.getType().getLibelle();
        if (typeLibelle == null || typeLibelle.isBlank()) {
            throw new IllegalArgumentException("Le type du devis est invalide.");
        }

        TypeDevis typeDevis = TypeDevis.fromLibelle(typeLibelle)
                .orElseThrow(() -> new IllegalArgumentException("Type non pris en charge: " + typeLibelle));

        StatutDemandeEnum statutEnum = STATUS_BY_TYPE.get(typeDevis);
        if (statutEnum == null) {
            throw new IllegalArgumentException("Type non pris en charge pour statut: " + typeLibelle);
        }

        Devis savedDevis = devisRepository.save(devis);

        Demande demande = savedDevis.getDemande();
        Statut statut = statutRepository.getReferenceById(statutEnum.getId());
        StatutDemande statutDemande = new StatutDemande(demande, statut);
        statutDemandeRepository.save(statutDemande);

        return savedDevis;
    }
}
