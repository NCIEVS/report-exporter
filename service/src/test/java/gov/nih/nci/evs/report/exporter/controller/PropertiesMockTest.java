package gov.nih.nci.evs.report.exporter.controller;


import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;
import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;


@WebMvcTest(PropertyController.class)
@ActiveProfiles("test")
public class PropertiesMockTest {


	@Autowired
	private MockMvc mockMvc;
	

	@MockBean
	private TerminologyPropertyService service;

	@Test
	public void shouldReturnProp() throws Exception {

		RestPropertyMetadata property = new RestPropertyMetadata();
		property.setName("Semantic_Type");
		property.setCode("P234");
		property.setTerminology("ncit");
		property.setVersion("ver1");
		RestPropertyMetadata[] props = {property};
		
		Mockito.when(service.getRestProperties(
				Mockito.any())).thenReturn(props);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/properties").accept(
				MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:P234,name:Semantic_Type,terminology:ncit,version:ver1}]";

		
		JSONAssert.assertEquals(expected, 
				result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnProp2() throws Exception {

		
		RestPropertyMetadata property = new RestPropertyMetadata();
		property.setCode("P233");
		property.setName("Semantic_Type");

		RestPropertyMetadata prop2 = new RestPropertyMetadata();
		prop2.setCode("P445");
		prop2.setName("UMLS_CUI");
		RestPropertyMetadata[] props = {property, prop2};

		
		Mockito.when(service.getRestProperties(
				Mockito.any())).thenReturn(props);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/properties").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:P233,name:Semantic_Type},"
				+ "{code:P445,name:UMLS_CUI}]";

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	}