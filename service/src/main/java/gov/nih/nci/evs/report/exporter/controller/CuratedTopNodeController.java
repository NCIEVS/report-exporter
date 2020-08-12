package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import gov.nih.nci.evs.report.exporter.model.CuratedTopNode;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;

@RestController
public class CuratedTopNodeController {
	
	@Autowired
	BranchResolutionService service;

@GetMapping("/curated-top-nodes")
public List<CuratedTopNode> getCuratedTopNodes(){
	return service.getCuratedTopNodeList();
}

}
