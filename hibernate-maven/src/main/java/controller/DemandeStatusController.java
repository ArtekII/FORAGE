package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import model.StatutDemande;
import service.DemandeService;
import service.DemandeStatutService;
import service.StatutService;

@Controller
@RequestMapping("/demande-statut")
public class DemandeStatusController {

    private final DemandeStatutService demandeStatutService;
    private final DemandeService demandeService;
    private final StatutService statutService;

    public DemandeStatusController(DemandeStatutService demandeStatutService, DemandeService demandeService, StatutService statutService) {
        this.demandeStatutService = demandeStatutService;
        this.demandeService = demandeService;
        this.statutService = statutService;
    }
    
    @GetMapping({"", "/"})
    public ModelAndView listDemandeStatus() {
        ModelAndView mav = new ModelAndView("demande-statut/list");
        mav.addObject("demandeStatuts", demandeStatutService.getDemandeStatuts());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createDemandeStatusForm() {
        ModelAndView mav = new ModelAndView("demande-statut/create");
        mav.addObject("demandes", demandeService.getAllDemandes());
        mav.addObject("statuts", statutService.getStatuts());
        mav.addObject("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editDemandeStatusForm(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("demande-statut/edit");
        StatutDemande demandeStatut = demandeStatutService.getDemandeStatutById(id);
        mav.addObject("demandeStatut", demandeStatut);
        mav.addObject("demandes", demandeService.getAllDemandes());
        mav.addObject("statuts", demandeStatutService.getStatutsForDemande(demandeStatut.getDemande().getId()));
        return mav;
    }

    @PostMapping("/edit")
    public String updateDemandeStatus(@RequestParam("id") Long id,
                                      @RequestParam("demandeId") Long demandeId,
                                      @RequestParam("statutId") Long statutId,
                                      @RequestParam(value = "dateChangement", required = false) String dateChangement,
                                      @RequestParam(value = "observation", required = false) String observation) {
        demandeStatutService.updateDemandeStatus(id, demandeId, statutId, dateChangement, observation);
        return "redirect:/demande-statut";
    }

    @PostMapping("/create")
    public String createDemandeStatus(@RequestParam("demandeId") Long demandeId,
                                     @RequestParam("statutId") Long statutId,
                                     @RequestParam("dateChangement") String dateChangement,
                                     @RequestParam("observation") String observation) {
        demandeStatutService.createDemandeStatus(demandeId, statutId, dateChangement, observation);
        return "redirect:/demande-statut/create";
    }
}
