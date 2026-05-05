package service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.Demande;
import model.Status;
import model.StatusDemande;
import repository.DemandeRepository;
import repository.StatusDemandeRepository;
import repository.StatusRepository;

@Service
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final StatusDemandeRepository statusDemandeRepository;
    private final StatusRepository statusRepository;
    private static final String STATUS_DEMANDE_CREATE = "demande_creer";

    public DemandeService(
            DemandeRepository demandeRepository,
            StatusDemandeRepository statusDemandeRepository,
            StatusRepository statusRepository) {
        this.demandeRepository = demandeRepository;
        this.statusDemandeRepository = statusDemandeRepository;
        this.statusRepository = statusRepository;
    }

    public List<Demande> getAllDemandes() {
        return demandeRepository.findAllWithDetails();
    }

    @Transactional
    public Demande createDemande(Demande demande) {
        Demande savedDemande = demandeRepository.save(demande);

        Optional<Status> maybeStatus = statusRepository.findByLibelle(STATUS_DEMANDE_CREATE);
        Status statusCreer = maybeStatus.orElseThrow(
                () -> new IllegalStateException("Statut introuvable: " + STATUS_DEMANDE_CREATE));

        StatusDemande statusDemande = new StatusDemande(savedDemande, statusCreer);
        statusDemandeRepository.save(statusDemande);

        return savedDemande;
    }
}
