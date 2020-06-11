package gov.nih.nci.evs.report.exporter.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class TerminologyPropertyService {
	
	@Value("${REST_PROP_URL}")
	String RestPropertyURL;


	
	public RestPropertyMetadata[] getRestProperties(RestTemplate template){
		@SuppressWarnings("unchecked")
		RestPropertyMetadata[] propMeta = 
				 template
		.getForObject(
		"https://api-evsrest-qa.nci.nih.gov/api/v1/metadata/ncit/properties?include=minimal"
				,RestPropertyMetadata[].class);
		return propMeta;
	}
	
	public static void main(String ...args) {
		RestPropertyMetadata[] list = 		 
				(RestPropertyMetadata[]) CommonServices.getRestTemplate()
		.getForObject(
		"https://api-evsrest-qa.nci.nih.gov/api/v1/metadata/ncit/properties?include=minimal"
				,RestPropertyMetadata[].class);
		Stream.of(list).forEach(x -> System.out.println(x.getName()));
	}
}
