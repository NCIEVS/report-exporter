package gov.nih.nci.evs.report.exporter.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.PropertyPrime;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.util.CommonServices.ResType;

@Service
public class CodeReadService {
	
	@Autowired
	EVSAPIBaseService service;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	AssociationService associationService;
	

	public static final String NOTFOUND = "Concept Code Not Found";
	public static final String RETIRED = "Concept Code Retired";
	public static final String VALID = "SUCCESS";
	
	public List<RestEntity> getRestEntitiesWithParents(List<String> codes, String props ){
		List<RestEntity> propMeta = 
				codes.stream().map(x -> 
					getRestEntityWithParent(
							x, service.getRestParents(x)))
				.collect(Collectors.toList());
		return propMeta;
	}
	
	public List<RestEntity> getRestEntities(List<String> codes){
		List<RestEntity> propMeta = 
				codes.stream().map(code -> getCuratedEntityForCode(code)).collect(Collectors.toList());
		return propMeta;
	}
	
	public List<RestEntity> getRestEntities(List<String> codes, ResType type){
		List<RestEntity> propMeta = 
				codes.stream().map(code -> 
				getCuratedEntityForCodeAndType(code, type))
				.collect(Collectors.toList());
		return propMeta;
	}
	
	public RestEntity getRestEntityWithParent(String code, List<Root> parents){
		RestEntity entity = null;
			entity = getCuratedEntityForCode(code);
			entity.setParents(parents);
		return entity;
	}
	
	public RestEntity[] getRestEntitiesWithParent(String codes){
		RestEntity[] entities = service.getEntities(codes);
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	public List<RestEntity> getEntitiesForPropertyNameFilter
	(List<RestEntity> list, List<String> propList){
		list.stream().filter(x -> !retiredConceptsFilter(x)).forEach(
				entity -> {
					entity.setProperties(
						(List<Property>)filterProperties(entity.getProperties(), propList));
					entity.setDefinitions(
						(List<Definition>)filterProperties(entity.getDefinitions(), propList));
					entity.setSynonyms(
						(List<Synonym>)filterProperties(entity.getSynonyms(), propList));
					entity.setMaps(
							(List<PropertyMap>)filterProperties(entity.getMaps(), propList));
				});
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public RestEntity getEntityForPropertyNameFilter(RestEntity entity, List<String> propList){
				
					entity.setProperties(
						(List<Property>)filterProperties(entity.getProperties(), propList));
					entity.setDefinitions(
						(List<Definition>)filterProperties(entity.getDefinitions(), propList));
					entity.setSynonyms(
						(List<Synonym>)filterProperties(entity.getSynonyms(), propList));
					entity.setMaps(
							(List<PropertyMap>)filterProperties(entity.getMaps(), propList));
				
		return entity;
	}
	
	public List<? extends PropertyPrime> filterProperties(List<? extends PropertyPrime> propList, List<String> list){
		if(propList == null) {return null;}
		if(propList.get(0) instanceof PropertyMap && !list.contains("Maps_To")){ 
		propList.clear();return propList;}
		List<String> tempList = new ArrayList<String>(list);
		if(propList.get(0) instanceof PropertyMap) 
		{tempList.add("Has Synonym"); tempList.add("Related To");}
		if(propList.get(0) instanceof Definition && !(list.contains("DEFINITION") || list.contains("ALT_DEFINITION"))){
			propList.clear();
			return propList; 
		}
		if(propList.get(0) instanceof Synonym && !(list.contains("FULL_SYN") || list.contains("Display_Name"))){
			propList.clear();
			return propList; 
		}
		
		return propList.stream()
				.filter(
				x -> tempList
					.stream()
					.anyMatch(y -> 
					x.getType() == null?
							true:
							x.getType().equals(y)))
					.collect(Collectors.toList());
	}
	
	public  boolean retiredConceptsFilter(RestEntity entity){
		return (entity
				.getProperties()
				.stream()
				.anyMatch(prop -> prop.getValue()
						.equals("Retired_Concept")));
	}
	
	public RestEntity getCuratedEntityForCode(String code){
		RestEntity entity = null;
		try {
			entity = service.getEntity(code);
			entity.setRoles(roleService.getRolesForEntityCode(code));
			entity.setAssociations(associationService.getAssociationsForCode(code));
		}
			catch (WebClientResponseException.NotFound | URISyntaxException nf) {
				entity = new RestEntity();
				entity.setName("");
				entity.setCode(code);
				entity.setQueryCode(-1);
				entity.setQueryStatus(NOTFOUND);
				return entity;
		}

		if(retiredConceptsFilter(entity)) {
			entity = new RestEntity();
			entity.setName("");
			entity.setCode(code);
			entity.setQueryCode(-1);
			entity.setQueryStatus(RETIRED);
			return entity;
		}
		
		entity.setQueryCode(0);
		entity.setQueryStatus(VALID);
		return entity;
	}
	
	public RestEntity getCuratedEntityForCodeAndType(String code, ResType type) {
		RestEntity entity = null;
		try {
			entity = service.getEntity(code);
			if(type == ResType.ROLE) {
			entity.setRoles(roleService.getRolesForEntityCode(code));
			}
			else if (type == ResType.ASSOC) {
			entity.setAssociations(associationService.getAssociationsForCode(code));
			}
		}
			catch (WebClientResponseException.NotFound | URISyntaxException wu) {
				entity = new RestEntity();
				entity.setName("");
				entity.setCode(code);
				entity.setQueryCode(-1);
				entity.setQueryStatus(NOTFOUND);
				return entity;
		}

		if(retiredConceptsFilter(entity)) {
			entity = new RestEntity();
			entity.setName("");
			entity.setCode(code);
			entity.setQueryCode(-1);
			entity.setQueryStatus(RETIRED);
			return entity;
		}
		
		entity.setQueryCode(0);
		entity.setQueryStatus(VALID);
		return entity;
	}
	
	public void setService(EVSAPIBaseService service) {
		this.service = service;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	public AssociationService getAssociationService() {
		return associationService;
	}

	public void setAssociationService(AssociationService associationService) {
		this.associationService = associationService;
		
	}

	public List<String> getResType() {
		return Stream.of(ResType.values()).map(resType -> resType.toString()).collect(Collectors.toList());
	}
}
