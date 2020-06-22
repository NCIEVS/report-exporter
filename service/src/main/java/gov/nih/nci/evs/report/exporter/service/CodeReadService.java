package gov.nih.nci.evs.report.exporter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
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
	
	public List<RestEntity> getRestEntities(List<String> codes){
		List<RestEntity> propMeta = 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				baseURL + code + summary
						, RestEntity.class)).collect(Collectors.toList());
		return propMeta;
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
				});
		return list;
	}
	
	public List<? extends PropertyPrime> filterProperties(List<? extends PropertyPrime> propList, List<String> list){
		return propList.stream().filter(
				x -> list.stream().anyMatch(y -> x.getType() == null?true:x.getType().equals(y)))
				.collect(Collectors.toList());
	}
	
}
