package gov.nih.nci.evs.report.exporter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;

@Service
public class TerminologyPropertyService {
	
	@Value("${REST_PROP_URL}")
	private String propURL;


	
	public RestPropertyMetadata[] getRestProperties(RestTemplate template){
		RestPropertyMetadata[] propMeta = 
				 template
		.getForObject(
		 propURL
				,RestPropertyMetadata[].class);
		return propMeta;
	}

}
