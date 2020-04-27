package gov.nih.nci.evs.report.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;

@SpringBootApplication
public class ReportExplorerApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ReportExplorerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReportExplorerApplication.class, args);
	}
	
//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//		messageConverters.add(converter);;
//		builder.additionalMessageConverters(messageConverters);
//		return builder.build();
//	}
//
//	@Bean
//	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//		return args -> {
//			RestPropertyMetadata[] propMeta = (RestPropertyMetadata[]) restTemplate.getForObject(
//					"https://api-evsrest-qa.nci.nih.gov/api/v1/metadata/ncit/properties?include=minimal", RestPropertyMetadata[].class);
//			Stream.of(propMeta).forEach(x->System.out.println(x.toString()));
//		};
//	}

}
