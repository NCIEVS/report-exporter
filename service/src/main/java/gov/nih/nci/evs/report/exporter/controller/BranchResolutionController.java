package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
public class BranchResolutionController {

	@Autowired
	BranchResolutionService service;
	

	  @GetMapping("/resolve-branch-for-codes/{ids}")
	  public List<ChildEntity> codeResolveFrom(@PathVariable String ids) {
			return service.getEnitiesForBranchTopNode(
					CommonServices.splitInput(ids));
	  }
}
