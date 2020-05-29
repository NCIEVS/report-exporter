package gov.nih.nci.evs.report.exporter.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import gov.nih.nci.evs.report.exporter.model.EntityProperties;
import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;

@Controller
public class PropertyController {
	
	@Autowired
	TerminologyPropertyService service;

	@GetMapping("/properties")
	public String getPropertyMeta(Model model){
		model.addAttribute("properties", Arrays.asList(service.getRestProperties(service.getRestTemplate(new RestTemplateBuilder()))));
		 model.addAttribute("selected", new EntityProperties());
		return "properties";
	}
	
	@PostMapping("/properties")
    public String save(@ModelAttribute EntityProperties selected, Model model) {
        return "saved";
    }
	
}
