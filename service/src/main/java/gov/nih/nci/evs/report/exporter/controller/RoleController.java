package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.Rel;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.service.RoleService;

@RestController
public class RoleController {
	
	@Autowired
	RoleService service;
	
	  @GetMapping("/role/{code}")
	  public RestEntity codeReadRole(@PathVariable String code) {
			return service.getRestRoleEntityForRoleNode(code);
	  }
	

	  @GetMapping("/roles/{codes}")
	  public List<RestEntity> codeReadRoles(@PathVariable String codes) {
			return service.getRestRoleEntitiesForRoleNode(codes);
	  }
	  
	  @GetMapping("/sortedroles/{codes}")
	  public List<? extends Rel> getSortedRolesForCodes(@PathVariable String codes) {
			return service.getSortedRoles(codes);
	  }
	
}
