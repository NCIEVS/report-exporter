package gov.nih.nci.evs.report.exporter.util;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.Qualifier;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.model.TypeListAndPositionTuple;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.FormattedOutputService;

class JSONUtilityTest {

	private CodeReadService service;
	FormattedOutputService ouputService;

	@BeforeEach
	void setUp() throws Exception {
		service = new CodeReadService();
		
	}
	
	@Test 
	void testDefinitions(){
		List<RestEntity> list = new GsonBuilder().create().fromJson(
				CommonServices.getGsonForPrettyPrint().toJson(service.getEntitiesForPropertyNameFilter(getRestEntityList(), 
				CommonServices.splitInput(
						"DEFINITION,ALT_DEFINITION"))), 
				new TypeToken<List<RestEntity>>(){}.getType());
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getDefinitions() != null);
		assertTrue(ent1.getDefinitions().size() > 0);
	}
	
	@Test 
	void testSynonyms(){
		List<RestEntity> list = new GsonBuilder().create().fromJson(
				CommonServices.getGsonForPrettyPrint().toJson(service.getEntitiesForPropertyNameFilter(getRestEntityList(), 
				CommonServices.splitInput(
						"FULL_SYN"))), 
				new TypeToken<List<RestEntity>>(){}.getType());
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getSynonyms() != null);
		assertTrue(ent1.getSynonyms().size() > 0);
		}

	@Test
	void testSynsAndOnePropType() {
		List<RestEntity> list = new GsonBuilder().create().fromJson(
				CommonServices.getGsonForPrettyPrint().toJson(service.getEntitiesForPropertyNameFilter(getRestEntityList(), 
				CommonServices.splitInput(
						"FULL_SYN,PropType"
								))), new TypeToken<List<RestEntity>>(){}.getType());
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getSynonyms() != null);
		assertTrue(ent1.getSynonyms().size() > 0);
		assertTrue(ent1.getDefinitions() != null);
		assertTrue(ent1.getDefinitions().size() == 0);
		assertTrue(ent1.getMaps() == null);
		assertTrue(ent1.getProperties() != null);
		assertTrue(ent1.getProperties().size() > 0);
		assertTrue(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType2")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));		
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));	
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));	
		
		RestEntity ent2 = list.stream().filter(x -> x.getCode().equals("C000000")).findAny().get();
		
		assertTrue(ent2.getSynonyms() == null);
		assertTrue(ent2.getDefinitions() == null);
		assertTrue(ent2.getMaps() == null);
		assertTrue(ent2.getProperties() != null);
		assertTrue(ent2.getProperties().size() == 0);
		assertFalse(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertFalse(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));
		
		RestEntity ent3 = list.stream().filter(x -> x.getCode().equals("C999999")).findAny().get();
		
		assertTrue(ent3.getSynonyms() == null);
		assertTrue(ent3.getDefinitions() == null);
		assertTrue(ent3.getMaps() == null);
		assertTrue(ent3.getProperties() != null);
		assertTrue(ent3.getProperties().size() > 0);
		assertFalse(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));
		assertFalse(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));
		assertTrue(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		
		assertTrue(ent4.getSynonyms() == null);
		assertTrue(ent4.getDefinitions() == null);
		assertTrue(ent4.getMaps() != null);
		assertTrue(ent4.getMaps().size() == 0);
		assertTrue(ent4.getProperties() != null);
		assertTrue(ent4.getProperties().size() > 0);
		assertFalse(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type")));
		assertFalse(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type2")));
		assertTrue(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
	}
	
	@Test 
	void testAllProps(){
		List<RestEntity> list = new GsonBuilder().create().fromJson(
				CommonServices.getGsonForPrettyPrint().toJson(service.getEntitiesForPropertyNameFilter(getRestEntityList(), 
				CommonServices.splitInput(
						"Maps_To,FULL_SYN,DEFINITION,"
						+ "ALT_DEFINITION,PropType,PropType2,"
						+ "Prop0Type,GO_Annotation,"
						+ "Prop9Type,Prop9Type2"))), 
				new TypeToken<List<RestEntity>>(){}.getType());
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getSynonyms() != null);
		assertTrue(ent1.getSynonyms().size() > 0);
		assertTrue(ent1.getDefinitions() != null);
		assertTrue(ent1.getDefinitions().size() > 0);
		assertTrue(ent1.getMaps() == null);
		assertTrue(ent1.getProperties() != null);
		assertTrue(ent1.getProperties().size() > 0);
		assertTrue(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));
		assertTrue(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType2")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));		
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));	
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));	
		
		RestEntity ent2 = list.stream().filter(x -> x.getCode().equals("C000000")).findAny().get();
		
		assertTrue(ent2.getSynonyms() == null);
		assertTrue(ent2.getDefinitions() == null);
		assertTrue(ent2.getMaps() == null);
		assertTrue(ent2.getProperties() != null);
		assertTrue(ent2.getProperties().size() > 0);
		assertTrue(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertTrue(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));
		
		RestEntity ent3 = list.stream().filter(x -> x.getCode().equals("C999999")).findAny().get();
		
		assertTrue(ent3.getSynonyms() == null);
		assertTrue(ent3.getDefinitions() == null);
		assertTrue(ent3.getMaps() == null);
		assertTrue(ent3.getProperties() != null);
		assertTrue(ent3.getProperties().size() > 0);
		assertTrue(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));
		assertTrue(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));
		assertTrue(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		
		assertTrue(ent4.getSynonyms() == null);
		assertTrue(ent4.getDefinitions() == null);
		assertTrue(ent4.getMaps() != null);
		assertTrue(ent4.getMaps().size() > 0);
		assertTrue(ent4.getProperties() != null);
		assertTrue(ent4.getProperties().size() > 0);
		assertFalse(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type")));
		assertFalse(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type2")));
		assertTrue(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
	
	}
	
	@Test 
	void testAllClassPropsNoProps(){
		List<RestEntity> list = new GsonBuilder().create().fromJson(
				CommonServices.getGsonForPrettyPrint().toJson(service.getEntitiesForPropertyNameFilter(getRestEntityList(), 
				CommonServices.splitInput(
						"Maps_To,FULL_SYN,DEFINITION,"
						+ "ALT_DEFINITION"))), 
				new TypeToken<List<RestEntity>>(){}.getType());
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getSynonyms() != null);
		assertTrue(ent1.getSynonyms().size() > 0);
		assertTrue(ent1.getDefinitions() != null);
		assertTrue(ent1.getDefinitions().size() > 0);
		assertTrue(ent1.getMaps() == null);
		assertTrue(ent1.getProperties() != null);
		assertTrue(ent1.getProperties().size() == 0);
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType2")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));		
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));	
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));	
		
		RestEntity ent2 = list.stream().filter(x -> x.getCode().equals("C000000")).findAny().get();
		
		assertTrue(ent2.getSynonyms() == null);
		assertTrue(ent2.getDefinitions() == null);
		assertTrue(ent2.getMaps() == null);
		assertTrue(ent2.getProperties() != null);
		assertTrue(ent2.getProperties().size() == 0);
		assertFalse(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertFalse(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));
		
		RestEntity ent3 = list.stream().filter(x -> x.getCode().equals("C999999")).findAny().get();
		
		assertTrue(ent3.getSynonyms() == null);
		assertTrue(ent3.getDefinitions() == null);
		assertTrue(ent3.getMaps() == null);
		assertTrue(ent3.getProperties() != null);
		assertTrue(ent3.getProperties().size() == 0);
		assertFalse(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));
		assertFalse(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));
		assertFalse(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		
		assertTrue(ent4.getSynonyms() == null);
		assertTrue(ent4.getDefinitions() == null);
		assertTrue(ent4.getMaps() != null);
		assertTrue(ent4.getMaps().size() > 0);
		assertTrue(ent4.getProperties() != null);
		assertTrue(ent4.getProperties().size() == 0);
		assertFalse(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type")));
		assertFalse(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type2")));
		assertFalse(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
	
	}
	
	@Test 
	void testAllNoClassPropsAllProps(){
		List<RestEntity> list = new GsonBuilder().create().fromJson(
				CommonServices.getGsonForPrettyPrint().toJson(service.getEntitiesForPropertyNameFilter(getRestEntityList(), 
				CommonServices.splitInput(
						"PropType,PropType2,"
						+ "Prop0Type,GO_Annotation,"
						+ "Prop9Type,Prop9Type2,"
						+ "Prop2Type,Prop2Type2"))), 
				new TypeToken<List<RestEntity>>(){}.getType());
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getSynonyms() != null);
		assertTrue(ent1.getSynonyms().size() == 0);
		assertTrue(ent1.getDefinitions() != null);
		assertTrue(ent1.getDefinitions().size() == 0);
		assertTrue(ent1.getMaps() == null);
		assertTrue(ent1.getProperties() != null);
		assertTrue(ent1.getProperties().size() > 0);
		assertTrue(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));
		assertTrue(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType2")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));		
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));	
		assertFalse(ent1
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));	
		
		RestEntity ent2 = list.stream().filter(x -> x.getCode().equals("C000000")).findAny().get();
		
		assertTrue(ent2.getSynonyms() == null);
		assertTrue(ent2.getDefinitions() == null);
		assertTrue(ent2.getMaps() == null);
		assertTrue(ent2.getProperties() != null);
		assertTrue(ent2.getProperties().size() > 0);
		assertTrue(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop0Type")));
		assertTrue(ent2
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("GO_Annotation")));
		
		RestEntity ent3 = list.stream().filter(x -> x.getCode().equals("C999999")).findAny().get();
		
		assertTrue(ent3.getSynonyms() == null);
		assertTrue(ent3.getDefinitions() == null);
		assertTrue(ent3.getMaps() == null);
		assertTrue(ent3.getProperties() != null);
		assertTrue(ent3.getProperties().size() > 0);
		assertTrue(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type")));
		assertTrue(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop9Type2")));
		assertTrue(ent3
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		
		assertTrue(ent4.getSynonyms() == null);
		assertTrue(ent4.getDefinitions() == null);
		assertTrue(ent4.getMaps() != null);
		assertTrue(ent4.getMaps().size() == 0);
		assertTrue(ent4.getProperties() != null);
		assertTrue(ent4.getProperties().size() > 0);
		assertTrue(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type")));
		assertTrue(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type2")));
		assertTrue(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropType")));	
		
	
	}
	
	
	private List<RestEntity> getRestEntityList() {
		List<RestEntity> list = new ArrayList<RestEntity>();
		
		RestEntity ent = new RestEntity();
		ent.setCode("C123234");
		ent.setName("Myent");
		ent.setTerminology("ncit");
		
		List<Synonym> syns = new ArrayList<Synonym>();
		Synonym syn = new Synonym();
		syn.setType("FULL_SYN");
		syn.setName("synName");
		syn.setSource("NCIt");
		syn.setSubSource("CDISC");
		syn.setTermGroup("mytermgr");
		Synonym syn2 = new Synonym();
		syn2.setType("FULL_SYN");
		syn2.setSource("synSource2");
		syn2.setSubSource("NCI");
		syn2.setTermGroup("atermgrp");
		syn2.setName("synName2");
		syns.add(syn);
		syns.add(syn2);
		ent.setSynonyms(syns);
		
		List<Definition> defs = new ArrayList<Definition>();
		Definition def = new Definition();
		def.setType("DEFINITION");
		def.setDefinition("defvalue");
		def.setSource("NCI");
		Definition def2 = new Definition();
		def2.setType("ALT_DEFINITIO");
		def2.setDefinition("defvalue2");
		defs.add(def);
		defs.add(def2);
		ent.setDefinitions(defs);
		
		List<Property> props = new ArrayList<Property>();
		Property prop = new Property();
		prop.setType("PropType");
		prop.setValue("propvalue");
		Property propa = new Property();
		propa.setType("PropType");
		propa.setValue("propvalue1");
		Property prop2 = new Property();
		prop2.setType("PropType2");
		prop2.setValue("propvalue2");
		props.add(prop);
		props.add(propa);
		props.add(prop2);
		ent.setProperties(props);
		
		RestEntity ent1 = new RestEntity();
		ent1.setCode("C000000");
		ent1.setName("0ent");
		ent1.setTerminology("ncit");
		List<Property> props1 = new ArrayList<Property>();
		Property prop1 = new Property();
		prop1.setType("Prop0Type");
		prop1.setValue("prop0value");
		Property prop0 = new Property();
		prop0.setType("GO_Annotation");
		prop0.setValue("prop0value2");
		Qualifier qual = new Qualifier();
		Qualifier qual1 = new Qualifier();
		Qualifier qual2 = new Qualifier();
		Qualifier qual3 = new Qualifier();
		qual.setType("go-evi");
		qual.setValue("TAS");
		qual1.setType("go-id");
		qual1.setValue("GO:0000075");
		qual2.setType("source-date");
		qual2.setValue("29-SEP-2003");
		qual3.setType("go-source");
		qual3.setValue("CGAP");
		List<Qualifier> quallist = new ArrayList<Qualifier>();
		quallist.add(qual);
		quallist.add(qual1);
		quallist.add(qual2);
		quallist.add(qual3);
		prop0.setQualifiers(quallist);
		props1.add(prop1);
		props1.add(prop0);
		ent1.setProperties(props1);
		
		RestEntity ent9 = new RestEntity();
		ent9.setCode("C999999");
		ent9.setName("My9");
		ent9.setTerminology("ncit");
		List<Property> props9 = new ArrayList<Property>();
		Property prop9 = new Property();
		prop9.setType("Prop9Type");
		prop9.setValue("prop9value");
		Property prop29 = new Property();
		prop29.setType("Prop9Type2");
		prop29.setValue("prop9value2");
		Property prop39 = new Property();
		prop39.setType("PropType");
		prop39.setValue("prop9value3");
		props9.add(prop9);
		props9.add(prop29);
		props9.add(prop39);
		ent9.setProperties(props9);
		
		
		RestEntity ent2 = new RestEntity();
		ent2.setCode("C2222");
		ent2.setName("My2");
		ent2.setTerminology("ncit");
		List<Property> props2 = new ArrayList<Property>();
		Property prop2a = new Property();
		prop2a.setType("Prop2Type");
		prop2a.setValue("prop2value");
		Property prop22 = new Property();
		prop22.setType("Prop2Type2");
		prop22.setValue("prop2value2");
		Property prop32 = new Property();
		prop32.setType("PropType");
		prop32.setValue("prop2value3");
		props2.add(prop2a);
		props2.add(prop22);
		props2.add(prop32);
		ent2.setProperties(props2);
		List<PropertyMap> maps = new ArrayList<PropertyMap>();
		PropertyMap map = new PropertyMap();
		map.setType("Has Synonym");
		map.setTargetName("Acute myeloid leukemia, NOS");
		map.setTargetTermGroup("PT");
		map.setTargetCode("PD");
		map.setTargetTerminology("GDC");
		
		PropertyMap map1 = new PropertyMap();
		map1.setType("Related To");
		map1.setTargetName("Acute myeloid leukemia, NOS");
		map1.setTargetTermGroup("PT");
		map1.setTargetCode("9861/3");
		map1.setTargetTerminology("ICDO3");
		map1.setTargetTerminologyVersion("3.1");
		
		PropertyMap map2 = new PropertyMap();
		map2.setType("Related To");
		map2.setTargetName("Acute myeloid leukemia, NOS");
		map2.setTargetTermGroup("PT");
		map2.setTargetCode("9861/3");
		map2.setTargetTerminology("ICDO3");
		map2.setTargetTerminologyVersion("3.2");
		maps.add(map);
		maps.add(map1);
		maps.add(map);
		
		ent2.setMaps(maps);
		

		list.add(ent);
		list.add(ent1);
		list.add(ent9);
		list.add(ent2);
		return list;
	}	
	
	
	public RestEntity getRestEntity() {
		
		RestEntity ent = new RestEntity();
		
		Synonym syn = new Synonym();
		//syn.setCode(code);
		syn.setName("Clinical Data Interchange Standards Consortium Terminology");
		//syn.setSource("Preferred_Name");
		///syn.setTermGroup(termGroup);
		syn.setType( "Preferred_Name");
		Synonym syn1 = new Synonym();
//		syn1.setCode(code);
		syn1.setName("Clinical Data Interchange Standards Consortium Terminology");
		syn1.setSource("NCI");
		syn1.setTermGroup("PT");
		syn1.setType("FULL_SYN");
		Synonym syn2 = new Synonym();
//		syn2.setCode(code);
		syn2.setName("CDISC Terminology");
		syn2.setSource("NCI");
		syn2.setTermGroup("SY");
		syn2.setType("FULL_SYN");
		Synonym syn3 = new Synonym();
//		syn3.setCode(code);
		syn3.setName("CDISC");
		syn3.setSource("NCI");
		syn3.setTermGroup("SY");
		syn3.setType("FULL_SYN");
		
		Definition def = new Definition();
		def.setDefinition("terms relative to CDISC.");
		def.setSource("NCI");
		def.setType("DEFINITION");
		
		Property prop = new Property();
		//prop.setQualifiers(qualifiers);
		prop.setType("Semantic_Type");
		prop.setValue("Intellectual Product");
		Property prop1 = new Property();
		//prop1.setQualifiers(qualifiers);
		prop1.setType("UMLS_CUI");
		prop1.setValue("C1880104");
		Property prop2 = new Property();
		//prop2.setQualifiers(qualifiers);
		prop2.setType("Contributing_Source");
		prop2.setValue("CDISC");
//		Property prop3 = new Property();
		//prop3.setQualifiers(qualifiers);
//		prop3.setType("Legacy Concept Name");
//		prop3.setValue("Clinical_Data_Interchange_Standards_Consortium");
//		Property prop4 = new Property();
//		//prop4.setQualifiers(qualifiers);
//		prop4.setType("Publish_Value_Set");
//		prop4.setValue("Yes");
//		Property prop5 = new Property();
//		//prop5.setQualifiers(qualifiers);
//		prop5.setType("Term_Browser_Value_Set_Description");
//		prop5.setValue("tb value");
//		Property prop6 = new Property();
//		//prop6.setQualifiers(qualifiers);
//		prop6.setType("Value_Set_Pair");
//		prop6.setValue("No");
		
		List<Synonym> synonyms = new ArrayList<Synonym>();
		synonyms.add(syn);
		synonyms.add(syn1);
		synonyms.add(syn2);
		synonyms.add(syn3);
		List<Definition> definitions = new ArrayList<Definition>();
		definitions.add(def);
		List<PropertyMap> maps = new ArrayList<PropertyMap>();
		List<Property> props = new ArrayList<Property>();
		List<Root>  parents = new ArrayList<Root>();
		Root parent = new Root();
		parent.setCode("C54443");
		parent.setName("Terminology Subset");
		parents.add(parent);
		props.add(prop);
		props.add(prop1);
		props.add(prop2);
//		props.add(prop3);
//		props.add(prop4);
//		props.add(prop5);
//		props.add(prop6);
		ent.setCode("C61410");
		ent.setName("Clinical Data Interchange Standards Consortium Terminology");
		ent.setTerminology("ncit");
		ent.setParents(parents);
		ent.setSynonyms(synonyms);
		ent.setDefinitions(definitions);
		ent.setMaps(maps);
		ent.setProperties(props);
		return ent;
	}
	
public RestEntity getRestEntityWNoDefsNoMaps() {
		
		RestEntity ent = new RestEntity();
		
		Synonym syn = new Synonym();
		//syn.setCode(code);
		syn.setName("Clinical Data Interchange Standards Consortium Terminology");
		//syn.setSource("Preferred_Name");
		///syn.setTermGroup(termGroup);
		syn.setType( "Preferred_Name");
		Synonym syn1 = new Synonym();
//		syn1.setCode(code);
		syn1.setName("Clinical Data Interchange Standards Consortium Terminology");
		syn1.setSource("NCI");
		syn1.setTermGroup("PT");
		syn1.setType("FULL_SYN");
		Synonym syn2 = new Synonym();
//		syn2.setCode(code);
		syn2.setName("CDISC Terminology");
		syn2.setSource("NCI");
		syn2.setTermGroup("SY");
		syn2.setType("FULL_SYN");
		Synonym syn3 = new Synonym();
//		syn3.setCode(code);
		syn3.setName("CDISC");
		syn3.setSource("NCI");
		syn3.setTermGroup("SY");
		syn3.setType("FULL_SYN");

		
		Property prop = new Property();
		//prop.setQualifiers(qualifiers);
		prop.setType("Semantic_Type");
		prop.setValue("Intellectual Product");
		Property prop1 = new Property();
		//prop1.setQualifiers(qualifiers);
		prop1.setType("UMLS_CUI");
		prop1.setValue("C1880104");
		Property prop2 = new Property();
		//prop2.setQualifiers(qualifiers);
		prop2.setType("Contributing_Source");
		prop2.setValue("CDISC");

		
		List<Synonym> synonyms = new ArrayList<Synonym>();
		synonyms.add(syn);
		synonyms.add(syn1);
		synonyms.add(syn2);
		synonyms.add(syn3);
		List<Definition> definitions = new ArrayList<Definition>();
		List<PropertyMap> maps = new ArrayList<PropertyMap>();
		List<Property> props = new ArrayList<Property>();
		List<Root>  parents = new ArrayList<Root>();
		Root parent = new Root();
		parent.setCode("C54443");
		parent.setName("Terminology Subset");
		parents.add(parent);
		props.add(prop);
		props.add(prop1);
		props.add(prop2);
//		props.add(prop3);
//		props.add(prop4);
//		props.add(prop5);
//		props.add(prop6);
		ent.setCode("C61410");
		ent.setName("Clinical Data Interchange Standards Consortium Terminology");
		ent.setTerminology("ncit");
		ent.setParents(parents);
		ent.setSynonyms(synonyms);
		ent.setDefinitions(definitions);
		ent.setMaps(maps);
		ent.setProperties(props);
		return ent;
	}

public RestEntity getRestEntityWNoMapsEmptySynsEmptyDefs() {
	
	RestEntity ent = new RestEntity();
	
	
	Property prop = new Property();
	//prop.setQualifiers(qualifiers);
	prop.setType("Semantic_Type");
	prop.setValue("Intellectual Product");
	Property prop1 = new Property();
	//prop1.setQualifiers(qualifiers);
	prop1.setType("UMLS_CUI");
	prop1.setValue("C1880104");
	Property prop2 = new Property();
	//prop2.setQualifiers(qualifiers);
	prop2.setType("Contributing_Source");
	prop2.setValue("CDISC");
	
	PropertyMap map = new PropertyMap();
	map.setType("Has Synonym");
	map.setTargetName("Acute myeloid leukemia, NOS");
	map.setTargetTermGroup("PT");
	map.setTargetCode("PD");
	map.setTargetTerminology("GDC");
	
	List<Synonym> synonyms = new ArrayList<Synonym>();
	List<Definition> definitions = new ArrayList<Definition>();
	List<PropertyMap> maps = new ArrayList<PropertyMap>();
	List<Property> props = new ArrayList<Property>();
	List<Root>  parents = new ArrayList<Root>();
	Root parent = new Root();
	parent.setCode("C54443");
	parent.setName("Terminology Subset");
	parents.add(parent);
	props.add(prop);
	props.add(prop1);
	props.add(prop2);
	ent.setCode("C61410");
	ent.setName("Clinical Data Interchange Standards Consortium Terminology");
	ent.setTerminology("ncit");
	ent.setParents(parents);
	ent.setSynonyms(synonyms);
	ent.setDefinitions(definitions);
	ent.setMaps(maps);
	ent.setProperties(props);
	return ent;
}


public RestEntity getRestEntityWNoDefsEmptySynsEmptyMaps() {
	
	RestEntity ent = new RestEntity();
	
	Definition def = new Definition();
	def.setDefinition("terms relative to CDISC.");
	def.setSource("NCI");
	def.setType("DEFINITION");

	
	Property prop = new Property();
	//prop.setQualifiers(qualifiers);
	prop.setType("Semantic_Type");
	prop.setValue("Intellectual Product");
	Property prop1 = new Property();
	//prop1.setQualifiers(qualifiers);
	prop1.setType("UMLS_CUI");
	prop1.setValue("C1880104");
	Property prop2 = new Property();
	//prop2.setQualifiers(qualifiers);
	prop2.setType("Contributing_Source");
	prop2.setValue("CDISC");

	
	List<Synonym> synonyms = new ArrayList<Synonym>();
	List<Definition> definitions = new ArrayList<Definition>();
	definitions.add(def);
	List<PropertyMap> maps = new ArrayList<PropertyMap>();
	List<Property> props = new ArrayList<Property>();
	List<Root>  parents = new ArrayList<Root>();
	Root parent = new Root();
	parent.setCode("C54443");
	parent.setName("Terminology Subset");
	parents.add(parent);
	props.add(prop);
	props.add(prop1);
	props.add(prop2);

	ent.setCode("C61410");
	ent.setName("Clinical Data Interchange Standards Consortium Terminology");
	ent.setTerminology("ncit");
	ent.setParents(parents);
	ent.setSynonyms(synonyms);
	ent.setDefinitions(definitions);
	ent.setMaps(maps);
	ent.setProperties(props);
	return ent;
}

}
