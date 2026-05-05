package controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import model.Demande;
import service.ClientService;
import service.CommuneService;
import service.DemandeService;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    private final DemandeService demandeService;
    private final CommuneService communeService;
    private final ClientService clientService;

    public DemandeController(DemandeService demandeService, CommuneService communeService, ClientService clientService) {
        this.demandeService = demandeService;
        this.communeService = communeService;
        this.clientService = clientService;
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
        List<model.Client> clients = clientService.getClients();
        List<model.Commune> communes = communeService.getAllCommunes();

        String references = new Demande().generateReference();
        mav.addObject("references", references);

        mav.addObject("clients", clients);
        mav.addObject("communes", communes);
        return mav;
    }

    @PostMapping("/create")
    public String createDemande() {
        return "redirect:/demandes";
    }
}
