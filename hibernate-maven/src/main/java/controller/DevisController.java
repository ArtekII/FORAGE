package controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import model.Client;
import model.Details;
import model.Devis;
import service.ClientService;
import service.DevisService;


@Controller
@RequestMapping("/devis")
public class DevisController {
    
    private final DevisService devisService;
    private final ClientService clientService;

    public DevisController(DevisService devisService, ClientService clientService) {
        this.devisService = devisService;
        this.clientService = clientService;
    }

    @GetMapping
    public ModelAndView listDevis() {
        ModelAndView mav = new ModelAndView("devis/list");
        List<Devis> devis = devisService.getDevis();
        mav.addObject("devis", devis);
        return mav;
    }
    
    @GetMapping("/create")
    public ModelAndView createDevisForm() {
        ModelAndView mav = new ModelAndView("devis/create");
        mav.addObject("clients", clientService.getClients());
        mav.addObject("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        return mav;
    }

    @GetMapping("/details/{id}")
    public ModelAndView showDevisDetails(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("devis/details");
        mav.addObject("devis", devisService.getDevisById(id));
        return mav;
    }

    @PostMapping("/create")
    public String createDevis(
        @RequestParam("clientId") Long clientId, 
        @RequestParam(value = "dateEmission", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateEmission,
        HttpServletRequest request) {
        Client client = clientService.getClientById(clientId);

        Devis devis = new Devis(client, dateEmission);

        int index = 0;
        while (true) {
            String designation = request.getParameter("details[" + index + "].designation");
            String quantiteValue = request.getParameter("details[" + index + "].quantite");
            String unite = request.getParameter("details[" + index + "].unite");
            String prixUnitaireValue = request.getParameter("details[" + index + "].prixUnitaire");

            if (designation == null || quantiteValue == null || unite == null || prixUnitaireValue == null) {
                break;
            }

            if (!designation.isBlank() && !unite.isBlank()) {
                Details detail = new Details();
                detail.setDesignation(designation.trim());
                detail.setQuantite(new BigDecimal(quantiteValue));
                detail.setUnite(unite.trim());
                detail.setPrixUnitaire(new BigDecimal(prixUnitaireValue));
                devis.addDetail(detail);
            }

            index++;
        }

        devisService.createDevis(devis);
        return "redirect:/devis";
    }

}
