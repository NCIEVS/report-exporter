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
import org.springframework.web.bind.annotation.PostMapping;

import gov.nih.nci.evs.report.exporter.model.Code;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

@Controller
public class CodeReadController {
	
	@Autowired
	CodeReadService service;
	

	  @GetMapping("/coderead")
	  public String codeReadForm(Model model) {
	    model.addAttribute("code", new Code());
	    return "coderead";
	  }
	
	@PostMapping("/coderead")
	public String getEntities(@ModelAttribute Code code, Model model){
		model.addAttribute("entities",service.getRestProperties(
				getCodes((String)(code.getId()))));
		return "result";
	}
	
	private List<String> getCodes(String codes){
		return Arrays.asList(codes.split(","));
	}

}
