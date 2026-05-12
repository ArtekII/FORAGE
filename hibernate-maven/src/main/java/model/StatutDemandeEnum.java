package model;

public enum StatutDemandeEnum {
    DEMANDE_CREER(1L, "Demande Creer"),
    DEVIS_ETUDE_CREER(2L, "Devis Etude Creer"),
    DEVIS_ETUDE_REFUSER(3L, "Devis Etude Refuser"),
    DEVIS_FORAGE_CREER(4L, "Devis Forage Creer"),
    DEVIS_FORAGE_REFUSER(5L, "Devis Forage Refuser"),
    DEMANDE_CLOTUREE(6L, "Demande Cloturee");

    private final Long id;
    private final String dbLabel;

    StatutDemandeEnum(Long id, String dbLabel) {
        this.id = id;
        this.dbLabel = dbLabel;
    }

    public Long getId() {
        return id;
    }

    public String getDbLabel() {
        return dbLabel;
    }
}
