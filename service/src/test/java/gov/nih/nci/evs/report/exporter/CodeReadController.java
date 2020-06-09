package gov.nih.nci.evs.report.exporter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gov.nih.nci.evs.report.exporter.controller.CodeReadRestController;

@SpringBootTest
class CodeReadControllerTest {
	@Autowired
	CodeReadRestController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
