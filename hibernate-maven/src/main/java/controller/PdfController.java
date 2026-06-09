package controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import service.PdfService;

@Controller
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/devis/pdf")
    public ResponseEntity<byte[]> exportPdf() {
        byte[] pdf = pdfService.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
            ContentDisposition.attachment()
                .filename("export.pdf")
                .build()
        );

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}