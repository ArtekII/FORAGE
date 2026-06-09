package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.Demande;
import model.Statut;
import model.StatutDemande;
import repository.DemandeRepository;
import repository.StatutDemandeRepository;
import repository.StatutRepository;

@Service
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutRepository statutRepository;
    private static final String SIGLE_DEMANDE_CREATE = "C";
    private static final String SIGLE_FORAGE_TERMINE = "FCT";

    public DemandeService(
            DemandeRepository demandeRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutRepository statutRepository) {
        this.demandeRepository = demandeRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutRepository = statutRepository;
    }

    public List<Demande> getAllDemandes() {
        return demandeRepository.findAllWithDetails();
    }

    public Demande getDemandeById(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable: " + id));
    }

    public Optional<Demande> findByReference(String reference) {
        if (reference == null || reference.isBlank()) {
            return Optional.empty();
        }
        return demandeRepository.findByReferenceNormalized(reference.trim());
    }

    @Transactional
    public Demande createDemande(Demande demande) {
        Demande savedDemande = demandeRepository.save(demande);

        Optional<Statut> maybeStatut = statutRepository.findBySigle(SIGLE_DEMANDE_CREATE);
        Statut statutCreer = maybeStatut.orElseThrow(
                () -> new IllegalStateException("Statut initial introuvable pour le sigle: " + SIGLE_DEMANDE_CREATE));

        StatutDemande statutDemande = new StatutDemande(savedDemande, statutCreer, savedDemande.getDateDemande());
        statutDemandeRepository.save(statutDemande);

        return savedDemande;
    }

    public List<Demande> filtreDemandes(
            String reference,
            Long clientId,
            Long communeId,
            String lieu,
            LocalDateTime dateDebut,
            LocalDateTime dateFin
    ) {
        return demandeRepository.filtreDemandes(reference,clientId,communeId,lieu,dateDebut,dateFin);
    }

    public long getDureeTotaleTermine(List<Demande> demandes) {
        if (demandes == null || demandes.isEmpty()) {
            return 0;
        }

        long total = 0;
        for (Demande demande : demandes) {
            long duree = getDureeTotaleTerminee(demande);
            demande.setDureeTotale(duree);
            total += duree;
        }
        return total;
    }

    private long getDureeTotaleTerminee(Demande demande) {
        if (demande == null || demande.getId() == null) {
            return 0;
        }

        List<StatutDemande> historique = statutDemandeRepository
                .findByDemande_IdOrderByDateStatutAscWithDetails(demande.getId());
        if (!isDernierStatutTermine(historique)) {
            return 0;
        }

        return historique.stream()
                .mapToLong(statutDemande -> (long) statutDemande.getDureeTravail())
                .sum();
    }

    private boolean isDernierStatutTermine(List<StatutDemande> historique) {
        if (historique == null || historique.isEmpty()) {
            return false;
        }

        StatutDemande dernier = historique.get(historique.size() - 1);
        return dernier.getStatut() != null
                && dernier.getStatut().getSigle() != null
                && SIGLE_FORAGE_TERMINE.equalsIgnoreCase(dernier.getStatut().getSigle());
    }
}
