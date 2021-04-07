package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

class TabDelUtilityTest {
	
	TabDelUtility util;
	
	BranchResolutionService service;
	String tabdOutLine1 = "terminology\tcode\tname\tparents\tsynonyms\tPropType\tPropType2\tProp0Type\tGO_Annotation\tProp9Type\tProp9Type2\r";
	String tabdOutLine2 = "ncit\tC123234\tMyent\t\t\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\"\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine2z = "ncit\tC123234\tMyent\t\t\"|synSource2 NCI atermgrp:synName2 |Display_Name:synName |\"\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine3 = "ncit\tC000000\t0ent\t\t\t\t\t\"|prop0value|\"\t\"|GO:0000075 prop0value2:TAS|\"\r";
	String tabdOutline4 = "ncit\tC999999\tMy9\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";
	String tabdOutline5 = "ncit\tC2222\tMy2\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";

	String tabdOutLine1a = "terminology\tcode\tname\tparents\tMaps_To\tPropType\tPropType2\tProp0Type\tGO_Annotation\tProp9Type\tProp9Type2\r";
	String tabdOutLine2a = "ncit\tC123234\tMyent\t\t\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine3a = "ncit\tC000000\t0ent\t\t\t\t\t\"|prop0value|\"\t\"|GO:0000075 prop0value2:TAS|\"\r";
	String tabdOutline4a = "ncit\tC999999\tMy9\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";
	String tabdOutline5a = "ncit\tC2222\tMy2\t\t\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\"\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";

	String tabdOutLine1b = "terminology\tcode\tname\tparents\tdefinitions\tPropType\tPropType2\tProp0Type\tGO_Annotation\tProp9Type\tProp9Type2\r";
	String tabdOutLine2b = "ncit\tC123234\tMyent\t\t\"|NCI:defvalue|defvalue2|\"\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine3b = "ncit\tC000000\t0ent\t\t\t\t\t\"|prop0value|\"\t\"|GO:0000075 prop0value2:TAS|\"\r";
	String tabdOutline4b = "ncit\tC999999\tMy9\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";
	String tabdOutline5b = "ncit\tC2222\tMy2\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";

	
	String tabdOutLine1c = "terminology\tcode\tname\tparents\tsynonyms\tdefinitions\tPropType\tPropType2\tProp0Type\tGO_Annotation\tProp9Type\tProp9Type2\r";
	String tabdOutLine2c = "ncit\tC123234\tMyent\t\t\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\"\t\"|NCI:defvalue|\"\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine3c = "ncit\tC000000\t0ent\t\t\t\t\t\t\"|prop0value|\"\t\"|GO:0000075 prop0value2:TAS|\"\r";
	String tabdOutline4c = "ncit\tC999999\tMy9\t\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";
	String tabdOutline5c = "ncit\tC2222\tMy2\t\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";

	
	String tabdOutLine1d = "terminology\tcode\tname\tparents\tsynonyms\tdefinitions\tPropType\tPropType2\tProp0Type\tGO_Annotation\tProp9Type\tProp9Type2\r";
	String tabdOutLine2d = "ncit\tC123234\tMyent\t\t\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\"\t\"|NCI:defvalue|\"\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine3d = "ncit\tC000000\t0ent\t\t\t\t\t\t\"|prop0value|\"\t\"|GO:0000075 prop0value2:TAS|\"\r";
	String tabdOutline4d = "ncit\tC999999\tMy9\t\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";
	String tabdOutline5d = "ncit\tC2222\tMy2\t\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";

	String tabdOutLine1e = "terminology\tcode\tname\tparents\tdefinitions\tMaps_To\tPropType\tPropType2\tProp0Type\tGO_Annotation\tProp9Type\tProp9Type2\r";
	String tabdOutLine2e = "ncit\tC123234\tMyent\t\t\"|NCI:defvalue|defvalue2|\"\t\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine3e = "ncit\tC000000\t0ent\t\t\t\t\t\t\"|prop0value|\"\t\"|GO:0000075 prop0value2:TAS|\"\r";
	String tabdOutline4e = "ncit\tC999999\tMy9\t\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";
	String tabdOutline5e = "ncit\tC2222\tMy2\t\t\t\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\"\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";

