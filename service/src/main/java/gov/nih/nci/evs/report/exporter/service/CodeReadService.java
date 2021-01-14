package gov.nih.nci.evs.report.exporter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

@Service
public class CodeReadService {
	
	@Autowired
	EVSAPIBaseService service;
	

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
	
	public RestEntity getRestEntityWithParent(String code, List<Root> parents){
		RestEntity entity = service.getEntity(code);
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
		if(propList.get(0) instanceof Synonym && !list.contains("FULL_SYN")){
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
		}
			catch (WebClientResponseException.NotFound nf) {
				entity = new RestEntity();
				entity.setName("");
				entity.setCode(code);
				entity.setQueryCode(-1);
				entity.setQueryStatus(NOTFOUND);
				return entity;
		}
		if(entity == null) {
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
}
