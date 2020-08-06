package gov.nih.nci.evs.report.exporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ReportExplorerApplication extends SpringBootServletInitializer{
	
	private static final Logger log = LoggerFactory.getLogger(ReportExplorerApplication.class);
	
	   @Override
	    protected SpringApplicationBuilder configure(
	      SpringApplicationBuilder builder) {
	        return builder.sources(ReportExplorerApplication.class);
	    }

	public static void main(String[] args) {
		SpringApplication.run(ReportExplorerApplication.class, args);
	}

}
