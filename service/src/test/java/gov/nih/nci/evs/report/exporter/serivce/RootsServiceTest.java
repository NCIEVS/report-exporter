package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.service.EVSAPIBaseService;
import gov.nih.nci.evs.report.exporter.service.RestRootNodeService;

@RunWith(SpringRunner.class)
class RootsServiceTest {


	@InjectMocks
	RestRootNodeService rootService;

	@Mock
	EVSAPIBaseService service;


	@BeforeEach
	void setUp() throws Exception {
	 rootService = new RestRootNodeService();
	 service = Mockito.mock(EVSAPIBaseService.class);
	 rootService.setService(service);
	}

	@Test
	void testService() {
		when(service.getRestRoots(Mockito.any(RestTemplate.class))).thenReturn(getJsonRoots());
		when(service.getRootFilterList()).thenReturn("C28428");
		assertFalse(Stream.of(rootService.getRestRoots(new RestTemplate())).anyMatch(x -> x.getCode().equals("C28428")));
		
	}

	private Root[] getJsonRoots() {
		Root root = new Root();
		root.setCode("C12913");
		root.setName("Abnormal Cell");
		Root root1 = new Root();
		root1.setCode("C43431");
		root1.setName("Activity");
		Root root2 = new Root();
		root2.setCode("C20633");
		root2.setName("Biochemical Pathway");
		Root root3 = new Root();
		root3.setCode("C28428");
		root3.setName("Retired Concept");
		
		return new Root[] {root, root1, root2, root3};
	}

}
