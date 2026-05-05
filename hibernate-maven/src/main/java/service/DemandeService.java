package service;

import java.util.List;

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
    private final String STATUS_DEMANDE_CREATE = "demande_creer";

    public DemandeService(
        DemandeRepository demandeRepository, 
        StatusDemandeRepository statusDemandeRepository, 
        StatusRepository statusRepository) {
        this.demandeRepository = demandeRepository;
        this.statusDemandeRepository = statusDemandeRepository;
        this.statusRepository = statusRepository;
    }

    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Transactional
    public void createDemande(Demande demande) {
        Status statut_creer = statusRepository.findByLibelle(STATUS_DEMANDE_CREATE).get();
        StatusDemande status_demande = new StatusDemande();
        status_demande.setDemande(demande);
        status_demande.setStatus(statut_creer);
        demandeRepository.save(demande);
        statusDemandeRepository.save(status_demande);
    }
}
