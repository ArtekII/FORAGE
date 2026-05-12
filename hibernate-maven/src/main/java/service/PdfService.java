package service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import model.Client;
import model.Details;
import model.Devis;

@Service
public class PdfService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("#,##0.00",
            DecimalFormatSymbols.getInstance(Locale.FRANCE));

    private final DevisService devisService;

    public PdfService(DevisService devisService) {
        this.devisService = devisService;
    }

    public byte[] generatePdf() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<Devis> devisList = devisService.getDevis();

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font emptyFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);

            document.add(new Paragraph("Liste des devis", titleFont));
            document.add(new Paragraph(" "));

            if (devisList.isEmpty()) {
                document.add(new Paragraph("Aucun devis trouvé.", emptyFont));
                document.close();
                return out.toByteArray();
            }

            for (Devis devis : devisList) {
                document.add(new Paragraph("Devis #" + devis.getId(), headingFont));
                document.add(new Paragraph("Client : " + buildClientFullName(devis.getClient()), normalFont));
                document.add(new Paragraph("Date d'émission : " + formatDate(devis.getDateEmission()), normalFont));
                document.add(new Paragraph("Montant total : " + formatAmount(BigDecimal.valueOf(devis.getMontantTotal())) + " Ar", normalFont));
                document.add(new Paragraph(" "));

                List<Details> details = devis.getDetails();
                if (details == null || details.isEmpty()) {
                    document.add(new Paragraph("Aucune ligne de détail.", emptyFont));
                } else {
                    PdfPTable table = new PdfPTable(5);
                    table.setWidthPercentage(100f);
                    table.setWidths(new float[] { 3f, 1.2f, 1f, 1.6f, 1.6f });

                    addHeaderCell(table, "Désignation");
                    addHeaderCell(table, "Qté");
                    addHeaderCell(table, "Unité");
                    addHeaderCell(table, "Prix unitaire");
                    addHeaderCell(table, "Montant");

                    for (Details detail : details) {
                        table.addCell(new PdfPCell(new Phrase(safeText(detail.getDesignation()), normalFont)));
                        table.addCell(new PdfPCell(new Phrase(formatAmount(detail.getQuantite()), normalFont)));
                        table.addCell(new PdfPCell(new Phrase(safeText(detail.getUnite()), normalFont)));
                        table.addCell(new PdfPCell(new Phrase(formatAmount(detail.getPrixUnitaire()), normalFont)));
                        table.addCell(new PdfPCell(new Phrase(formatAmount(resolveLineAmount(detail)), normalFont)));
                    }

                    document.add(table);
                }

                document.add(new Paragraph(" "));
                Paragraph separator = new Paragraph("____________________________________________________________");
                separator.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(separator);
                document.add(new Paragraph(" "));
            }

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private void addHeaderCell(PdfPTable table, String label) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        PdfPCell cell = new PdfPCell(new Phrase(label, headerFont));
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);
    }

    private String buildClientFullName(Client client) {
        if (client == null) {
            return "N/A";
        }
        String nom = safeText(client.getNom());
        String prenom = safeText(client.getPrenom());
        String fullName = (nom + " " + prenom).trim();
        return fullName.isEmpty() ? "N/A" : fullName;
    }

    private String formatDate(LocalDateTime date) {
        return date == null ? "N/A" : date.format(DATE_FORMAT);
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }

    private String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return AMOUNT_FORMAT.format(amount);
    }

    private BigDecimal resolveLineAmount(Details detail) {
        if (detail.getMontantParLigne() != null) {
            return detail.getMontantParLigne();
        }
        if (detail.getQuantite() == null || detail.getPrixUnitaire() == null) {
            return BigDecimal.ZERO;
        }
        return detail.getQuantite().multiply(detail.getPrixUnitaire());
    }
}
