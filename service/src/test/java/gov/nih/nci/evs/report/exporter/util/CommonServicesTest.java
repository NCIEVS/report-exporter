package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestEntity;

class CommonServicesTest {
	
	@Test
	void testGetListValues() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		list.add("four");
		String csvList = CommonServices.getListValues(list);
		assertEquals("\"[one, two, three, four]\"", csvList);		
	}
	
	@Test
	void testCleanListOutPut() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		list.add("four");
		String csvList = CommonServices.getListValues(list);
		assertEquals("\"[one, two, three, four]\"", csvList);
		String cleanedList = CommonServices.cleanListOutPut(csvList);
		assertFalse(cleanedList.contains("["));
		assertFalse(cleanedList.contains("]"));
	}
	
	@Test
	void testSplitInput() {
		String list = "one,two,three,four";
		List<String> splitList = CommonServices.splitInput(list);
		assertTrue(splitList.size() == 4);
		assertTrue(splitList.stream().filter(x -> x.equals("one")).findAny().isPresent());
		assertTrue(splitList.stream().filter(x -> x.equals("two")).findAny().isPresent());
		assertTrue(splitList.stream().filter(x -> x.equals("three")).findAny().isPresent());
		assertTrue(splitList.stream().filter(x -> x.equals("four")).findAny().isPresent());
	}
	
	@Test
	void testGetGsonForPrettyPrint() {
		RestEntity ent = new RestEntity();
		ent.setCode("C123234");
		ent.setName("Myent");
		ent.setTerminology("ncit");
		List<Property> props = new ArrayList<Property>();
		Property prop = new Property();
		prop.setType("PropType");
		prop.setValue("propvalue");
		Property prop2 = new Property();
		prop2.setType("PropType2");
		prop2.setValue("propvalue2");
		props.add(prop);
		props.add(prop2);
		ent.setProperties(props);
		Gson gson = CommonServices.getGsonForPrettyPrint();
		gson.toJson(ent);
		System.out.println(gson.toJson(ent));
		Gson gson2 = new Gson();
		System.out.println(gson2.toJson(ent));
	}
	
	@Test
	void testGetRestTemplate() {
		RestTemplate t = CommonServices.getRestTemplate();
		assertTrue(t != null);
	}
	
	@Test
	void testGetRestTemplateNoBuilder() {
		RestTemplate t = CommonServices.getRestTemplate(new RestTemplateBuilder());
		assertTrue(t != null);
	}

}
