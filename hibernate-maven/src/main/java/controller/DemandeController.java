package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import model.Demande;

@Controller
@RequestMapping("/demandes")
public class DemandeController {
    
    private final service.DemandeService demandeService;

    public DemandeController(service.DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public ModelAndView listDemandes() {
        ModelAndView mav = new ModelAndView("demandes/list");
        List<Demande> demandes = demandeService.getAllDemandes();
        mav.addObject("demandes", demandes);
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createDemandeForm() {
        ModelAndView mav = new ModelAndView("demandes/create");
        // Ajouter les objets nécessaires pour le formulaire de création
        // mav.addObject("demande", new Demande());
        // String reference = generateReference(); // Méthode pour générer une référence unique (existe dans le modele demande)
        // mav.addObject("reference", reference);
        return mav;
    }

    @PostMapping("/create")
    public String createDemande(/* @ModelAttribute Demande demande */) {
        // Traiter la création de la demande
        // demandeService.createDemande(demande);
        // Ajout d'un status_demande pour cet demande et le status "demande_creer"
        return "redirect:/demandes";
    }
}
