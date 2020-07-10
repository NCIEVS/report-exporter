package gov.nih.nci.evs.report.exporter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.PropertyPrime;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class CodeReadService {
	
	@Value("${BASE_URL}")
	private String baseURL;
	
	@Value("${SUMMARY}")
	private String summary;
	
	@Value("${MAPS}")
	private String maps;
	
	public List<RestEntity> getRestEntities(List<String> codes){
		List<RestEntity> propMeta = 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				baseURL + code + summary + "," + maps
						, RestEntity.class)).collect(Collectors.toList());
		return propMeta;
	}
	
	public RestEntity getRestEntityWithParent(String code, String parent){
		RestEntity entity = CommonServices.getRestTemplate()
				.getForObject(
				baseURL + code + summary + "," + maps
						, RestEntity.class);
			entity.setParent(parent);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<RestEntity> getEntitiesForPropertyNameFilter
	(List<RestEntity> list, List<String> propList){
		list.stream().forEach(
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
		if(propList.get(0) instanceof PropertyMap && list.contains("Maps_To")) {return propList;}
		return propList.stream().filter(
				x -> list.stream().anyMatch(y -> x.getType() == null?true:x.getType().equals(y)))
				.collect(Collectors.toList());
	}
	
}
