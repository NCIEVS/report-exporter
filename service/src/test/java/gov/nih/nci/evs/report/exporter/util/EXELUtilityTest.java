package gov.nih.nci.evs.report.exporter.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.Qualifier;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;

class EXELUtilityTest {
	
	ExcelUtility util;
	
	@Mock
	BranchResolutionService service;
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@BeforeEach
	void setUp() throws Exception {
		//Mockito.when(
			//	service.resolveChildEntityGraph(Mockito.anyString(), Mockito., Mockito.anyList())).
		service = mock(BranchResolutionService.class);
		util = new ExcelUtility();
	}

	@Test
	void testProduceCSVOutputFromListWithHeading() throws IOException {
		assertNotNull(util.produceExcelOutputFromListWithHeading(getRestEntityList()));
	}
	
	private List<RestEntity> getRestEntityList() {
		List<RestEntity> list = new ArrayList<RestEntity>();
		
		RestEntity ent = new RestEntity();
		ent.setCode("C123234");
		ent.setName("Myent");
		ent.setTerminology("ncit");
		
		List<Synonym> syns = new ArrayList<Synonym>();
		Synonym syn = new Synonym();
		syn.setType("synType");
		syn.setName("synName");
		syn.setSource("NCIt");
		syn.setSubSource("CDISC");
		syn.setTermGroup("mytermgr");
		Synonym syn2 = new Synonym();
		syn2.setSource("synSource2");
		syn2.setSubSource("NCI");
		syn2.setTermGroup("atermgrp");
		syn2.setName("synName2");
		syns.add(syn);
		syns.add(syn2);
		ent.setSynonyms(syns);
		
		List<Definition> defs = new ArrayList<Definition>();
		Definition def = new Definition();
		def.setType("defType");
		def.setDefinition("defvalue");
		def.setSource("NCI");
		Definition def2 = new Definition();
		def2.setType("defType2");
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
		props2.add(prop2);
		props2.add(prop22);
		props2.add(prop32);
		ent2.setProperties(props9);
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

	@Test
	void testProduceChildCSVOutputFromListWithHeading() throws IOException {
		assertNotNull(util.produceChildExcelOutputFromListWithHeading(getChildEntityList()));
	}
	
	private List<ChildEntity> getChildEntityList() {
		ChildEntity entity = new ChildEntity();
		List<ChildEntity> list = new ArrayList<ChildEntity>();
		List<ChildEntity> children = new ArrayList<ChildEntity>();
		List<ChildEntity> grandChildren = new ArrayList<ChildEntity>();
		List<ChildEntity> grandChildren2 = new ArrayList<ChildEntity>();

		ChildEntity child1 = new ChildEntity();
		child1.setCode("C00001");
		child1.setName("child1");
		child1.setLeaf(false);
		child1.setLevel("1");
		child1.setChildren(grandChildren);
		ChildEntity child2 = new ChildEntity();
		child2.setCode("C00002");
		child2.setName("child2");
		child2.setLeaf(true);
		child2.setLevel("1");
		ChildEntity child3 = new ChildEntity();
		child3.setCode("C00003");
		child3.setName("child3");
		child3.setLeaf(false);
		child3.setLevel("1");
		child3.setChildren(grandChildren2);
		ChildEntity grandchild1 = new ChildEntity();
		grandchild1.setCode("C00011");
		grandchild1.setName("grandchild1");
		grandchild1.setLeaf(true);
		grandchild1.setLevel("2");
		ChildEntity grandchild2 = new ChildEntity();
		grandchild2.setCode("C00012");
		grandchild2.setName("grandchild1");
		grandchild2.setLeaf(true);
		grandchild2.setLevel("2");
		ChildEntity grandchild3 = new ChildEntity();
		grandchild3.setCode("C00021");
		grandchild3.setName("grandchild3");
		grandchild3.setLeaf(true);
		grandchild3.setLevel("2");
		
		grandChildren.add(grandchild1);
		grandChildren.add(grandchild2);
		
		grandChildren2.add(grandchild3);
		
		children.add(child1);
		children.add(child2);
		children.add(child3);
		
		entity.setCode("C00000");
		entity.setName("parent");
		entity.setLeaf(false);
		entity.setLevel("0");
		entity.setChildren(children);
		List<Root> roots = new ArrayList<Root>();
		Root r1 = new Root();
		r1.setCode("C23423");
		r1.setName("Bird");
		Root r2 = new Root();
		r2.setCode("C8988");
		r2.setName("dog");
		Root r3 = new Root();
		r3.setCode("C534");
		r3.setName("Cat");
		roots.add(r1);
		roots.add(r2);
		roots.add(r3);
		
		doAnswer(invocation -> {
			  Object[] args = invocation.getArguments();
			  ((ChildEntity)args[1]).setParents(roots);
			  return null; // void method in a block-style lambda, so return null
			}).when(service).resolveChildEntityGraph(Mockito.anyString(), Mockito.any(ChildEntity.class), Mockito.anyList());;
		service.resolveChildEntityGraph(CommonServices.TOP_NODE, entity, list);
		return list;
	}
	
}