	String tabdOutLine1f = "terminology\tcode\tname\tparents\tsynonyms\tdefinitions\tMaps_To\tPropType\tPropType2\tProp0Type\tGO_Annotation\tProp9Type\tProp9Type2\r";
	String tabdOutLine2f = "ncit\tC123234\tMyent\t\t\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\"\t\"|NCI:defvalue|defvalue2|\"\t\t\"|propvalue|propvalue1|\"\t\"|propvalue2|\"\r";
	String tabdOutLine3f = "ncit\tC000000\t0ent\t\t\t\t\t\t\t\"|prop0value|\"\t\"|GO:0000075 prop0value2:TAS|\"\r";
	String tabdOutline4f = "ncit\tC999999\tMy9\t\t\t\t\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";
	String tabdOutline5f = "ncit\tC2222\tMy2\t\t\t\t\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\"\t\"|prop9value3|\"\t\t\t\t\"|prop9value|\"\t\"|prop9value2|\"\r";

	
	String singleLineHeading = "terminology\tcode\tname\tparents\tsynonyms\tdefinitions\tSemantic_Type\tUMLS_CUI\tContributing_Source";
	String singelLinetbd	 = "ncit\tC61410\tClinical Data Interchange Standards Consortium Terminology\t\"|C54443:Terminology Subset|\"\t\"|NCI PT:Clinical Data Interchange Standards Consortium Terminology |NCI SY:CDISC Terminology |NCI SY:CDISC |\"\t\"|NCI:terms relative to CDISC.|\"\t\t\"|Intellectual Product|\"\t\"|C1880104|\"\t\"|CDISC|\"\r";
	String singelLinetbdNoDefsNoMaps	 = "ncit\tC61410\tClinical Data Interchange Standards Consortium Terminology\t\"|C54443:Terminology Subset|\"\t\"|NCI PT:Clinical Data Interchange Standards Consortium Terminology |NCI SY:CDISC Terminology |NCI SY:CDISC |\"\t\t\t\"|Intellectual Product|\"\t\"|C1880104|\"\t\"|CDISC|\"\r";
	
	CodeReadService crsvc;
	
