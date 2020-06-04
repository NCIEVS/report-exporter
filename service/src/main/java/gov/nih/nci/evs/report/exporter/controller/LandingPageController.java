package gov.nih.nci.evs.report.exporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
