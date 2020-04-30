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
	
//	@GetMapping("/coderead")
//	public String getPropertyMeta(Model model){
//		model.addAttribute("title", "Code Read Service");
//		return "coderead";
//	}
	

	  @GetMapping("/coderead")
	  public String codeReadForm(Model model) {
	    model.addAttribute("coderead", new Code());
	    return "coderead";
	  }
	
	@PostMapping("/coderead")
	public String getEntities( @ModelAttribute Code code, BindingResult bzult, Model model){
		if(bzult.hasErrors()) {
			System.out.println( "Errors were made");
		}
		//model.addAttribute("codes", codes);
		model.addAttribute("entities",service.getRestProperties( 
				service.getRestTemplate(new RestTemplateBuilder()),
				getCodes((String) code.getEntityCode())));
		((List<RestEntity>)model.getAttribute("entities")).forEach(x -> System.out.println("Model: " + x));
		return "coderead";
	}
	
	private List<String> getCodes(String codes){
		return Arrays.asList(codes.split(","));
	}

}
