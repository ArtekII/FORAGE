package controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Demande;
import service.DemandeService;

@RestController
@RequestMapping("/devis")
public class DevisApiController {

    private final DemandeService demandeService;

    public DevisApiController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping(value = "/demande-by-reference", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findDemandeByReference(@RequestParam("reference") String reference) {
        Optional<Demande> maybeDemande = demandeService.findByReference(reference);
        if (maybeDemande.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"found\":false,\"message\":\"Demande introuvable\"}");
        }

        Demande demande = maybeDemande.get();
        String clientName = "";
        if (demande.getClient() != null) {
            String nom = demande.getClient().getNom() == null ? "" : demande.getClient().getNom().trim();
            String prenom = demande.getClient().getPrenom() == null ? "" : demande.getClient().getPrenom().trim();
            clientName = (nom + " " + prenom).trim();
        }

        String json = "{"
                + "\"found\":true,"
                + "\"id\":" + demande.getId() + ","
                + "\"reference\":\"" + escapeJson(demande.getReference()) + "\","
                + "\"client\":\"" + escapeJson(clientName) + "\","
                + "\"dateDemande\":\"" + escapeJson(demande.getFormattedDateDemande()) + "\","
                + "\"lieu\":\"" + escapeJson(demande.getLieu()) + "\""
                + "}";
        return ResponseEntity.ok(json);
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
