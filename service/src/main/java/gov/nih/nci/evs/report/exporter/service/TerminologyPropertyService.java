package gov.nih.nci.evs.report.exporter.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;

@Service
public class TerminologyPropertyService {

	
	@Autowired
	EVSAPIBaseService service;


	
	public void setService(EVSAPIBaseService service) {
		this.service = service;
	}

	public RestPropertyMetadata[] getRestProperties(RestTemplate template){
		RestPropertyMetadata[] propMeta = 
				getFilteredList(service.getRestProperties(template));
		RestPropertyMetadata[] synMeta = 
				getFilteredList(service.getRestSynonyms(template));
		RestPropertyMetadata[] defMeta = 
				getFilteredList(service.getRestDefinitions(template));
		
		return Stream
				.of(propMeta,synMeta,defMeta)
				.flatMap(Stream::of)
				.toArray(RestPropertyMetadata[]::new);
	}
	
	public RestPropertyMetadata[] getFilteredList(RestPropertyMetadata[] propMeta) {
		 
				List<Object> obs = Stream.of(propMeta)
				.filter(
						x -> Stream.of(
						service.getFilterList().split(","))
						.noneMatch(
								y -> y.equals(
										x.getName()))).collect(Collectors.toList());
				
				return obs.toArray(new RestPropertyMetadata[obs.size()]);
	}

}
