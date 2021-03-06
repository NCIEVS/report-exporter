package gov.nih.nci.evs.report.exporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.service.RestRootNodeService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
public class RootsController {
	
	@Autowired
	RestRootNodeService service;

	@GetMapping("/roots")
	public Root[] getPropertyMeta(Model model){
		return service.getRestRoots(CommonServices.getRestTemplate());
	}
	
}
