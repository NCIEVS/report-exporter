package gov.nih.nci.evs.report.exporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
public class ReportExplorerApplication extends SpringBootServletInitializer{
	
	private static final Logger log = LoggerFactory.getLogger(ReportExplorerApplication.class);
	
	@Value("${PROD_CO_URL}")
	private String prod_co_url;
	@Value("${DEV_CO_URL}")
	private String dev_co_url;
	@Value("${CLIENT_CO_URL}")
	private String client_co_url;
	@Value("${UI_CLIENT_CO_URL}")
	private String ui_client_co_url;
	@Value("${QA_CO_URL}")
	private String qa_co_url;
	@Value("${STAGE_CO_URL}")
	private String stage_co_url;

	
	   @Override
	    protected SpringApplicationBuilder configure(
	      SpringApplicationBuilder builder) {
	        return builder.sources(ReportExplorerApplication.class);
	    }
	   
		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
				   	System.out.println("Running EVSREPEXP-220 codebase");
					registry.addMapping("/properties")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/resolve-children-for-codes/**")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/resolve-flat-branch-for-codes/**")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/resolve-branch-for-codes/**")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/codereadrest/**")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/codereadrestprops/**")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/download/**")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/curated-top-nodes")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
					registry.addMapping("/roots/**")
						.allowedOrigins(prod_co_url,dev_co_url,client_co_url,qa_co_url,stage_co_url,ui_client_co_url);
				}
			};
		}

	public static void main(String[] args) {
		SpringApplication.run(ReportExplorerApplication.class, args);
	}

}
