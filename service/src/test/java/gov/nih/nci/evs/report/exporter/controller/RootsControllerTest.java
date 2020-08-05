package gov.nih.nci.evs.report.exporter.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RootsControllerTest {
	@Autowired
	RootsController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
