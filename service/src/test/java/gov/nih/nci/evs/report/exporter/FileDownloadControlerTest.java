package gov.nih.nci.evs.report.exporter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gov.nih.nci.evs.report.exporter.controller.CodeReadRestController;
import gov.nih.nci.evs.report.exporter.controller.FileDownloadController;

@SpringBootTest
class FileDownloadControllerTest {
	@Autowired
	FileDownloadController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
