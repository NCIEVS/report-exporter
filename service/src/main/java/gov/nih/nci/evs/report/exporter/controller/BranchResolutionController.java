package gov.nih.nci.evs.report.exporter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class BranchResolutionController {

	@Autowired
	BranchResolutionService service;
	
	@Autowired
	CodeReadService readService;
	

	  @GetMapping("/resolve-children-for-codes/{ids}")
	  public List<ChildEntity> resolveChildrenFromCodes(@PathVariable String ids) {
			return service.getChildrenForBranchTopNode(
					CommonServices.splitInput(ids));
	  }
	  
	  @GetMapping("/resolve-flat-branch-for-codes/{ids}/{maximum}")
	  public List<ChildEntity> resolveTreeToListFromCodes(@PathVariable String ids, @PathVariable String maximum ) {
			return service.getAllChildrenForBranchTopNode(
					CommonServices.splitInput(ids), maximum);
	  }
	  
	  @GetMapping("/resolve-branch-for-codes/{ids}/{maximum}")
	  public List<ChildEntity> resolveTreeFromCodes(@PathVariable String ids, @PathVariable String maximum ) {
			return service.getUnprocessedChildrenForBranchTopNode(
					CommonServices.splitInput(ids), maximum);
	  }
	  
	  @GetMapping("/resolve-flat-branch-for-codes-full/{ids}/{props}/{maximum}")
	  public List<RestEntity> resolveFullEntityListFromCodes(
			  @PathVariable String ids,
			  @PathVariable String props, 
			  @PathVariable String maximum )
	  {
		 return service.getAllChildrenForBranchTopNode(CommonServices.splitInput(ids), maximum)
		  .stream()
		  .map(x -> readService.getEntitiesForPropertyNameFilter(
				  readService.getRestEntities(
						  CommonServices.getCodesListForCode(x.getCode())), 
						  CommonServices.splitInput(props))).flatMap(List::stream)
		  .collect(Collectors.toList());		  
	  }
	  	  
}
