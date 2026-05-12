package model;

import java.util.Arrays;
import java.util.Optional;

public enum TypeDevis {
    ETUDE("Etude"),
    FORAGE("Forage");

    private final String dbLabel;

    TypeDevis(String dbLabel) {
        this.dbLabel = dbLabel;
    }

    public String getDbLabel() {
        return dbLabel;
    }

    public static Optional<TypeDevis> fromLibelle(String libelle) {
        if (libelle == null || libelle.isBlank()) {
            return Optional.empty();
        }

        String normalized = libelle.trim();
        return Arrays.stream(values())
                .filter(type -> type.dbLabel.equalsIgnoreCase(normalized) || type.name().equalsIgnoreCase(normalized))
                .findFirst();
    }
}
