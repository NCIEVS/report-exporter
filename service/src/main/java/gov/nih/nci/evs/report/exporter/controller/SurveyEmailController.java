package gov.nih.nci.evs.report.exporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.Survey;
import gov.nih.nci.evs.report.exporter.service.SurveyEmailService;

@RestController
public class SurveyEmailController {
	
	@Autowired
	SurveyEmailService service;

	  @GetMapping("/survey")
	  public String surveyForm(Model model) {
	    model.addAttribute("survey", new Survey());
	    return "survey";
	  }
	
	@PostMapping("/survey")
	  public String surveySubmit(@ModelAttribute Survey survey, Model model) {
	    model.addAttribute("survey", survey);
	    service.sendSurveyEmail(survey);
	    return "result";
	  }
}
