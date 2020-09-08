package gov.nih.nci.evs.report.exporter.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.PropertyPrime;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.util.CommonServices;
import reactor.core.publisher.Flux;

@Service
public class CodeReadService {
	
	@Value("${BASE_URL}")
	private String baseURL;
	
	@Value("${SUMMARY}")
	private String summary;
	
	@Value("${MAPS}")
	private String maps;
	
	@Value("${PARENTS}")
	private String parents;
	
	public static final String NOTFOUND = "Concept Code Not Found";
	public static final String RETIRED = "Concept Code Retired";
	public static final String VALID = "SUCCESS";
	
	public List<RestEntity> getRestEntitiesWithParents(List<String> codes){
		//long start = System.currentTimeMillis();
		List<RestEntity> propMeta = 
				codes.stream().map(x -> 
					getRestEntityWithParent(
							x, getRestParents(x)))
				.collect(Collectors.toList());
//		System.out.println("milli seconds to resolve parents and entity: " 
//				+ (System.currentTimeMillis() - start));
		return propMeta;
	}
	
	public List<RestEntity> getRestEntities(List<String> codes){
		List<RestEntity> propMeta = 
				codes.stream().map(code -> getCuratedEntityForCode(code)).collect(Collectors.toList());
		return propMeta;
	}
	
	public List<Root> getRestParents(String code){
		List<Root> roots = Stream.of(WebClient
				.create()
				.get()
				.uri(baseURL + code + parents)
				.retrieve()
				.bodyToFlux(Root.class)
				.blockLast())
				.collect(Collectors.toList());			
		return roots;
	}
	
	public RestEntity getRestEntityWithParent(String code, List<Root> parents){
		RestEntity entity = getEntity(CommonServices.getRestTemplate(), code);
			entity.setParents(parents);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<RestEntity> getEntitiesForPropertyNameFilter
	(List<RestEntity> list, List<String> propList){
		list.stream().filter(x -> retiredConceptsFilter(x) ).forEach(
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
			entity = getEntity(CommonServices.getRestTemplate(), code);
		}
			catch (HttpClientErrorException.NotFound nf) {
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
	
	public RestEntity getEntity(RestTemplate template, String code) {	
		try {
			return WebClient
					.create()
					.get()
					.uri(new URI(baseURL + code + summary + "," + maps))
					.retrieve()
					.bodyToMono(RestEntity.class)
					.block();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMaps() {
		return maps;
	}

	public void setMaps(String maps) {
		this.maps = maps;
	}

	public String getParents() {
		return parents;
	}

	public void setParents(String parents) {
		this.parents = parents;
	}
	
}
