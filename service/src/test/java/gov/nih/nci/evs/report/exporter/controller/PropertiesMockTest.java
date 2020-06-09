package gov.nih.nci.evs.report.exporter.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import gov.nih.nci.evs.report.exporter.controller.CodeReadRestController;
import gov.nih.nci.evs.report.exporter.controller.PropertyController;
import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;


@WebMvcTest(PropertyController.class)
@ActiveProfiles("test")
public class PropertiesMockTest {


	@Autowired
	private MockMvc mockMvc;
	

	@MockBean
	private TerminologyPropertyService service;

	




	
	@Test
	public void shouldReturnProp() throws Exception {

		List<Property> props = new ArrayList<Property>();
		Property property = new Property();
		property.setType("Semantic_Type");
		property.setValue("Class");
		props.add(property);
		
		Mockito.when(service.getRestProperties(
				service.getRestTemplate(
						new RestTemplateBuilder()))).thenReturn(props);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/properties").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{type:Semantic_Type,value:Class}]";
				//+ "{type:UMLS_CUI,value:C11111}]}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnProp2() throws Exception {

		List<Property> props = new ArrayList<Property>();
		Property property = new Property();
		property.setType("Semantic_Type");
		property.setValue("Class");
		props.add(property);
		Property prop2 = new Property();
		prop2.setType("UMLS_CUI");
		prop2.setValue("C11111");
		props.add(prop2);

		
		Mockito.when(service.getRestProperties(
				service.getRestTemplate(
						new RestTemplateBuilder()))).thenReturn(props);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/properties").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{type:Semantic_Type,value:Class},"
				+ "{type:UMLS_CUI,value:C11111}]}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	}