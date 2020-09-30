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
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.model.TypeListAndPositionTuple;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;

class CSVUtilityTest {
	
	CSVUtility util;
	
	String csvOutLineHeading = "terminology,code,name,parents,synonyms,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLineHeading1 = "terminology,code,name,parents,definitions,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLineHeading1a = "terminology,code,name,parents,synonyms,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	//String csvOutLineHeading2 = "terminology,code,name,parents,definitions,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLineHeading3 = "terminology,code,name,parents,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLineHeading4 = "terminology,code,name,parents,synonyms,definitions,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLine2a = "ncit,C123234,Myent,,\"|NCI:defvalue|NOSOURCE:defvalue2|\",\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	String csvOutLine2ab = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",,\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	String csvOutLine2ac = "ncit,C123234,Myent,,\"|NCI:defvalue|NOSOURCE:defvalue2|\",,\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	//String csvOutLine2aca = "ncit,C123234,Myent,,\"|NCI:defvalue|NOSOURCE:defvalue2|\",\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	
	String csvOutLine2ad = "ncit,C123234,Myent,,,\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	String csvOutLine2ae = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",\"|NCI:defvalue|NOSOURCE:defvalue2|\",,\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	
	//	String csvOutLine2b	= "|NCIt CDISC mytermgr:synName ";
//	String csvOutLine2c	=  "|synSource2 NCI atermgrp:synName2 |\"";
//	String csvOutLine2d	= ",\"|NCI:defvalue|NOSOURCE:defvalue2|\",";
//	String csvOutLine2e = "\"|PropType:propvalue|PropType:propvalue1|\",\"|PropType2:propvalue2|\",\r";
	String csvOutLine3 = "ncit,C000000,0ent,,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"\r";
	String csvOutLine3a = "ncit,C000000,0ent,,,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"\r";
	//String csvOutLine3b = "ncit,C000000,0ent,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"\r";
	String csvOutline4 = "ncit,C999999,My9,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	//String csvOutline4a = "ncit,C999999,My9,,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	String csvOutline4b = "ncit,C999999,My9,,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	String csvOutline4c = "ncit,C999999,My9,,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	String csvOutline4d = "ncit,C999999,My9,,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	
	String csvOutline5 = "ncit,C2222,My2,,,\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\",\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String csvOutline5b = "ncit,C2222,My2,,,,\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\",\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	//String csvOutline5a = "ncit,C2222,My2,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	
	String csvOutLineHeading2 = "terminology,code,name,parents,definitions,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLine2aca = "ncit,C123234,Myent,,\"|NCI:defvalue|NOSOURCE:defvalue2|\",\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	String csvOutLine3b = "ncit,C000000,0ent,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"\r";
	String csvOutline4a = "ncit,C999999,My9,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	String csvOutline5a = "ncit,C2222,My2,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	
	String csvOutLineHeading3e = "terminology,code,name,parents,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLine2e = "ncit,C123234,Myent,,,\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	String csvOutLine3e = "ncit,C000000,0ent,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"\r";
	String csvOutline4e = "ncit,C999999,My9,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	String csvOutline5e = "ncit,C2222,My2,,\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\",\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	
	String csvOutLineHeadingf = "terminology,code,name,parents,synonyms,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2\r";
	String csvOutLine2f = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",\"|propvalue|propvalue1|\",\"|propvalue2|\"\r";
	String csvOutLine3f = "ncit,C000000,0ent,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"\r";
	String csvOutline4f = "ncit,C999999,My9,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"\r";
	String csvOutline5f = "ncit,C2222,My2,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	
	
	
	String singleLineHeading = "terminology,code,name,parents,synonyms,definitions,Semantic_Type,UMLS_CUI,Contributing_Source";
	String singelLineCSV	 = "ncit,C61410,Clinical Data Interchange Standards Consortium Terminology,\"|C54443:Terminology Subset|\",\"|NCI  PT:Clinical Data Interchange Standards Consortium Terminology |NCI  SY:CDISC Terminology |NCI  SY:CDISC |\",\"|NCI:terms relative to CDISC.|\",\"|Intellectual Product|\",\"|C1880104|\",\"|CDISC|\"";
	
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
	void testProduceCSVOutputFromListWithHeadingSyn() {
		String props = "FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading);
		assertEquals(csvLines[1],csvOutLine2f);
		assertEquals(csvLines[2],csvOutLine3f);
		assertEquals(csvLines[3],csvOutline4f);
		assertEquals(csvLines[4], csvOutline5a);
	}
	
	@Test
	void testProduceCSVOutputFromListWithHeadingMapSyn() {
		String props = "Maps_To,FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading1a);
		assertEquals(csvLines[1],csvOutLine2ab);
		assertEquals(csvLines[2],csvOutLine3);
		assertEquals(csvLines[3],csvOutline4);
		assertEquals(csvLines[4], csvOutline5);
	}
	
