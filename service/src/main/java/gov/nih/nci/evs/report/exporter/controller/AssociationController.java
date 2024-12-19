package gov.nih.nci.evs.report.exporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.model.Rel;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.service.AssociationService;
import gov.nih.nci.evs.report.exporter.service.RoleService;

@RestController
public class AssociationController {
	
	@Autowired
	AssociationService service;
	
	@Autowired
	public AssociationController(@Lazy AssociationService service) {
		this.service = service;
	}
	
	  @GetMapping("/association/{code}")
	  public RestEntity codeReadRole(@PathVariable String code) {
			return service.getRestAssociationEntityForRoleNode(code);
	  }
	

	  @GetMapping("/associations/{codes}")
	  public List<RestEntity> codeReadRoles(@PathVariable String codes) {
			return service.getRestRelEntitiesForRelNode(codes);
	  }
	  
	  @GetMapping("/sortedassociations/{codes}")
	  public List<? extends Rel> getSortedRolesForCodes(@PathVariable String codes) {
			return service.getSortedAssociations(codes);
	  }
	
}
