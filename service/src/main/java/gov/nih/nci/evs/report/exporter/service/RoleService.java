package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.Rel;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.util.CommonServices;
import gov.nih.nci.evs.report.exporter.util.DelimitedRoleOutputUtility;
import gov.nih.nci.evs.report.exporter.util.ExcelUtility;
import gov.nih.nci.evs.report.exporter.util.JSONUtility;

@Service
public class RoleService {
	
	@Autowired
	EVSAPIBaseService service;
	
	@Autowired
    CodeReadService crservice;
	
	DelimitedRoleOutputUtility utility;
	
	@Autowired
	public RoleService(CodeReadService crservice) {
		this.crservice = crservice;
	}
	
	public List<Role> getRolesForEntityCode(String code){
		return service.getRestRole(code);
	}
	
	public List<Rel> getDistinctWeightedRelsForEntityCodes(List<? extends Rel> rawRels){
		Hashtable<String,Rel> distinctRels = new Hashtable<String,Rel>();	
		rawRels.stream().forEach(x -> CommonServices.saveOrUpdateWeightedRels(x, distinctRels));
		return distinctRels.values().stream().collect(Collectors.toList());
	}
	
	
	public RestEntity getRestEntityForRoleNode(String code) {
		return crservice.getCuratedEntityForCode(code);
	}
	
	public RestEntity getRestRoleEntityForRoleNode(String code) {

		RestEntity entity = new RestEntity();
		
		RestEntity rEntity = crservice.getCuratedEntityForCode(code);
		if(rEntity == null) {throw new RuntimeException("Code has no roles or does not exist");}
		entity.setCode(rEntity.getCode());
		entity.setName(rEntity.getName());
		entity.setRoles(service.getRestRole(code));
		
		return entity;
	}
	
	public List<RestEntity> getRestRoleEntitiesForRoleNode(String codes) {
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
	
	public List<? extends Rel> getSortedRoles(String codes){
		return CommonServices.getSortedRels(getRolesForCodes(codes));
	}
	

	public InputStream getChildCSVBytesForRestRoleParams(String codes, String roles) {
		List<RestEntity> entities =  filterEntitiesAndRolesForRoles(roles,getRestRoleEntitiesForRoleNode(codes));
		return new ByteArrayInputStream( 
				new DelimitedRoleOutputUtility().produceDelimitedOutputFromListWithHeading(entities, roles, codes, ",").getBytes());
	}
	
	public InputStream getChildTabDelBytesForRestRoleParams(String codes, String roles) {
		List<RestEntity> entities =  filterEntitiesAndRolesForRoles(roles,getRestRoleEntitiesForRoleNode(codes));
		return new ByteArrayInputStream( 
				new DelimitedRoleOutputUtility().produceDelimitedOutputFromListWithHeading(entities, roles, codes, "\t").getBytes());
	}

	private List<RestEntity> filterEntitiesAndRolesForRoles(String roles, List<RestEntity> entities) {
		return entities.stream()
				.filter(x -> entityContainRoleFilter(roles, x))
				.collect(Collectors.toList());
	}
	
	private boolean entityContainRoleFilter(String roles, RestEntity entity) {
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

	public InputStream getJsonBytesForRestRoleParams(String codes, String roles) {
		return new ByteArrayInputStream(new JSONUtility().produceJsonRoleOutputFromListWithHeading(
				filterEntitiesAndRolesForRoles(roles,getRestRoleEntitiesForRoleNode(codes)), roles, codes).getBytes());
	}

	public ByteArrayInputStream getChildExcelBytesForRestRoleParams(String codes, String roles) throws IOException {
		return new ByteArrayInputStream(new ExcelUtility().produceExcelRoleOutputFromListWithHeading(
				filterEntitiesAndRolesForRoles(roles,getRestRoleEntitiesForRoleNode(codes)), roles, codes).toByteArray());
	}

}