	@Test
	void testProduceCSVOutputFromListWithHeadingMap() {
		String props = "Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading3e);
		assertEquals(csvLines[1],csvOutLine2e);
		assertEquals(csvLines[2],csvOutLine3e);
		assertEquals(csvLines[3],csvOutline4e);
		assertEquals(csvLines[4], csvOutline5e);
	}
	
	@Test
	void testProduceCSVOutputFromListWithHeadingMapDefinition() {
		String props = "Maps_To,DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading1);
		assertEquals(csvLines[1],csvOutLine2ac);
		assertEquals(csvLines[2],csvOutLine3);
		assertEquals(csvLines[3],csvOutline4);
		assertEquals(csvLines[4], csvOutline5);
	}
	
	@Test
	void testProduceCSVOutputFromListWithHeadingMapDef2() {
		String props = "Maps_To,ALT_DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading1);
		assertEquals(csvLines[1],csvOutLine2ac);
		assertEquals(csvLines[2],csvOutLine3);
		assertEquals(csvLines[3],csvOutline4);
		assertEquals(csvLines[4], csvOutline5);
	}
	
	@Test
	void testProduceCSVOutputFromListWithHeadingMapDefBoth() {
		String props = "Maps_To,ALT_DEFINITION,ALT_DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading1);
		assertEquals(csvLines[1],csvOutLine2ac);
		assertEquals(csvLines[2],csvOutLine3);
		assertEquals(csvLines[3],csvOutline4);
		assertEquals(csvLines[4], csvOutline5);
	}
	
	@Test
	void testProduceCSVOutputFromListWithHeadingDef() {
		String props = "ALT_DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading2);
		assertEquals(csvLines[1],csvOutLine2aca);
		assertEquals(csvLines[2],csvOutLine3b);
		assertEquals(csvLines[3],csvOutline4a);
		assertEquals(csvLines[4], csvOutline5a);
	}
	
	@Test
	void testProduceCSVOutputFromListWithHeadingAll() {
		String props = "Maps_To,FULL_SYN,DEFINITION,ALT_DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props);
		String[] csvLines = csv.split(System.lineSeparator());
		assertEquals(csvLines[0],csvOutLineHeading4);
		assertEquals(csvLines[1],csvOutLine2ae);
		assertEquals(csvLines[2],csvOutLine3a);
		assertEquals(csvLines[3],csvOutline4d);
		assertEquals(csvLines[4], csvOutline5b);
	}
	// Single line header matching tests
	@Test
	void testProduceCSVOutputFromOneListMemberWithInvalidProps() {
		String props = "ALT_DEFINITION,Accepted_Therapeutic_Use_For,CAS_Registry,CHEBI_ID,Chemical_Formula,Concept_Status,Contributing_Source,DEFINITION,Display_Name,EntrezGene_ID,Essential_Amino_Acid,Essential_Fatty_Acid,FDA_UNII_Code,FULL_SYN,GO_Annotation,GenBank_Accession_Number,HGNC_ID,ICD-O-3_Code,INFOODS,KEGG_ID,MGI_Accession_ID,Macronutrient,Maps_To,Micronutrient,NCBI_Taxon_ID,NCI_META_CUI,NSC Number,Neoplastic_Status,Nutrient,OID,OMIM_Number,PDQ_Closed_Trial_Search_ID,PDQ_Open_Trial_Search_ID,PID_ID,Preferred_Name,PubMedID_Primary_Reference,SNP_ID,Semantic_Type,Subsource,Swiss_Prot,Tolerable_Level,UMLS_CUI,USDA_ID,US_Recommended_Intake,Unit,code,miRBase_ID";
		List<RestEntity> entity = new ArrayList<RestEntity>();
		entity.add(getRestEntity());
		String csv = util.produceCSVOutputFromListWithHeading(entity, props);
		String[] csvLines = csv.split(System.lineSeparator());
		//assertEquals(csvLines[0],singleLineHeading);
		assertEquals(csvLines[1],singelLineCSV);}
	
	// Single line header matching tests
	@Test
	void testSingleLineMatchtoHeadingMapsTo() {
		String props = "FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String csv = util.produceCSVOutputFromListWithHeading(getRestEntityList(), props );
//		String[] csvLines = csv.split(System.lineSeparator());
//		assertEquals(csvLines[0],csvOutLineHeading3);
//		assertEquals(csvLines[1],csvOutLine2ad);
//		assertEquals(csvLines[2],csvOutLine3);
//		assertEquals(csvLines[3],csvOutline4);
//		assertEquals(csvLines[4], csvOutline5);
		System.out.print(csv);
		System.out.flush();
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
