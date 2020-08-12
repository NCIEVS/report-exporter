package gov.nih.nci.evs.report.exporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.EntityProperties;
import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;
import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
public class PropertyController {
	
	@Autowired
	TerminologyPropertyService service;

	@GetMapping("/properties")
	public RestPropertyMetadata[] getPropertyMeta(Model model){
		return service.getRestProperties(CommonServices.getRestTemplate());
	}
	
	@PostMapping("/properties")
    public String save(@ModelAttribute EntityProperties selected, Model model) {
        return "saved";
    }
	
}
