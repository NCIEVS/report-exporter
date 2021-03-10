package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
public class BranchResolutionController {

	@Autowired
	BranchResolutionService service;
	
	@Autowired
	CodeReadService readService;
	

	  @GetMapping("/resolve-children-for-codes/{code}")
	  public List<ChildEntity> resolveChildrenFromCodes(@PathVariable String code) {
			return service.getChildrenForBranchTopNode(
					CommonServices.splitInput(code));
	  }
	  
	  @GetMapping("/resolve-flat-branch-for-codes/{code}/{maximum}")
	  public List<ChildEntity> resolveTreeToListFromCodes(@PathVariable String code, @PathVariable String maximum ) {
			return service.getAllChildrenForBranchTopNode(
					code, maximum);
	  }
	  
	  @GetMapping("/resolve-branch-for-codes/{code}/{maximum}")
	  public List<ChildEntity> resolveTreeFromCodes(@PathVariable String code, @PathVariable String maximum ) {
			return service.getUnprocessedChildrenForBranchTopNode(
					code, maximum);
	  }
	  
	  @GetMapping("/resolve-flat-branch-for-codes-full/{code}/{props}/{maximum}")
	  public List<RestEntity> resolveFullEntityListFromCodes(
			  @PathVariable String code,
			  @PathVariable String props, 
			  @PathVariable String maximum )
	  {
		 return service.getAllChildrenForBranchTopNode(code, maximum)
		  .stream()
		  .map(x -> readService.getEntitiesForPropertyNameFilter(
				  readService.getRestEntities(
						  CommonServices.getCodesListForCode(x.getCode())), 
						  CommonServices.splitInput(props))).flatMap(List::stream)
		  .collect(Collectors.toList());		  
	  }
	  	  
}
