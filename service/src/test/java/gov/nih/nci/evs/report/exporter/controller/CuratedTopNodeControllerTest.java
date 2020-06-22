package gov.nih.nci.evs.report.exporter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CuratedTopNodeControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void greetingShouldReturnContent() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/curated-top-nodes",
				String.class)).contains("Anatomic");
	}
	
	@Test
	public void greetingShouldReturnFirstContent() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/curated-top-nodes",
				String.class)).contains("C1909");
	}
	
	@Test
	public void greetingShouldReturnLastContent() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/curated-top-nodes",
				String.class)).contains("Carcinoma");
	}
}
