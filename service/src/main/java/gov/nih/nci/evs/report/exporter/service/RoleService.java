package gov.nih.nci.evs.report.exporter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.Role;

@Service
public class RoleService {
	
	@Autowired
	EVSAPIBaseService service;
	
	public List<Role> getRolesForEntityCode(String code){
		return service.getRestRole(code);
	}

}
