package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.RestRolesEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.service.RoleService;

@RestController
public class RoleController {
	
	@Autowired
	RoleService service;
	

	  @GetMapping("/roles/{codes}")
	  public List<RestRolesEntity> codeReadRoles(@PathVariable String codes) {
		  
			return service.getRestRoleEntitiesForRoleNode(codes);
	  }
	
}
