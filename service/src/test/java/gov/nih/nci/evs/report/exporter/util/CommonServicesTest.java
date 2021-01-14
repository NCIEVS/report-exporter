package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Synonym;

class CommonServicesTest {
	
	CommonServices services;
	
	@Before
	void setUp() {
		services = new CommonServices();
	}
	
	@Test
	void testGetListValues() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		list.add("four");
		String csvList = CommonServices.getListValues(list);
		assertEquals("\"|one|two|three|four|\"", csvList);		
	}
	
	@Test
	void testCleanListOutPut() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		list.add("four");
		String csvList = CommonServices.getListValues(list);
		assertEquals("\"|one|two|three|four|\"", csvList);
		String cleanedList = CommonServices.cleanListOutPut(csvList);
		assertFalse(cleanedList.contains("["));
		assertFalse(cleanedList.contains("]"));
	}
	
	@Test
	void TestGetListValuesWithPipeDelimiter() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		list.add("four");
		String csvList = CommonServices.getListValuesWithPipeDelimiter(list);
		System.out.println(csvList);
		assertEquals("|one|two|three|four", csvList);
		String cleanedList = CommonServices.cleanListOutPut(csvList);
		assertFalse(cleanedList.contains("["));
		assertFalse(cleanedList.contains("]"));
	}
	
	@Test
	void TestGetListValuesWithPipeDelimiter3() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		String csvList = CommonServices.getListValuesWithPipeDelimiter(list);
		System.out.println(csvList);
		assertEquals("|one|two|three", csvList);
		String cleanedList = CommonServices.cleanListOutPut(csvList);
		assertFalse(cleanedList.contains("["));
		assertFalse(cleanedList.contains("]"));
	}
	
	@Test
	void TestGetListValuesWithPipeDelimiter2() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		String csvList = CommonServices.getListValuesWithPipeDelimiter(list);
		System.out.println(csvList);
		assertEquals("|one|two", csvList);
		String cleanedList = CommonServices.cleanListOutPut(csvList);
		assertFalse(cleanedList.contains("["));
		assertFalse(cleanedList.contains("]"));
	}
	
	@Test
	void TestGetListValuesWithPipeDelimiter1() {
		List<String> list = new ArrayList<String>();
		list.add("one");
		String csvList = CommonServices.getListValuesWithPipeDelimiter(list);
		System.out.println(csvList);
		assertEquals("|one", csvList);
		String cleanedList = CommonServices.cleanListOutPut(csvList);
		assertFalse(cleanedList.contains("["));
		assertFalse(cleanedList.contains("]"));
	}
	
	@Test
	void TestGetListValuesWithPipeDelimiterSyns() {
		List<Synonym> list = new ArrayList<Synonym>();
		Synonym syn1 = new Synonym();
		syn1.setCode("234");
		syn1.setName("syn1");
		syn1.setSource("CDISC");
		syn1.setSubSource("GAIA");
		syn1.setTermGroup("PT");
		syn1.setType("FULL_SYN");
		Synonym syn2 = new Synonym();
		syn2.setCode("435");
		syn2.setName("syn2");
		syn2.setSource("NCI");
		syn2.setSubSource("BAD");
		syn2.setTermGroup("SY");
		Synonym syn3 = new Synonym();
		syn3.setCode("234");
		syn3.setName("syn3");
		syn3.setSource("CSYN");
		syn3.setSubSource("FULLS");
		syn3.setTermGroup("PT");
		syn3.setType("Preferred_Name");
		list.add(syn1);
		list.add(syn2);
		list.add(syn3);
		String csvList = CommonServices.getListValuesWithPipeDelimiter(list);
		System.out.println(csvList);
		assertEquals("|CDISC GAIA PT:syn1 234|NCI BAD SY:syn2 435", csvList);
		String cleanedList = CommonServices.cleanListOutPut(csvList);
		assertFalse(cleanedList.contains("["));
		assertFalse(cleanedList.contains("]"));
	}
	@Test
	void TestRemoveAllNonSourceTypeSynonyms() {
		Synonym syn = new Synonym();
		syn.setCode("C1232");
		syn.setName("SynNameOne");
		syn.setSource("CDISC");
		syn.setType("FULL_SYN");
		syn.setTermGroup("PT");
		Synonym synPreferredName = new Synonym();
		synPreferredName.setCode("C12434");
		synPreferredName.setName("Blood");
		synPreferredName.setSource(null);
		synPreferredName.setSubSource(null);
		synPreferredName.setTermGroup(null);
		synPreferredName.setType("Preferred_Name");
		Synonym synNoType = new Synonym();
		synNoType.setCode("C0345");
		synNoType.setName("NoTypeSyn");
		synNoType.setSource("NCI");
		synNoType.setSubSource("CADSR");
		synNoType.setTermGroup("SY");
		synNoType.setType(null);
		Definition def = new Definition();
		def.setDefinition("a definition");
		def.setSource("CDISC");
		def.setType("DEFINITION");
		Property prop = new Property();
		prop.setQualifiers(null);
		prop.setType("Prop");
		prop.setValue("a prop value");
		assertNotNull(CommonServices.removeAllNoSourceNoTypeSynonyms(syn));
		assertNull(CommonServices.removeAllNoSourceNoTypeSynonyms(synPreferredName));
		assertNotNull(CommonServices.removeAllNoSourceNoTypeSynonyms(synNoType));
		assertNotNull(CommonServices.removeAllNoSourceNoTypeSynonyms(def));
		assertNotNull(CommonServices.removeAllNoSourceNoTypeSynonyms(prop));
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
	
	@Test
	void testGetFilteredHeadingListSynonym() {
		List<String> props = new ArrayList<String>();
		props.add("FULL_SYN");
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		flags.noEntitiesHaveSyns = false;
		List<String> filtered = services.filterHeadings(services);
		assertTrue(filtered.contains("synonyms"));
		assertFalse(filtered.contains("definitions"));
		assertFalse(filtered.contains("Map_To"));
	}
	
	@Test
	void testGetFilteredHeadingListMaps() {
		List<String> props = new ArrayList<String>();
		props.add("Maps_To");
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		flags.noEntitiesHaveMaps = false;
		List<String> filtered = services.filterHeadings(services);
		assertFalse(filtered.contains("synonyms"));
		assertFalse(filtered.contains("definitions"));
		assertTrue(filtered.contains("Maps_To"));
	}
	@Test
	void testGetFilteredHeadingListDefinition() {
		List<String> props = new ArrayList<String>();
		props.add("DEFINITION");
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		flags.noEntitiesHaveDefs = false;
		List<String> filtered = services.filterHeadings(services);
		assertFalse(filtered.contains("synonyms"));
		assertTrue(filtered.contains("definitions"));
		assertFalse(filtered.contains("Maps_To"));
	}
	@Test
	void testGetFilteredHeadingListDefinitionb() {
		List<String> props = new ArrayList<String>();
		props.add("ALT_DEFINITION");
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		flags.noEntitiesHaveDefs = false;
		List<String> filtered = services.filterHeadings(services);
		assertFalse(filtered.contains("synonyms"));
		assertTrue(filtered.contains("definitions"));
		assertFalse(filtered.contains("Maps_To"));
	}
	@Test
	void testGetFilteredHeadingListDefinition2() {
		List<String> props = new ArrayList<String>();
		props.add("DEFINITION");
		props.add("ALT_DEFINITION");
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		flags.noEntitiesHaveDefs = false;
		List<String> filtered = services.filterHeadings(services);
		assertFalse(filtered.contains("synonyms"));
		assertTrue(filtered.contains("definitions"));
		assertFalse(filtered.contains("Maps_To"));
	}
	
	@Test
	void testGetFilteredHeadingListDefinitionNSynoyms() {
		List<String> props = new ArrayList<String>();
		props.add("DEFINITION");
		props.add("FULL_SYN");
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		flags.noEntitiesHaveSyns = false;
		flags.noEntitiesHaveDefs = false;
		List<String> filtered = services.filterHeadings(services);
		assertTrue(filtered.contains("synonyms"));
		assertTrue(filtered.contains("definitions"));
		assertFalse(filtered.contains("Maps_To"));
	}
	
	@Test
	void testGetFilteredHeadingListDefinitionNMaps() {
		List<String> props = new ArrayList<String>();
		props.add("DEFINITION");
		props.add("Maps_To");
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		flags.noEntitiesHaveDefs = false;
		flags.noEntitiesHaveMaps = false;
		List<String> filtered = services.filterHeadings(services);
		assertFalse(filtered.contains("synonyms"));
		assertTrue(filtered.contains("definitions"));
		assertTrue(filtered.contains("Maps_To"));
	}

}
