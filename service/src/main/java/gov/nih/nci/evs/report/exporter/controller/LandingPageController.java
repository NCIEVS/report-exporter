package gov.nih.nci.evs.report.exporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import gov.nih.nci.evs.report.exporter.service.LandingPageService;

@Controller
public class LandingPageController {
	
	@Autowired
	LandingPageService service;

//	@GetMapping("/")
//	public String getPropertyMeta(Model model){
//		model.addAttribute("landingpage", "Landing Page");
//		return "index";
//	}
}
