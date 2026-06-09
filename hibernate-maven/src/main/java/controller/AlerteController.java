package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import service.AlerteService;

@Controller
@RequestMapping("/alertes")
public class AlerteController {
    private final AlerteService alerteService;

    public AlerteController(AlerteService alerteService) {
        this.alerteService = alerteService;
    }

    @GetMapping({"", "/"})
    public ModelAndView listAlertes() {
        ModelAndView mav = new ModelAndView("alertes/list");
        mav.addObject("demandesAlertes", alerteService.getDemandesAvecAlertes());
        mav.addObject("parametresAlertes", alerteService.getParametres());
        return mav;
    }
}
