package gov.nih.nci.evs.report.exporter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("evs")
public class ProfileConfiguration {
	
	Logger log = LoggerFactory.getLogger(ProfileConfiguration.class);
	
    @Value("${evs.api.url.baseurl}")
	String baseurl;
	@Value("${evs.api.url.metadataurl}")
	String metadataurl;
	@Value("${evs.mail.list}")
	String maillist;

	@Profile("dev")
	@Bean
	public String getDevProfileConfiguration() {
		log.info("Base URL :" + baseurl);
		log.info("Meta URL :" + metadataurl);
		System.out.println("BASE URL: " + baseurl);
		return baseurl;
	}
	
	@Profile("qa")
	@Bean
	public String getQaProfileConfiguration() {
		log.info("Base URL :" + baseurl);
		log.info("Meta URL :" + metadataurl);
		System.out.println("BASE URL: " + baseurl);
		return baseurl;
	}
	
	@Profile("stage")
	@Bean
	public String getStageProfileConfiguration() {
		log.info("Base URL :" + baseurl);
		log.info("Meta URL :" + metadataurl);
		System.out.println("BASE URL: " + baseurl);
		return baseurl;
	}
	
	@Profile("prod")
	@Bean
	public String getProdProfileConfiguration() {
		log.info("Base URL :" + baseurl);
		log.info("Meta URL :" + metadataurl);
		System.out.println("BASE URL: " + baseurl);
		return baseurl;
	}

    
    

    
	
	
	

}
