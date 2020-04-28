package gov.nih.nci.evs.report.exporter.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;

@Controller
public class PropertyController {
	
	@Autowired
	TerminologyPropertyService service;

	@GetMapping("/properties")
	public String getPropertyMeta(Model model){
		model.addAttribute("properties",Arrays.asList(service.getRestProperties(service.getRestTemplate(new RestTemplateBuilder()))));
		return "properties";
	}
	
}
