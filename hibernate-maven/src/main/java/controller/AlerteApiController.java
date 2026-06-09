package controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.AlerteEvaluation;
import model.DemandeAlerte;
import service.AlerteService;

@RestController
@RequestMapping("/api/alertes")
public class AlerteApiController {
    private final AlerteService alerteService;

    public AlerteApiController(AlerteService alerteService) {
        this.alerteService = alerteService;
    }

    @GetMapping(value = "/demandes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDemandesAvecAlertes() {
        List<DemandeAlerte> demandesAlertes = alerteService.getToutesDemandesAvecAlertes();
        StringBuilder json = new StringBuilder();

        json.append("[");
        for (int i = 0; i < demandesAlertes.size(); i++) {
            appendDemandeAlerte(json, demandesAlertes.get(i));
            if (i < demandesAlertes.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        return ResponseEntity.ok(json.toString());
    }

    private void appendDemandeAlerte(StringBuilder json, DemandeAlerte demandeAlerte) {
        String client = "";
        if (demandeAlerte.getDemande().getClient() != null) {
            String nom = nullToEmpty(demandeAlerte.getDemande().getClient().getNom()).trim();
            String prenom = nullToEmpty(demandeAlerte.getDemande().getClient().getPrenom()).trim();
            client = (nom + " " + prenom).trim();
        }

        String commune = "";
        if (demandeAlerte.getDemande().getCommune() != null) {
            commune = nullToEmpty(demandeAlerte.getDemande().getCommune().getLibelle());
        }

        json.append("{");
        json.append("\"id\":").append(demandeAlerte.getDemande().getId()).append(",");
        json.append("\"reference\":\"").append(escapeJson(demandeAlerte.getDemande().getReference())).append("\",");
        json.append("\"client\":\"").append(escapeJson(client)).append("\",");
        json.append("\"lieu\":\"").append(escapeJson(demandeAlerte.getDemande().getLieu())).append("\",");
        json.append("\"commune\":\"").append(escapeJson(commune)).append("\",");
        json.append("\"dateDemande\":\"").append(escapeJson(demandeAlerte.getDemande().getFormattedDateDemande())).append("\",");
        json.append("\"alertes\":");
        appendAlertes(json, demandeAlerte.getAlertes());
        json.append("}");
    }

    private void appendAlertes(StringBuilder json, List<AlerteEvaluation> alertes) {
        json.append("[");
        for (int i = 0; i < alertes.size(); i++) {
            AlerteEvaluation alerte = alertes.get(i);
            json.append("{");
            json.append("\"niveau\":\"").append(escapeJson(alerte.getNiveau())).append("\",");
            json.append("\"statutDepart\":\"").append(escapeJson(alerte.getStatutDepartLibelle())).append("\",");
            json.append("\"statutArrivee\":\"").append(escapeJson(alerte.getStatutArriveeLibelle())).append("\",");
            json.append("\"dureeMinutes\":").append(alerte.getDureeMinutes()).append(",");
            json.append("\"intervalleMinutes1\":").append(alerte.getIntervalleMinutes1()).append(",");
            json.append("\"intervalleMinutes2\":").append(alerte.getIntervalleMinutes2());
            json.append("}");
            if (i < alertes.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
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
