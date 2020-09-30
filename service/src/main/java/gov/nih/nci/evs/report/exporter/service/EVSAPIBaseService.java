package gov.nih.nci.evs.report.exporter.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class EVSAPIBaseService {
	
    @Value("${NODE_LIST}")
	private String curatedTopNodeList;
    
    @Value("${BASE_URL}")
    private String baseURL;
    
    @Value("${CHILDREN}")
    private String children;
    
    @Value("${DESCENDANTS}")
    private String descendants;
    
	@Value("${SUMMARY}")
	private String summary;
	
	@Value("${MAPS}")
	private String maps;
	
	@Value("${PARENTS}")
	private String parents;
	
	@Value("${REST_PROP_URL}")
	private String propURL;
	
	@Value("${REST_PROP_FILTER_LIST}")
	private String filterList;
    
    
	public List<ChildEntity> getChildrenForBranchTopNode(List<String> codes){
		return 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				baseURL  + code + children
						,ChildEntity[].class)).flatMap(Arrays::stream)
						.collect(Collectors.toList());
	}
	
	public List<ChildEntity> getUnprocessedChildrenForBranchTopNode(String code, String max){
		return 
				Arrays.asList(CommonServices.getRestTemplate()
				.getForObject(
				baseURL 
				+ code 
				+ descendants + max
						,ChildEntity[].class));
	}
	
	public List<Root> getRestParents(String code){
		List<Root> roots = Stream.of(WebClient
				.create()
				.get()
				.uri(baseURL + code + parents)
				.retrieve().bodyToMono(Root[].class)
				.block()).collect(Collectors.toList());			
		return roots;
	}
	
	public RestEntity getEntity(String code) {	
		try {
			return WebClient
					.create()
					.get()
					.uri(new URI(baseURL + code + summary + "," + maps))
					.retrieve()
					.bodyToMono(RestEntity.class)
					.block();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Root[] getRestRoots(RestTemplate template){
		Root[] roots = 
				 template
		.getForObject(
		 baseURL + "/roots"
				,Root[].class);
		return roots;
	}
	
	public RestPropertyMetadata[] getRestProperties(RestTemplate template){
		return template
		.getForObject(
		 propURL
				,RestPropertyMetadata[].class);

	}

	public String getCuratedTopNodeList() {
		return curatedTopNodeList;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getChildren() {
		return children;
	}

	public String getDescendants() {
		return descendants;
	}

	public String getSummary() {
		return summary;
	}

	public String getMaps() {
		return maps;
	}

	public String getParents() {
		return parents;
	}

	public String getPropURL() {
		return propURL;
	}

	public String getFilterList() {
		return filterList;
	}
	
	
    

}