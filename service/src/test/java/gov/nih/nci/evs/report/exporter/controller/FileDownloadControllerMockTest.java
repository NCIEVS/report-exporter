package gov.nih.nci.evs.report.exporter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
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

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutService;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutServiceDeferredWrapper;
import gov.nih.nci.evs.report.exporter.service.FormattedOutputService;
import gov.nih.nci.evs.report.exporter.service.RoleService;

@WebMvcTest(FileDownloadController.class)
@ActiveProfiles("test")
public class FileDownloadControllerMockTest {


	@Autowired
	private MockMvc mockMvc;
	

	@MockBean
	private CodeReadService service;
	
	@MockBean
	private FormattedOutputService outputservice;
	
	@MockBean
	private RoleService roleService;
	
	@MockBean
	private FormattedBranchOutPutService branchoutputservice;

	
	@MockBean
	private FormattedBranchOutPutServiceDeferredWrapper deferredbranchoutputservice;

	
	@Test
	public void retrieveJSONFileForCode() throws Exception {
		
		List<RestEntity> output = new ArrayList<RestEntity>();
		RestEntity entity = new RestEntity();
		entity.setCode("C12434");
		entity.setName("Blood");
		entity.setTerminology("ncit");
		output.add(entity);

		Mockito.when(service.getRestEntities(Mockito.anyList())).thenReturn(output);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/download/get-file/C12434//JsonFile.json").accept(
				MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:C12434,name:Blood,terminology:ncit}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}



	@Test
	public void shouldReturnJSONUsingProps() throws Exception {
		List<String> input = new ArrayList<String>();
		input.add("C12434");;

		String output = "[{code:C12434,name:Blood,terminology:ncit}]";
		Mockito.when(outputservice.getJsonBytesForRestParams(
				Mockito.anyString(),Mockito.anyString()))
		.thenReturn(new ByteArrayInputStream(output.getBytes()));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/download//get-file-for-readCodes/C44444/Semqntic_Type/JSON/file.json").accept(
				MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{code:C12434,name:Blood,terminology:ncit}]";

		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnCSVFile() throws Exception {
		
		
		String output = "code,name,terminology,synonyms,definitions,properties\n" + 
				"C12434,Blood,ncit,\"FULL_SYN, Half_Syn\",\"a definiton,another definition\",\"a prop,prop 2\"";
		Mockito.when(outputservice.getCSVBytesForRestParams(Mockito.anyString(), Mockito.anyString())).thenReturn(new ByteArrayInputStream(output.getBytes()));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/download//get-file-for-readCodes/C33333/PROP/CSV/file.csv").accept(
				MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		
		
		assertEquals(output, result.getResponse()
				.getContentAsString());
	}
	
	@Test
	public void shouldReturnTabdelFile() throws Exception {
		
		
		String output = "code\tname\tterminology\tsynonyms\tdefinitions\tproperties\n" + 
				"C12434\tBlood\tncit\t\"FULL_SYN\t Half_Syn\"\t\"a definiton\tanother definition\"\t\"a prop\tprop 2\"";
		Mockito.when(outputservice.
				getTabDelBytesForRestParams(
						Mockito.anyString(), Mockito.anyString())).
		thenReturn(
				new ByteArrayInputStream(output.getBytes()));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/download//get-file-for-readCodes/C33333/PROP/TABD/file.txt").accept(
				MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		
		
		assertEquals(output, result.getResponse()
				.getContentAsString());
	}
	
	@Test
	public void shouldReturnXSLFile() throws Exception {
		
		String output = "code\tname\tterminology\tsynonyms\tdefinitions\tproperties\n" + 
				"C12434\tBlood\tncit\t\"FULL_SYN\t Half_Syn\"\t\"a definiton\tanother definition\"\t\"a prop\tprop 2\"";
		Mockito.when(outputservice.getXSLBytesForRestParams(Mockito.anyString(), Mockito.anyString())).thenReturn(new ByteArrayInputStream(output.getBytes()));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/download/get-file-for-excel/C33333/PROP").accept(
				MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		
		
		assertTrue(result.getResponse()
				.getContentAsString() != null);
	}
}