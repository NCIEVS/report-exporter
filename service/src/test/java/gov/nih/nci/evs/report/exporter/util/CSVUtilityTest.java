package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;

class CSVUtilityTest {
	
	CSVUtility util;
	
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
		assertEquals("NCIt synType:synName",syn.toString());
	}
	
	@Test
	void testDefinitionOutput() {
		Definition def = new Definition();
		def.setType("defType");
		def.setDefinition("defvalue");
		def.setSource("NCI");
		System.out.println(def.toString());
		assertEquals("NCI defType:defvalue", def.toString());
	}
	
	@Test
	void testPropertyOutput() {
		Property prop = new Property();
		prop.setType("PropType");
		prop.setValue("propvalue");
		System.out.println(prop.toString());
		assertEquals("PropType:propvalue", prop.toString());
	}

	@Test
	void testProduceCSVOutputFromListWithHeading() {
		System.out.println(util.produceCSVOutputFromListWithHeading(getRestEntityList()));
		assertEquals(this.getCSVRestEntityOutput(), util.produceCSVOutputFromListWithHeading(getRestEntityList()));
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
		syn2.setType("synType2");
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
		Property prop2 = new Property();
		prop2.setType("PropType2");
		prop2.setValue("propvalue2");
		props.add(prop);
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
		prop0.setType("Prop0Type2");
		prop0.setValue("prop0value2");
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
		props9.add(prop9);
		props9.add(prop29);
		ent9.setProperties(props9);

		list.add(ent);
		list.add(ent1);
		list.add(ent9);
		return list;
	}

	@Test
	void testProduceChildCSVOutputFromListWithHeading() {
		assertEquals(getChildCSVRestEntityOutput(), util.produceChildCSVOutputFromListWithHeading(getChildEntityList()));
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
	
	private String getCSVRestEntityOutput() {
		return "code,name,terminology,parent,synonyms,definitions,properties" +
				"\r\nC123234,Myent,ncit,null,\"|NCIt synType:synName|NOSOURCE synType2:synName2|\",\"|NCI defType:defvalue|NOSOURCE defType2:defvalue2|\",\"|PropType:propvalue|PropType2:propvalue2|\"" +
				"\r\nC000000,0ent,ncit,null,null,null,\"|Prop0Type:prop0value|Prop0Type2:prop0value2|\"" +
				"\r\nC999999,My9,ncit,null,null,null,\"|Prop9Type:prop9value|Prop9Type2:prop9value2|\"";	    
	}
	
	private String getChildCSVRestEntityOutput() {
		return "code,name,level,parent,leaf,children" +
				"\r\nC00011,grandchild1,2,C00001:child1,true,null" +
				"\r\nC00012,grandchild2,2,C00001:child1,true,null" +
				"\r\nC00001,child1,1,C00000:parent,false,null" +
				"\r\nC00002,child2,1,C00000:parent,true,null" +
				"\r\nC00021,grandchild3,2,C00003:child3,true,null" +
				"\r\nC00003,child3,1,C00000:parent,false,null" +
				"\r\nC00000,parent,0," + CommonServices.TOP_NODE + ",false,null";
	}

}
