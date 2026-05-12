package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import model.Statut;
import model.Type;
import repository.StatutRepository;
import repository.TypeRepository;

@Service
public class ValidationDemarrage {

    private static final Map<Long, String> TYPE_LABEL_BY_ID = Map.of(
            1L, "Etude",
            2L, "Forage");

    private static final Map<Long, String> STATUT_LABEL_BY_ID = Map.of(
            2L, "Devis Etude Creer",
            4L, "Devis Forage Creer");

    private final TypeRepository typeRepository;
    private final StatutRepository statutRepository;

    public ValidationDemarrage(TypeRepository typeRepository, StatutRepository statutRepository) {
        this.typeRepository = typeRepository;
        this.statutRepository = statutRepository;
    }

    @PostConstruct
    public void validate() {
        List<String> errors = new ArrayList<>();

        for (Long typeId : StatutMappingUtils.getSupportedTypeIds()) {
            Type type = typeRepository.findById(typeId).orElse(null);
            if (type == null) {
                errors.add("Type manquant pour id=" + typeId);
                continue;
            }

            String expectedTypeLabel = TYPE_LABEL_BY_ID.get(typeId);
            if (expectedTypeLabel != null && !expectedTypeLabel.equals(type.getLibelle())) {
                errors.add("Type incohérent id=" + typeId
                        + " attendu='" + expectedTypeLabel
                        + "' actuel='" + type.getLibelle() + "'");
            }

            Long statutId = StatutMappingUtils.getIdStatutFromTypeId(typeId);
            Statut statut = statutRepository.findById(statutId).orElse(null);
            if (statut == null) {
                errors.add("Statut manquant pour id=" + statutId + " (typeId=" + typeId + ")");
                continue;
            }

            String expectedStatutLabel = STATUT_LABEL_BY_ID.get(statutId);
            if (expectedStatutLabel != null && !expectedStatutLabel.equals(statut.getLibelle())) {
                errors.add("Statut incohérent id=" + statutId
                        + " attendu='" + expectedStatutLabel
                        + "' actuel='" + statut.getLibelle() + "'");
            }
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException("Validation démarrage échouée: " + errors);
        }
    }
}
