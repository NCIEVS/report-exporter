package gov.nih.nci.evs.report.exporter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.RestRolesEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class RoleService {
	
	@Autowired
	EVSAPIBaseService service;
	
	public List<Role> getRolesForEntityCode(String code){
		return service.getRestRole(code);
	}
	
	public RestEntity getRestEntityForRoleNode(String code) {
		return service.getEntity(code);
	}
	
	public RestRolesEntity getRestRoleEntityForRoleNode(String code) {

		RestRolesEntity entity = new RestRolesEntity();
		
		RestEntity rEntity = service.getEntity(code);
		entity.setCode(rEntity.getCode());
		entity.setName(rEntity.getName());
		entity.setRoles(service.getRestRole(code));
		
		return entity;
	}
	
	public List<RestRolesEntity> getRestRoleEntitiesForRoleNode(String codes) {
        ;
		return CommonServices.splitInput(codes)
				.stream()
				.map(code -> getRestRoleEntityForRoleNode(code))
				.collect(Collectors.toList());
		
	}

}
