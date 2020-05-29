package gov.nih.nci.evs.report.exporter.controller;

import java.util.Arrays;
import java.util.List;

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

import gov.nih.nci.evs.report.exporter.model.Code;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

@RestController
public class CodeReadRestController {
	
	@Autowired
	CodeReadService service;
	

	  @GetMapping("/codereadrest/{id}")
	  public List<RestEntity> codeReadForm(@PathVariable String id) {
			return service.getRestProperties( 
					service.getRestTemplate(new RestTemplateBuilder()),
					getCodes(id));
	  }
	
	@PostMapping("/codereadrest")
	public List<RestEntity> getEntities(@ModelAttribute Code code, Model model){
		return service.getRestProperties( 
				service.getRestTemplate(new RestTemplateBuilder()),
				getCodes((String)(code.getId())));
	}
	
	private List<String> getCodes(String codes){
		return Arrays.asList(codes.split(","));
	}

}
