package controller;

import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Demande;
import model.Statut;
import service.DemandeService;
import service.DemandeStatutService;

@RestController
@RequestMapping("/devis")
public class DevisApiController {

    private final DemandeService demandeService;
    private final DemandeStatutService demandeStatutService;

    public DevisApiController(DemandeService demandeService, DemandeStatutService demandeStatutService) {
        this.demandeService = demandeService;
        this.demandeStatutService = demandeStatutService;
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

    @GetMapping(value = "/statuts-by-demande", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStatutsByDemande(@RequestParam("demandeId") Long demandeId) {
        List<Statut> statuts = demandeStatutService.getStatutsForDemande(demandeId);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < statuts.size(); i++) {
            Statut s = statuts.get(i);
            sb.append("{");
            sb.append("\"id\":").append(s.getId()).append(",");
            sb.append("\"libelle\":\"").append(escapeJson(s.getLibelle())).append("\"");
            sb.append("}");
            if (i < statuts.size() - 1) sb.append(",");
        }
        sb.append("]");
        return ResponseEntity.ok(sb.toString());
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
