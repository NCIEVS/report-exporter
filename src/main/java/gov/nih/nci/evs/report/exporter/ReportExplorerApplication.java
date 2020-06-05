package gov.nih.nci.evs.report.exporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportExplorerApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ReportExplorerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReportExplorerApplication.class, args);
	}

}
