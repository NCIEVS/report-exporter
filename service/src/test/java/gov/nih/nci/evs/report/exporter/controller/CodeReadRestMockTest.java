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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import gov.nih.nci.evs.report.exporter.controller.CodeReadRestController;
import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;


@WebMvcTest(CodeReadRestController.class)
@ActiveProfiles("test")
public class CodeReadRestMockTest {


	@Autowired
	private MockMvc mockMvc;
	

	@MockBean
	private CodeReadService service;

	
	@Test
	public void retrieveDetailsForEntity() throws Exception {
		
		List<RestEntity> output = new ArrayList<RestEntity>();
		RestEntity entity = new RestEntity();
		entity.setCode("C12434");
		entity.setName("Blood");
		entity.setTerminology("ncit");
		output.add(entity);

		Mockito.when(service.getRestProperties(Mockito.anyList())).thenReturn(output);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/codereadrest/C12434").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:C12434,name:Blood,terminology:ncit}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}



	@Test
	public void shouldReturnMessageFromService() throws Exception {
		List<String> input = new ArrayList<String>();
		input.add("C12434");
		List<RestEntity> output = new ArrayList<RestEntity>();
		RestEntity entity = new RestEntity();
		entity.setCode("C12434");
		entity.setName("Blood");
		entity.setTerminology("ncit");
		output.add(entity);
		Mockito.when(service.getEntitiesForPropertyNameFilter(Mockito.anyList(),Mockito.anyList())).thenReturn(output);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/codereadrestprops/C12434/Semantic_Type").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:C12434,name:Blood,terminology:ncit}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnEntityWithProp() throws Exception {
		List<String> input = new ArrayList<String>();
		input.add("C12434");
		List<RestEntity> output = new ArrayList<RestEntity>();
		List<Property> props = new ArrayList<Property>();
		Property property = new Property();
		property.setType("Semantic_Type");
		property.setValue("Class");
		props.add(property);
		Property prop2 = new Property();
		prop2.setType("UMLS_CUI");
		prop2.setValue("C11111");
		props.add(prop2);
		RestEntity entity = new RestEntity();
		entity.setCode("C12434");
		entity.setName("Blood");
		entity.setTerminology("ncit");
		entity.setProperties(props);
		output.add(entity);
		Mockito.when(service.getEntitiesForPropertyNameFilter(Mockito.anyList(),Mockito.anyList())).thenReturn(output);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/codereadrestprops/C12434/Semantic_Type").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:C12434,name:Blood,terminology:ncit,properties:[{type:Semantic_Type,value:Class},{type:UMLS_CUI,value:C11111}]}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnEntityWithPropSyn() throws Exception {
		List<String> input = new ArrayList<String>();
		input.add("C12434");
		List<RestEntity> output = new ArrayList<RestEntity>();
		List<Property> props = new ArrayList<Property>();
		List<Synonym> syns = new ArrayList<Synonym>();
		Synonym syn = new Synonym();
		syn.setCode("CBloodtoo");
		syn.setName("AlsoBlood");
		syn.setSource("CDISC");
		syn.setTermGroup("termGroup");
		syn.setSubSource("CDISC-GLOS");
		syn.setType("FULL_SYN");
		syns.add(syn);
		Property property = new Property();
		property.setType("Semantic_Type");
		property.setValue("Class");
		props.add(property);
		Property prop2 = new Property();
		prop2.setType("UMLS_CUI");
		prop2.setValue("C11111");
		props.add(prop2);
		RestEntity entity = new RestEntity();
		entity.setCode("C12434");
		entity.setName("Blood");
		entity.setTerminology("ncit");
		entity.setProperties(props);
		entity.setSynonyms(syns);
		output.add(entity);
		Mockito.when(service.getEntitiesForPropertyNameFilter(Mockito.anyList(),Mockito.anyList())).thenReturn(output);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/codereadrestprops/C12434/Semantic_Type").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:C12434,name:Blood,terminology:ncit,synonyms:[{name:AlsoBlood,termGroup:termGroup,type:FULL_SYN,source:CDISC,code:CBloodtoo,subSource:CDISC-GLOS}],definitions:null,properties:[{type:Semantic_Type,value:Class},{type:UMLS_CUI,value:C11111}]}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnEntityWithPropSynDef() throws Exception {
		List<String> input = new ArrayList<String>();
		input.add("C12434");
		List<RestEntity> output = new ArrayList<RestEntity>();
		List<Property> props = new ArrayList<Property>();
		List<Synonym> syns = new ArrayList<Synonym>();
		List<Definition> defs = new ArrayList<Definition>();
		Definition def = new Definition();
		def.setDefinition("thiswouldbeadefinition");
		def.setSource("NCI");
		def.setType("ALT_Definition");
		defs.add(def);
		Synonym syn = new Synonym();
		syn.setCode("CBloodtoo");
		syn.setName("AlsoBlood");
		syn.setSource("CDISC");
		syn.setTermGroup("termGroup");
		syn.setSubSource("CDISC-GLOS");
		syn.setType("FULL_SYN");
		syns.add(syn);
		Property property = new Property();
		property.setType("Semantic_Type");
		property.setValue("Class");
		props.add(property);
		Property prop2 = new Property();
		prop2.setType("UMLS_CUI");
		prop2.setValue("C11111");
		props.add(prop2);
		RestEntity entity = new RestEntity();
		entity.setCode("C12434");
		entity.setName("Blood");
		entity.setTerminology("ncit");
		entity.setProperties(props);
		entity.setSynonyms(syns);
		entity.setDefinitions(defs);
		output.add(entity);
		Mockito.when(service.getEntitiesForPropertyNameFilter(Mockito.anyList(),Mockito.anyList())).thenReturn(output);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/codereadrestprops/C12434/Semantic_Type").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:C12434,name:Blood,terminology:ncit,synonyms:[{name:AlsoBlood,"
				+ "termGroup:termGroup,"
				+ "type:FULL_SYN,"
				+ "source:CDISC,"
				+ "code:CBloodtoo,"
				+ "subSource:CDISC-GLOS}],"
				+ "definitions:[{"
				+ "definition:thiswouldbeadefinition,"
				+ "source:NCI,type:ALT_Definition}],"
				+ "properties:[{"
				+ "type:Semantic_Type,value:Class},"
				+ "{type:UMLS_CUI,value:C11111}]}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
}