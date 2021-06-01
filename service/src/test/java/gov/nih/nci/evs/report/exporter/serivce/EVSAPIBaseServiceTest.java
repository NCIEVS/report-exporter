package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nih.nci.evs.report.exporter.service.EVSAPIBaseService;

@RunWith( SpringRunner.class )
@SpringBootTest
public class EVSAPIBaseServiceTest {
	
	@Autowired
	EVSAPIBaseService service;
	
//	@Before
//	public void setup() {
//		service = new EVSAPIBaseService();
//	}
	
	
	@Test
	void testCurationFilterURIException(){

	    Exception exception = assertThrows(URISyntaxException.class, () -> {
	    	try {
				service.getEntity("C%123");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
	    });
//	    String expected = "404 NOT_FOUND";
//	    String actual = exception.getMessage();
//	    assertEquals(expected, actual);
	}

}
