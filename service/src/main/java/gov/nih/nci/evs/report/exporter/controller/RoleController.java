package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import gov.nih.nci.evs.report.exporter.model.Code;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.RoleService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Controller
public class RoleController {
	
	@Autowired
	RoleService service;
	

	  @GetMapping("/roles/{code}")
	  public List<Role> codeReadForm(@PathVariable String code) {
			return service.getRolesForEntityCode(
					code);
	  }
	
	

}
