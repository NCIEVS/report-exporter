package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

class RestEntityServiceTest {

	@Test
	void test() {
		CodeReadService service = new CodeReadService();
		List<String> codes = new ArrayList<String>();
		codes.add("C12434");
		List<RestEntity> list = service.getRestProperties(
				service.getRestTemplate(new RestTemplateBuilder()), codes);
		list.forEach(x -> System.out.println(x));
		assertTrue(list.size() > 0);
	}

}
