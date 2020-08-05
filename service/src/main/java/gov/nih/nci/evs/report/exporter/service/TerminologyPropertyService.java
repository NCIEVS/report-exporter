package gov.nih.nci.evs.report.exporter.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;

@Service
public class TerminologyPropertyService {
	
	@Value("${REST_PROP_URL}")
	private String propURL;
	
	@Value("${REST_PROP_FILTER_LIST}")
	private String filterList;


	
	public RestPropertyMetadata[] getRestProperties(RestTemplate template){
		RestPropertyMetadata[] propMeta = 
				getFilteredList(template
		.getForObject(
		 propURL
				,RestPropertyMetadata[].class));
		return propMeta;
	}
	
	public RestPropertyMetadata[] getFilteredList(RestPropertyMetadata[] propMeta) {
		 
				List<Object> obs = Stream.of(propMeta)
				.filter(
						x -> Stream.of(filterList.split(","))
						.noneMatch(
								y -> y.equals(
										x.getName()))).collect(Collectors.toList());
				
				return obs.toArray(new RestPropertyMetadata[obs.size()]);
	}

}
