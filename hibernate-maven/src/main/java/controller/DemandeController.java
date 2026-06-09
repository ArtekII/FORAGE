package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import model.Client;
import model.Commune;
import model.Demande;
import service.AlerteService;
import service.ClientService;
import service.CommuneService;
import service.DemandeService;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    private final DemandeService demandeService;
    private final CommuneService communeService;
    private final ClientService clientService;
    private final AlerteService alerteService;

    public DemandeController(DemandeService demandeService, CommuneService communeService, ClientService clientService, AlerteService alerteService) {
        this.demandeService = demandeService;
        this.communeService = communeService;
        this.clientService = clientService;
        this.alerteService = alerteService;
    }

    @GetMapping
    public ModelAndView listDemandes(
            @RequestParam(name = "reference", required = false) String reference,
            @RequestParam(name = "clientId", required = false) Long clientId,
            @RequestParam(name = "communeId", required = false) Long communeId,
            @RequestParam(name = "lieu", required = false) String lieu,
            @RequestParam(name = "dateDebut", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime dateDebut,
            @RequestParam(name = "dateFin", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime dateFin
    ) {
        ModelAndView mav = new ModelAndView("demandes/list");

        List<Demande> demandes = demandeService.filtreDemandes(reference,clientId,communeId,lieu,dateDebut,dateFin);
        long dureeTotale = demandeService.getDureeTotaleTermine(demandes);
        alerteService.remplirAlertes(demandes);

        mav.addObject("demandes", demandes);
        mav.addObject("clients", clientService.getClients());
        mav.addObject("communes", communeService.getAllCommunes());
        mav.addObject("dureeTotale", dureeTotale);
        return mav;
    }
    
    @GetMapping("/create")
    public ModelAndView createDemandeForm() {
        ModelAndView mav = new ModelAndView("demandes/create");

        mav.addObject("references", new Demande().generateReference());
        mav.addObject("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        

        mav.addObject("clients", clientService.getClients());
        mav.addObject("communes", communeService.getAllCommunes());
        return mav;
    }

    @PostMapping("/create")
    public String createDemande(
        @RequestParam("reference") String reference, 
        @RequestParam("clientId") Long clientId, 
        @RequestParam("lieu") String lieu, 
        @RequestParam("communeId") Long communeId, 
        @RequestParam("dateDemande")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") 
        LocalDateTime dateDemande) {
        Client client = clientService.getClientById(clientId);
        Commune commune = communeService.getCommuneById(communeId);

        Demande demande = new Demande(reference, client, dateDemande, lieu, commune);
        demandeService.createDemande(demande);
        return "redirect:/demandes";
    }

}
