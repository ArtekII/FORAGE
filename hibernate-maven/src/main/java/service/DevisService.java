package service;

import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import model.Devis;
import model.Statut;
import repository.DevisRepository;
import repository.StatutRepository;

@Service
public class DevisService {

    private final DevisRepository devisRepository;
    private final StatutRepository statutRepository;
    private static final String STATUS_DEVIS_CREATE = "devis_genere";

    public DevisService(DevisRepository devisRepository, StatutRepository statutRepository) {
        this.devisRepository = devisRepository;
        this.statutRepository = statutRepository;
    }

    public List<Devis> getDevis() {
        return devisRepository.findAllWithDetails();
    }

    public Devis getDevisById(Long id) {
        return devisRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Devis introuvable: " + id));
    }

    public Devis createDevis(Devis devis) {
        Optional<Statut> maybeStatut = statutRepository.findByLibelle(STATUS_DEVIS_CREATE);
        Statut statutCreer = maybeStatut.orElseThrow(
                () -> new IllegalStateException("Statut introuvable: " + STATUS_DEVIS_CREATE));

        devis.setStatut(statutCreer);
        Devis savedDevis = devisRepository.save(devis);
        return savedDevis;
    }
}
