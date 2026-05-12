package service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class StatutMappingUtils {

    private static final class TypeVersStatut {
        private final String sigle;
        private final Long statutId;

        private TypeVersStatut(String sigle, Long statutId) {
            this.sigle = sigle;
            this.statutId = statutId;
        }
    }

    private static final Map<Long, TypeVersStatut> TYPE_TO_STATUT_CONFIG = Map.of(
            1L, new TypeVersStatut("DEC", 2L),
            2L, new TypeVersStatut("DFC", 4L));

    private static final Map<String, Long> STATUT_ID_BY_SIGLE;

    static {
        Map<String, Long> sigleMap = new HashMap<>();
        for (TypeVersStatut config : TYPE_TO_STATUT_CONFIG.values()) {
            sigleMap.put(config.sigle, config.statutId);
        }
        STATUT_ID_BY_SIGLE = Map.copyOf(sigleMap);
    }

    private StatutMappingUtils() {}

    public static String getSigleByIdType(Long typeId) {
        TypeVersStatut config = TYPE_TO_STATUT_CONFIG.get(typeId);
        if (config == null) {
            throw new IllegalArgumentException("Type id non pris en charge: " + typeId);
        }
        return config.sigle;
    }

    public static Long getIdStatutFromSigle(String sigle) {
        if (sigle == null || sigle.isBlank()) {
            throw new IllegalArgumentException("Sigle statut invalide.");
        }
        Long statutId = STATUT_ID_BY_SIGLE.get(sigle.trim().toUpperCase());
        if (statutId == null) {
            throw new IllegalArgumentException("Sigle statut non pris en charge: " + sigle);
        }
        return statutId;
    }

    public static Long getIdStatutFromTypeId(Long typeId) {
        String sigle = getSigleByIdType(typeId);
        return getIdStatutFromSigle(sigle);
    }

    public static Set<Long> getSupportedTypeIds() {
        return TYPE_TO_STATUT_CONFIG.keySet();
    }
}
