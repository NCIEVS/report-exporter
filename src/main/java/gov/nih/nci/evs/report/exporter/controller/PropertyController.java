package gov.nih.nci.evs.report.exporter.controller;

import java.util.Arrays;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;

@Controller
public class PropertyController {

	@GetMapping("/properties")
	public String getPropertyMeta(Model model){
		TerminologyPropertyService service = new TerminologyPropertyService();
		model.addAttribute("properties",Arrays.asList(service.getRestProperties(service.getRestTemplate(new RestTemplateBuilder()))));
		return "properties";
}
}
