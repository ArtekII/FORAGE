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
import model.Demande;
import model.Details;
import model.Devis;
import model.Type;
import service.DemandeService;
import service.DevisService;
import service.TypeService;


@Controller
@RequestMapping("/devis")
public class DevisController {
    
    private final DevisService devisService;
    private final DemandeService demandeService;
    private final TypeService typeService;

    public DevisController(DevisService devisService, DemandeService demandeService, TypeService typeService) {
        this.devisService = devisService;
        this.demandeService = demandeService;
        this.typeService = typeService;
    }

    @GetMapping({"", "/"})
    public ModelAndView listDevis() {
        ModelAndView mav = new ModelAndView("devis/list");
        List<Devis> devis = devisService.getDevis();
        mav.addObject("devis", devis);
        return mav;
    }
    
    @GetMapping("/create")
    public ModelAndView createDevisForm() {
        ModelAndView mav = new ModelAndView("devis/create");
        mav.addObject("demandes", demandeService.getAllDemandes());
        mav.addObject("types", typeService.getTypes());
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
        @RequestParam("demandeId") Long demandeId,
        @RequestParam("typeId") Long typeId,
        @RequestParam(value = "observation", required = false) String observation,
        @RequestParam(value = "dateEmission", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateEmission,
        HttpServletRequest request) {
        Demande demande = demandeService.getDemandeById(demandeId);
        Type type = typeService.getTypeById(typeId);
        Devis devis = new Devis(demande, type, dateEmission, observation);

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
