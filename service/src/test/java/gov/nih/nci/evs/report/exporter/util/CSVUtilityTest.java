package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.Qualifier;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.model.TypeListAndPositionTuple;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;

class CSVUtilityTest {
	
	CSVUtility util;
	
	String csvOutLine1 = "terminology,code,name,parents,synonyms,definitions,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLine2a = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",\"|NCI:defvalue|NOSOURCE:defvalue2|\",,\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
//	String csvOutLine2b	= "|NCIt CDISC mytermgr:synName ";
//	String csvOutLine2c	=  "|synSource2 NCI atermgrp:synName2 |\"";
//	String csvOutLine2d	= ",\"|NCI:defvalue|NOSOURCE:defvalue2|\",";
//	String csvOutLine2e = "\"|PropType:propvalue|PropType:propvalue1|\",\"|PropType2:propvalue2|\",\r";
	String csvOutLine3 = "ncit,C000000,0ent,,,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"\r";
	String csvOutline4 = "ncit,C999999,My9,,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	String csvOutline5 = "ncit,C2222,My2,,,,\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\",\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	
	BranchResolutionService service;

	@BeforeEach
	void setUp() throws Exception {
		service = new BranchResolutionService();
		util = new CSVUtility();
	}
	
	@Test
	void testSynonymOutput() {
		Synonym syn = new Synonym();
		syn.setType("synType");
		syn.setName("synName");
		syn.setSource("NCIt");
		syn.setSubSource("CDISC");
		syn.setTermGroup("mytermgr");
		System.out.println(syn.toString());	
		assertEquals("NCIt CDISC mytermgr:synName ",syn.toString());
	}
	
	@Test
	void testDefinitionOutput() {
		Definition def = new Definition();
		def.setType("defType");
		def.setDefinition("defvalue");
		def.setSource("NCI");
		System.out.println(def.toString());
		assertEquals("NCI:defvalue", def.toString());
	}
	
	@Test
	void testPropertyOutput() {
		Property prop = new Property();
		prop.setType("PropType");
		prop.setValue("propvalue");
		System.out.println(prop.toString());
		assertEquals("propvalue", prop.toString());
	}

	@Test
	void testProduceCSVOutputFromListWithHeading() {
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList());
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLine1);
		assertEquals(csvLines[1],csvOutLine2a);
		assertEquals(csvLines[2],csvOutLine3);
		assertEquals(csvLines[3],csvOutline4);
		assertEquals(csvLines[4], csvOutline5);
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
		grandchild2.setName("grandchild2");
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
		service.resolveChildEntityGraph(CommonServices.TOP_NODE,entity, list);
		return list;
	}
	
	@Test
	public void TestIterateOnPosition() {
		ConcurrentMap<String, TypeListAndPositionTuple > map = new ConcurrentHashMap<String, TypeListAndPositionTuple>();
		List<Property> props9 = new ArrayList<Property>();
		Property prop9 = new Property();
		prop9.setType("Prop9Type");
		prop9.setValue("prop9value");
		Property prop29 = new Property();
		prop29.setType("Prop9Type");
		prop29.setValue("prop9value2");
		props9.add(prop9);
		props9.add(prop29);
		TypeListAndPositionTuple tuple = new TypeListAndPositionTuple(1, "Prop9Type", props9);
		
		List<Property> props = new ArrayList<Property>();
		Property prop = new Property();
		prop.setType("PropType");
		prop.setValue("propvalue");
		Property prop2 = new Property();
		prop2.setType("PropType");
		prop2.setValue("propvalue2");
		props.add(prop);
		props.add(prop2);
		TypeListAndPositionTuple tuple2 = new TypeListAndPositionTuple(2,"PropType", props);
		
		List<Property> props2 = new ArrayList<Property>();
		Property prop4 = new Property();
		prop4.setType("PropType2");
		prop4.setValue("propvalue2");
		Property prop3 = new Property();
		prop3.setType("PropType2");
		prop3.setValue("propvalue3");
		props.add(prop4);
		props.add(prop3);
		TypeListAndPositionTuple tuple3 = new TypeListAndPositionTuple(0,"PropType2", props2);
		
		map.put("Prop9Type", tuple);
		map.put("PropType", tuple2);
		map.put("PropType2", tuple3);
		
		CommonServices services = new CommonServices();
		Iterator<TypeListAndPositionTuple> itr = services.iterateOnPostion(map);
		while(itr.hasNext()) {System.out.println(itr.next().getPos());}
		
	}
	
//	private String getCSVRestEntityOutput() {
//		return "code,name,terminology,parent,synonyms,definitions,properties" +
//				"\r\nC123234,Myent,ncit,null,\"|NCIt synType:synName|NOSOURCE synType2:synName2|\",\"|NCI defType:defvalue|NOSOURCE defType2:defvalue2|\",\"|PropType:propvalue|PropType2:propvalue2|\"" +
//				"\r\nC000000,0ent,ncit,null,null,null,\"|Prop0Type:prop0value|Prop0Type2:prop0value2|\"" +
//				"\r\nC999999,My9,ncit,null,null,null,\"|Prop9Type:prop9value|Prop9Type2:prop9value2|\"";	    
//	}
//	
//	private String getChildCSVRestEntityOutput() {
//		return "code,name,level,parent,leaf,children" +
//				"\r\nC00011,grandchild1,2,C00001:child1,true,null" +
//				"\r\nC00012,grandchild2,2,C00001:child1,true,null" +
//				"\r\nC00001,child1,1,C00000:parent,false,null" +
//				"\r\nC00002,child2,1,C00000:parent,true,null" +
//				"\r\nC00021,grandchild3,2,C00003:child3,true,null" +
//				"\r\nC00003,child3,1,C00000:parent,false,null" +
//				"\r\nC00000,parent,0," + CommonServices.TOP_NODE + ",false,null";
//	}
	
	

}
