package gov.nih.nci.evs.report.exporter.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import gov.nih.nci.evs.report.exporter.model.Code;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class CodeReadRestController {
	
	@Autowired
	CodeReadService service;
	

	  @GetMapping("/codereadrest/{ids}")
	  public List<RestEntity> codeReadForm(@PathVariable String ids) {
			return service.getRestProperties(
					service.getCodes(ids));
	  }
	
	@PostMapping("/codereadrest/{ids}")
	public List<RestEntity> getEntities(@PathVariable String ids){
		return service.getRestProperties(
				service.getCodes(ids));
	}
	
	@GetMapping("/codereadrestprops/{ids}/{list}")
	public List<RestEntity> getEntitiesWithParameters(@PathVariable String ids,
			@PathVariable String list){
		return service.getEntitiesForPropertyNameFilter(service.getRestProperties( 
				service.getCodes(ids)), service.getCodes(list));
	}
	
	

}
