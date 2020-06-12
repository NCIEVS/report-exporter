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
	

	  @GetMapping("/resolve-children-for-codes/{ids}")
	  public List<ChildEntity> resolveChildrenFromCodes(@PathVariable String ids) {
			return service.getChildrenForBranchTopNode(
					CommonServices.splitInput(ids));
	  }
	  
	  @GetMapping("/resolve-branch-for-codes/{ids}/{maximum}")
	  public List<ChildEntity> resolveTreeFromCodes(@PathVariable String ids, @PathVariable String maximum ) {
			return service.getAllChildrenForBranchTopNode(
					CommonServices.splitInput(ids), maximum);
	  }
}
