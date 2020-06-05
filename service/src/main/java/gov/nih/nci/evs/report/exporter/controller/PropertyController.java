package gov.nih.nci.evs.report.exporter.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.EntityProperties;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class PropertyController {
	
	@Autowired
	TerminologyPropertyService service;

	@GetMapping("/properties")
	public List<Property> getPropertyMeta(Model model){
		return service.getRestProperties(service.getRestTemplate(new RestTemplateBuilder()));
	}
	
	@PostMapping("/properties")
    public String save(@ModelAttribute EntityProperties selected, Model model) {
        return "saved";
    }
	
}
