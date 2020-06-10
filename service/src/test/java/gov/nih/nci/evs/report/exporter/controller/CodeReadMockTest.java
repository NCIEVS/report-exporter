package gov.nih.nci.evs.report.exporter.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import gov.nih.nci.evs.report.exporter.controller.CodeReadController;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

@WebMvcTest(CodeReadController.class)
public class CodeReadMockTest {

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private CodeReadService service;

	@Test
	public void shouldReturnRestEntity() throws Exception {
		List<String> input = new ArrayList<String>();
		input.add("C1234");
		List<RestEntity> output = new ArrayList<RestEntity>();
		RestEntity entity = new RestEntity();
		entity.setCode("C1234");
		entity.setName("Blood");
		entity.setTerminology("ncit");
		when(service.getRestEntities(input)).thenReturn(output);
		this.mockMvc.perform(post("/coderead").queryParam("id", "C1234")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("C1234")));
	}
}