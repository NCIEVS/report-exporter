package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

class CodeReadUtilityMethodsTest {
	
	@Autowired
	CodeReadService service;
	

	@BeforeEach
	void setUp() throws Exception {
		service = new CodeReadService();
		}

	@Test
	void testFilter() {
		RestEntity entity = getEntity();
			assertTrue(service.retiredConceptsFilter(entity));
	}
	
	@Test
	void testCurationFilterException(){
		CodeReadService spyService = Mockito.spy(CodeReadService.class);
		doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
		.when(spyService).getEntity(Mockito.any(RestTemplate.class), 
				Mockito.anyString());
		
	    Exception exception = assertThrows(HttpClientErrorException.class, () -> {
	    	spyService.getCuratedEntityForCode("CNOTVALID");;
	    });
	    String expected = "404 NOT_FOUND";
	    String actual = exception.getMessage();
	    assertEquals(expected, actual);
	}
	
	@Test
	void testCurationFilterRetired(){
		CodeReadService spyService = Mockito.spy(CodeReadService.class);
		doReturn(getEntity())
		.when(spyService).getEntity(Mockito.any(RestTemplate.class), 
				Mockito.anyString());
		
	    RestEntity retiredErrorEnt = spyService.getCuratedEntityForCode("CMUSTBERETIRED");
	    
	    assertEquals(retiredErrorEnt.getCode(),"CMUSTBERETIRED");
	    assertEquals(retiredErrorEnt.getName(),"");
	    assertEquals(retiredErrorEnt.getQueryCode(),-1);
	    assertEquals(retiredErrorEnt.getQueryStatus(),"Concept Code Retired");
	}

	private RestEntity getEntity() {
		RestEntity ent = new RestEntity();
		ent.setCode("C123234");
		ent.setName("Myent");
		ent.setTerminology("ncit");
		
		List<Property> props = new ArrayList<Property>();
		Property prop = new Property();
		prop.setType("PropType");
		prop.setValue("propvalue");
		Property propa = new Property();
		propa.setType("PropType");
		propa.setValue("propvalue1");
		Property prop2 = new Property();
		prop2.setType("ConceptStatus");
		prop2.setValue("Retired_Concept");
		props.add(prop);
		props.add(propa);
		props.add(prop2);
		ent.setProperties(props);
		
		return ent;
	}

}
