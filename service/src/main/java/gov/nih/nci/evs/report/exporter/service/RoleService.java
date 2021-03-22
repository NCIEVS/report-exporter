package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.RestRolesEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.model.WeightedRole;
import gov.nih.nci.evs.report.exporter.util.CSVRoleUtility;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class RoleService {
	
	@Autowired
	EVSAPIBaseService service;
	
	CSVRoleUtility utility;
	
	public List<Role> getRolesForEntityCode(String code){
		return service.getRestRole(code);
	}
	
	public List<WeightedRole> getDistinctWeightedRolesForEntityCodes(String codes){
		Hashtable<String,WeightedRole> distinctRoles = new Hashtable<String,WeightedRole>();	
		getRolesForCodes(codes).stream().forEach(x -> saveOrUpdateWeightedRoles(x, distinctRoles));
		return distinctRoles.values().stream().collect(Collectors.toList());
	}
	
	public void saveOrUpdateWeightedRoles(Role role, Hashtable<String, WeightedRole> wRoles) {
		WeightedRole rStored = wRoles.get(role.getType());
		if(rStored == null){wRoles.put(role.getType(), new WeightedRole(role,1));
		}else {rStored.setWeight(rStored.getWeight() + 1);
		}
	}
	
	
	public RestEntity getRestEntityForRoleNode(String code) {
		return service.getEntity(code);
	}
	
	public RestRolesEntity getRestRoleEntityForRoleNode(String code) {

		RestRolesEntity entity = new RestRolesEntity();
		
		RestEntity rEntity = service.getEntity(code);
		if(rEntity == null) {throw new RuntimeException("Code has no roles or does not exist");}
		entity.setCode(rEntity.getCode());
		entity.setName(rEntity.getName());
		entity.setRoles(service.getRestRole(code));
		
		return entity;
	}
	
	public List<RestRolesEntity> getRestRoleEntitiesForRoleNode(String codes) {
		return CommonServices.splitInput(codes)
				.stream()
				.map(code -> getRestRoleEntityForRoleNode(code))
				.collect(Collectors.toList());
		
	}

	public List<Role> getRolesForCodes(String codes) {
		return CommonServices.splitInput(codes)
				.stream()
				.map(code -> getRolesForEntityCode(code)).flatMap(List::stream).
				collect(Collectors.toList());
	}
	
	public List<Role> sortRoleListByWeight(List<WeightedRole> roles){
		Collections.sort(roles);
		return roles.stream().map(x -> x.getRole()).collect(Collectors.toList());
	}
	
	public List<Role> getSortedRoles(String codes){
		return sortRoleListByWeight(getDistinctWeightedRolesForEntityCodes(codes));
	}

	public InputStream getChildCSVBytesForRestRoleParams(String codes, String roles) {
		List<RestRolesEntity> entities =  filterEntitiesAndRolesForRoles(roles,getRestRoleEntitiesForRoleNode(codes));
		return new ByteArrayInputStream( 
				new CSVRoleUtility().produceCSVOutputFromListWithHeading(entities, roles, codes, ",").getBytes());
	}

	private List<RestRolesEntity> filterEntitiesAndRolesForRoles(String roles, List<RestRolesEntity> entities) {
		return entities.stream()
				.filter(x -> entityContainRoleFilter(roles, x))
				.collect(Collectors.toList());
	}
	
	private boolean entityContainRoleFilter(String roles, RestRolesEntity entity) {
		List<String> roleList = CommonServices.splitInput(roles);
		List<Role> roleModelList = entity
				.getRoles()
				.stream()
				.filter(x -> roleList.contains(x.getType()))
				.collect(Collectors.toList());
		if(roleModelList == null || roleModelList.size() == 0) {
		return false;
		}
		else {
			entity.setRoles(roleModelList);
			return true;
		}
	}

	public InputStream getJsonBytesForRestRoleParams(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getChildTabDelBytesForRestRoleParams(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
