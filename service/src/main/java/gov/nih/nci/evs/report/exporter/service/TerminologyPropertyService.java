package gov.nih.nci.evs.report.exporter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;

@Service
public class TerminologyPropertyService {
	
	@Value("${REST_PROP_URL}")
	String RestPropertyURL;

	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		builder.additionalMessageConverters(messageConverters);
		return builder.build();
	}
	
	public List<Property> getRestProperties(RestTemplate template){
		List<Property> propMeta = 
				(List<Property>) getRestTemplate(new RestTemplateBuilder())
		.getForObject(
		"https://api-evsrest-qa.nci.nih.gov/api/v1/metadata/ncit/properties?include=minimal"
				,List.class);;
		return propMeta;
	}
}
