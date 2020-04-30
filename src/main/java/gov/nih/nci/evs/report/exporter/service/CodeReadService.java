package gov.nih.nci.evs.report.exporter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.RestEntity;

@Service
public class CodeReadService {
	
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		builder.additionalMessageConverters(messageConverters);
		return builder.build();
	}
	
	public List<RestEntity> getRestProperties(RestTemplate template, List<String> codes){
		List<RestEntity> propMeta = 
				codes.stream().map(code -> getRestTemplate(new RestTemplateBuilder())
				.getForObject(
				"https://api-evsrest-dev.nci.nih.gov/api/v1/concept/ncit/" + code + "?include=summary"
						, RestEntity.class)).collect(Collectors.toList());
		Stream.of(propMeta).forEach(x->System.out.println(x.toString()));
		return propMeta;
	}

}
