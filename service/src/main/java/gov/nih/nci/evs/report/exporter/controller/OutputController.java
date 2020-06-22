package gov.nih.nci.evs.report.exporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import gov.nih.nci.evs.report.exporter.model.EntityProperties;
import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;

@Controller
public class OutputController {
	
	@Autowired
	TerminologyPropertyService service;

	@GetMapping("/output")
	public String getPropertyMeta(Model model){
		return "output";
	}
	
	@PostMapping("/output")
    public String save(@ModelAttribute EntityProperties selected, Model model) {
        return "output";
    }
	
}
