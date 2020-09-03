package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
public class CodeReadRestController {
	
	@Autowired
	CodeReadService service;
	

	  @GetMapping("/codereadrest/{ids}")
	  public List<RestEntity> codeReadForm(@PathVariable String ids) {
			return service.getRestEntities(
					CommonServices.splitInput(ids))
					.stream()
					.filter(entity -> 
						service.retiredConceptsFilter(entity))
//					.map(x -> {if(x != null) {if(x.getCode() == null) {return null;}}else{return x;}})
					.collect(Collectors.toList());
	  }
	
	@PostMapping("/codereadrest/{ids}")
	public List<RestEntity> getEntities(@PathVariable String ids){
		return service.getRestEntities(
				CommonServices.splitInput(ids));
	}
	
	@GetMapping("/codereadrestprops/{ids}/{list}")
	public List<RestEntity> getEntitiesWithParameters(@PathVariable String ids,
			@PathVariable String list){
		return service.getEntitiesForPropertyNameFilter(service.getRestEntities( 
				CommonServices.splitInput(ids)), CommonServices.splitInput(list));
	}
	
	

}
