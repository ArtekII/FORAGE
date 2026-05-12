package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import model.Statut;
import model.StatutDemandeEnum;
import model.TypeDevis;
import repository.StatutRepository;
import repository.TypeRepository;

@Service
public class ReferenceDataStartupValidator {

    private final TypeRepository typeRepository;
    private final StatutRepository statutRepository;

    public ReferenceDataStartupValidator(TypeRepository typeRepository, StatutRepository statutRepository) {
        this.typeRepository = typeRepository;
        this.statutRepository = statutRepository;
    }

    @PostConstruct
    public void validateReferenceData() {
        List<String> missingTypes = new ArrayList<>();
        for (TypeDevis typeDevis : TypeDevis.values()) {
            boolean exists = typeRepository.findByLibelle(typeDevis.getDbLabel()).isPresent();
            if (!exists) {
                missingTypes.add(typeDevis.getDbLabel());
            }
        }

        List<String> missingStatuts = new ArrayList<>();
        List<String> invalidStatutMapping = new ArrayList<>();
        for (StatutDemandeEnum statut : StatutDemandeEnum.values()) {
            Statut dbStatut = statutRepository.findById(statut.getId()).orElse(null);
            if (dbStatut == null) {
                missingStatuts.add(statut.getDbLabel());
                continue;
            }

            if (!statut.getDbLabel().equals(dbStatut.getLibelle())) {
                invalidStatutMapping.add(
                        "{id=" + statut.getId()
                                + ", expected='" + statut.getDbLabel()
                                + "', actual='" + dbStatut.getLibelle() + "'}");
            }
        }

        if (!missingTypes.isEmpty() || !missingStatuts.isEmpty() || !invalidStatutMapping.isEmpty()) {
            throw new RuntimeException(
                    "Validation des données de référence échouée au démarrage."
                            + " Types manquants=" + missingTypes
                            + ", Statuts manquants=" + missingStatuts
                            + ", Mapping statut invalide=" + invalidStatutMapping);
        }
    }
}