	@BeforeEach
	void setUp() throws Exception {
		service = new BranchResolutionService();
		crsvc = new CodeReadService();
		util = new TabDelUtility();
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingSyn() {
		String props = "FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(crsvc.getEntitiesForPropertyNameFilter(getRestEntityList(), CommonServices.splitInput(props)), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1);
		assertEquals(tabdLines[1],tabdOutLine2);
		assertEquals(tabdLines[2],tabdOutLine3);
		assertEquals(tabdLines[3],tabdOutline4);
		assertEquals(tabdLines[4],tabdOutline5);
		assertEquals(tabdLines[5], "\r");
		assertEquals(tabdLines[6], "\r");
		assertEquals(tabdLines[7], "\r");
		assertEquals(tabdLines[8], "Report Search Parameters: \r");
		assertEquals(tabdLines[9], "\"|Input:  C123456|\"\r");
		assertEquals(tabdLines[10], "Hierarchy level: 0\r");
		assertEquals(tabdLines[11], "\"|Properties Selected: FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2|\"");
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingSynDn() {
		String props = "Display_Name,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(crsvc.getEntitiesForPropertyNameFilter(getRestEntityList(), CommonServices.splitInput(props)), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1);
		assertEquals(tabdLines[1],tabdOutLine2z);
		assertEquals(tabdLines[2],tabdOutLine3);
		assertEquals(tabdLines[3],tabdOutline4);
		assertEquals(tabdLines[4],tabdOutline5);
		assertEquals(tabdLines[5], "\r");
		assertEquals(tabdLines[6], "\r");
		assertEquals(tabdLines[7], "\r");
		assertEquals(tabdLines[8], "Report Search Parameters: \r");
		assertEquals(tabdLines[9], "\"|Input:  C123456|\"\r");
		assertEquals(tabdLines[10], "Hierarchy level: 0\r");
		assertEquals(tabdLines[11], "\"|Properties Selected: Display_Name,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2|\"");
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingMap() {
		String props = "Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(getRestEntityList(), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1a);
		assertEquals(tabdLines[1],tabdOutLine2a);
		assertEquals(tabdLines[2],tabdOutLine3a);
		assertEquals(tabdLines[3],tabdOutline4a);
		assertEquals(tabdLines[4],tabdOutline5a);
		assertEquals(tabdLines[5], "\r");
		assertEquals(tabdLines[6], "\r");
		assertEquals(tabdLines[7], "\r");
		assertEquals(tabdLines[8], "Report Search Parameters: \r");
		assertEquals(tabdLines[9], "\"|Input:  C123456|\"\r");
		assertEquals(tabdLines[10], "Hierarchy level: 0\r");
		assertEquals(tabdLines[11], "\"|Properties Selected: Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2|\"");
		}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingDef() {
		String props = "DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(getRestEntityList(), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1b);
		assertEquals(tabdLines[1],tabdOutLine2b);
		assertEquals(tabdLines[2],tabdOutLine3b);
		assertEquals(tabdLines[3],tabdOutline4b);
		assertEquals(tabdLines[4],tabdOutline5b);
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingAltDef() {
		String props = "ALT_DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(getRestEntityList(), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1b);
		assertEquals(tabdLines[1],tabdOutLine2b);
		assertEquals(tabdLines[2],tabdOutLine3b);
		assertEquals(tabdLines[3],tabdOutline4b);
		assertEquals(tabdLines[4],tabdOutline5b);
	}
	@Test
	void testProduceTabDelOutputFromListWithHeadingBothDef() {
		String props = "ALT_DEFINITION,DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(getRestEntityList(), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1b);
		assertEquals(tabdLines[1],tabdOutLine2b);
		assertEquals(tabdLines[2],tabdOutLine3b);
		assertEquals(tabdLines[3],tabdOutline4b);
		assertEquals(tabdLines[4],tabdOutline5b);
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingDefSyn() {
		String props = "DEFINITION,FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(crsvc.getEntitiesForPropertyNameFilter(getRestEntityList(), CommonServices.splitInput(props)), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1c);
		assertEquals(tabdLines[1],tabdOutLine2c);
		assertEquals(tabdLines[2],tabdOutLine3c);
		assertEquals(tabdLines[3],tabdOutline4c);
		assertEquals(tabdLines[4],tabdOutline5c);
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingMapsSyn() {
		String props = "DEFINITION,FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(crsvc.getEntitiesForPropertyNameFilter(getRestEntityList(), CommonServices.splitInput(props)), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1d);
		assertEquals(tabdLines[1],tabdOutLine2d);
		assertEquals(tabdLines[2],tabdOutLine3d);
		assertEquals(tabdLines[3],tabdOutline4d);
		assertEquals(tabdLines[4],tabdOutline5d);
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingMapsDef() {
		String props = "DEFINITION,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(getRestEntityList(), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1e);
		assertEquals(tabdLines[1],tabdOutLine2e);
		assertEquals(tabdLines[2],tabdOutLine3e);
		assertEquals(tabdLines[3],tabdOutline4e);
		assertEquals(tabdLines[4],tabdOutline5e);
	}
	
	@Test
	void testProduceTabDelOutputFromListWithHeadingAll() {
		String props = "DEFINITION,ALT_DEFINITION,FULL_SYN,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(crsvc.getEntitiesForPropertyNameFilter(getRestEntityList(), CommonServices.splitInput(props)), props, "C123456", 0 );
		String[] tabdLines = tabd.split(System.lineSeparator());
		assertEquals(tabdLines[0],tabdOutLine1f);
		assertEquals(tabdLines[1],tabdOutLine2f);
		assertEquals(tabdLines[2],tabdOutLine3f);
		assertEquals(tabdLines[3],tabdOutline4f);
		assertEquals(tabdLines[4],tabdOutline5f);
	}
	
	@Test
	void testProduceCSVOutputFromOneListMemberWithInvalidProps() {
		String props = "ALT_DEFINITION,Accepted_Therapeutic_Use_For,CAS_Registry,CHEBI_ID,Chemical_Formula,Concept_Status,Contributing_Source,DEFINITION,Display_Name,EntrezGene_ID,Essential_Amino_Acid,Essential_Fatty_Acid,FDA_UNII_Code,FULL_SYN,GO_Annotation,GenBank_Accession_Number,HGNC_ID,ICD-O-3_Code,INFOODS,KEGG_ID,MGI_Accession_ID,Macronutrient,Maps_To,Micronutrient,NCBI_Taxon_ID,NCI_META_CUI,NSC Number,Neoplastic_Status,Nutrient,OID,OMIM_Number,PDQ_Closed_Trial_Search_ID,PDQ_Open_Trial_Search_ID,PID_ID,Preferred_Name,PubMedID_Primary_Reference,SNP_ID,Semantic_Type,Subsource,Swiss_Prot,Tolerable_Level,UMLS_CUI,USDA_ID,US_Recommended_Intake,Unit,code,miRBase_ID";
		List<RestEntity> entity = new ArrayList<RestEntity>();
		entity.add(getRestEntity());
		String csv = util.produceTabDelOutputFromListWithHeading(entity, props, "C123456", 0 );
		String[] csvLines = csv.split(System.lineSeparator());
		//assertEquals(csvLines[0],singleLineHeading);
		assertEquals(csvLines[1],singelLinetbd);}
	
	@Test
	void testProduceCSVOutputFromOneListMemberWithNoDefsNoMaps() {
		String props = "ALT_DEFINITION,Accepted_Therapeutic_Use_For,CAS_Registry,CHEBI_ID,Chemical_Formula,Concept_Status,Contributing_Source,DEFINITION,Display_Name,EntrezGene_ID,Essential_Amino_Acid,Essential_Fatty_Acid,FDA_UNII_Code,FULL_SYN,GO_Annotation,GenBank_Accession_Number,HGNC_ID,ICD-O-3_Code,INFOODS,KEGG_ID,MGI_Accession_ID,Macronutrient,Maps_To,Micronutrient,NCBI_Taxon_ID,NCI_META_CUI,NSC Number,Neoplastic_Status,Nutrient,OID,OMIM_Number,PDQ_Closed_Trial_Search_ID,PDQ_Open_Trial_Search_ID,PID_ID,Preferred_Name,PubMedID_Primary_Reference,SNP_ID,Semantic_Type,Subsource,Swiss_Prot,Tolerable_Level,UMLS_CUI,USDA_ID,US_Recommended_Intake,Unit,code,miRBase_ID";
		List<RestEntity> entity = new ArrayList<RestEntity>();
		entity.add(getRestEntityWNoDefsNoMaps());
		String csv = util.produceTabDelOutputFromListWithHeading(entity, props, "C123456", 0 );
		String[] csvLines = csv.split(System.lineSeparator());
		//assertEquals(csvLines[0],singleLineHeadingNoDefsNoMaps);
		assertEquals(csvLines[1],singelLinetbdNoDefsNoMaps);}
	
	
	
	@Test 
	void produceOutput() {
		String props = "DEFINITION,ALT_DEF,FULL_SYN,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		String tabd = util.produceTabDelOutputFromListWithHeading(getRestEntityList(), props, "C123456", 0 );
		System.out.println(tabd);
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
		syn2.setSource("synSource2");
		syn2.setSubSource("NCI");
		syn2.setTermGroup("atermgrp");
		syn2.setName("synName2");
		Synonym syn3 = new Synonym();
		syn3.setType("Display_Name");
		syn3.setName("synName");
		syns.add(syn);
		syns.add(syn2);
		syns.add(syn3);
		ent.setSynonyms(syns);
		
		List<Definition> defs = new ArrayList<Definition>();
		Definition def = new Definition();
		def.setType("DEFINITION");
		def.setDefinition("defvalue");
		def.setSource("NCI");
		Definition def2 = new Definition();
		def2.setType("ALT_DEFINITION");
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


public RestEntity getRestEntity() {
	
	RestEntity ent = new RestEntity();
	
	Synonym syn = new Synonym();
	//syn.setCode(code);
	syn.setName("Clinical Data Interchange Standards Consortium Terminology");
	//syn.setSource("Preferred_Name");
	///syn.setTermGroup(termGroup);
	syn.setType( "Preferred_Name");
	Synonym syn1 = new Synonym();
//	syn1.setCode(code);
	syn1.setName("Clinical Data Interchange Standards Consortium Terminology");
	syn1.setSource("NCI");
	syn1.setTermGroup("PT");
	syn1.setType("FULL_SYN");
	Synonym syn2 = new Synonym();
//	syn2.setCode(code);
	syn2.setName("CDISC Terminology");
	syn2.setSource("NCI");
	syn2.setTermGroup("SY");
	syn2.setType("FULL_SYN");
	Synonym syn3 = new Synonym();
//	syn3.setCode(code);
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
//	Property prop3 = new Property();
	//prop3.setQualifiers(qualifiers);
//	prop3.setType("Legacy Concept Name");
//	prop3.setValue("Clinical_Data_Interchange_Standards_Consortium");
//	Property prop4 = new Property();
//	//prop4.setQualifiers(qualifiers);
//	prop4.setType("Publish_Value_Set");
//	prop4.setValue("Yes");
//	Property prop5 = new Property();
//	//prop5.setQualifiers(qualifiers);
//	prop5.setType("Term_Browser_Value_Set_Description");
//	prop5.setValue("tb value");
//	Property prop6 = new Property();
//	//prop6.setQualifiers(qualifiers);
//	prop6.setType("Value_Set_Pair");
//	prop6.setValue("No");
	
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
//	props.add(prop3);
//	props.add(prop4);
//	props.add(prop5);
//	props.add(prop6);
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
//	syn1.setCode(code);
	syn1.setName("Clinical Data Interchange Standards Consortium Terminology");
	syn1.setSource("NCI");
	syn1.setTermGroup("PT");
	syn1.setType("FULL_SYN");
	Synonym syn2 = new Synonym();
//	syn2.setCode(code);
	syn2.setName("CDISC Terminology");
	syn2.setSource("NCI");
	syn2.setTermGroup("SY");
	syn2.setType("FULL_SYN");
	Synonym syn3 = new Synonym();
//	syn3.setCode(code);
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
//	props.add(prop3);
//	props.add(prop4);
//	props.add(prop5);
//	props.add(prop6);
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
		service.resolveChildEntityGraph( entity, list);
		return list;
	}
	
	
	private String getChildTabDelRestEntityOutput() {
		return "code\tname\tlevel\tparent\tleaf\tchildren" +
				"\r\nC00011\tgrandchild1\t2\tC00001:child1\ttrue\tnull" +
				"\r\nC00012\tgrandchild1\t2\tC00001:child1\ttrue\tnull" +
				"\r\nC00001\tchild1\t1\tC00000:parent\tfalse\tnull" +
				"\r\nC00002\tchild2\t1\tC00000:parent\ttrue\tnull" +
				"\r\nC00021\tgrandchild3\t2\tC00003:child3\ttrue\tnull" +
				"\r\nC00003\tchild3\t1\tC00000:parent\tfalse\tnull" +
				"\r\nC00000\tparent\t0\t" + CommonServices.TOP_NODE + "\tfalse\tnull";
	}

}
