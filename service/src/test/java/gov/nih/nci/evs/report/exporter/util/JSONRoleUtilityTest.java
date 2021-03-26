package gov.nih.nci.evs.report.exporter.util;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.ExporterQueryResponse;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.Qualifier;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.service.FormattedOutputService;
import gov.nih.nci.evs.report.exporter.service.RoleService;

class JSONRoleUtilityTest {
	
	String codes = "C423423,C23423,C5546456,C5645";
	String roles = "Anatomic_Structure_Is_Physical_Part_Of,Gene_Product_Plays_Role_In_Biological_Process";
	int level = 0;
	
	private RoleService service;
	FormattedOutputService ouputService;

	@BeforeEach
	void setUp() throws Exception {
		service = new RoleService();
		
	}
	
	@Test 
	void testRoles(){
		ExporterQueryResponse response = new GsonBuilder().create().fromJson(
				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),
				roles,codes),  
				new TypeToken<ExporterQueryResponse>(){}.getType());
		
		List<RestEntity> list = response.getEntities();
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getRoles() != null);
		assertTrue(ent1.getRoles().size() > 0);
		
		assertEquals(response.getInput(),codes);
		assertEquals(response.getPropertiesSelected(),roles);
		assertEquals(response.getHierarchyLevel(), level);
		assertEquals(ExporterQueryResponse.REPORT_SEARCH_PARAMETERS,"Report Search Parameters");
	}
	
	@Test 
	void testDefinitionsAndSyns(){
		ExporterQueryResponse response = new GsonBuilder().create().fromJson(
				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),roles,codes),  
				new TypeToken<ExporterQueryResponse>(){}.getType());
		List<RestEntity> list = response.getEntities();
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getDefinitions() != null);
		assertTrue(ent1.getDefinitions().size() > 0);
		assertTrue(ent1.getRoles() != null);
		assertTrue(ent1.getRoles().size() > 0);
		}
	
	@Test 
	void testMaps(){
		ExporterQueryResponse response = new GsonBuilder().create().fromJson(
				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),roles,codes),
				new TypeToken<ExporterQueryResponse>(){}.getType());
		List<RestEntity> list = response.getEntities();
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		assertTrue(ent1.getMaps() != null);
		assertTrue(ent1.getMaps().size() > 0);
		}
	
	@Test
	void testProps() {
		ExporterQueryResponse response = new GsonBuilder().create().fromJson(
				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),roles,codes),  
				new TypeToken<ExporterQueryResponse>(){}.getType());
		List<RestEntity> list = response.getEntities();

		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		assertTrue(ent1.getProperties() != null);
		assertTrue(ent1.getProperties().size() > 0);
	
	}

	@Test
	void testSynsAndOnePropType() {
		ExporterQueryResponse response = new GsonBuilder().create().fromJson(
				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),roles,codes),  
				new TypeToken<ExporterQueryResponse>(){}.getType());
		List<RestEntity> list = response.getEntities();

		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getRoles() != null);
		assertEquals(ent1.getRoles().size(), 4);
		assertTrue(ent1.getDefinitions() != null);
		assertEquals(ent1.getDefinitions().size(), 2);
		assertTrue(ent1.getMaps() == null);
		assertTrue(ent1.getProperties() != null);
		assertTrue(ent1.getProperties().size() > 0);
		assertTrue(ent1
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Chemical_Or_Drug_Has_Mechanism_Of_Action")));
		assertTrue(ent1
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Allele_Has_Activity")));
		assertTrue(ent1
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Associated_With_Disease")));		

		
		RestEntity ent2 = list.stream().filter(x -> x.getCode().equals("C000000")).findAny().get();
		
		assertTrue(ent2.getSynonyms() == null);
		assertTrue(ent2.getDefinitions() == null);
		assertTrue(ent2.getMaps() == null);
		assertTrue(ent2.getRoles() != null);
		assertTrue(ent2.getRoles().size() == 4);
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Chemical_Or_Drug_Has_Mechanism_Of_Action")));
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Allele_Has_Activity")));
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Associated_With_Disease")));
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Procedure_Has_Target_Disease")));
		assertFalse(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Found_In_Organism")));
		
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
		assertTrue(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Chemical_Or_Drug_Has_Mechanism_Of_Action")));
		assertTrue(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Allele_Has_Activity")));
		assertTrue(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Is_Biomarker_Type")));	
		assertFalse(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Found_In_Organism")));
		
RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		
		assertTrue(ent4.getSynonyms() == null);
		assertTrue(ent4.getDefinitions() == null);
		assertTrue(ent4.getMaps() != null);
		assertTrue(ent4.getMaps().size() > 0);
		assertTrue(ent4.getProperties() != null);
		assertTrue(ent4.getProperties().size() > 0);
		assertTrue(ent4.getRoles() != null);
		assertTrue(ent4.getRoles().size() > 0);
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
		assertTrue(ent4
				.getProperties()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Prop2Type")));
		assertTrue(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Found_In_Shoe")));
		assertFalse(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Found_In_Organism")));	
		assertFalse(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Allele_Has_Activity")));	
		assertFalse(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Procedure_Has_Target_Disease")));	
		
		
	}
	
	@Test 
	void testAllRoles(){
		ExporterQueryResponse response = new GsonBuilder().create().fromJson(
				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),roles,codes),  
				new TypeToken<ExporterQueryResponse>(){}.getType());
//						"Maps_To,FULL_SYN,DEFINITION,"
//								+ "ALT_DEFINITION,PropType,PropType2,"
//								+ "Prop0Type,GO_Annotation,"
//								+ "Prop9Type,Prop9Type2")),	"Maps_To,FULL_SYN,DEFINITION,"
//										+ "ALT_DEFINITION,PropType,PropType2,"
//										+ "Prop0Type,GO_Annotation,"
//										+ "Prop9Type,Prop9Type2",codes,level),  
//				new TypeToken<ExporterQueryResponse>(){}.getType());
		List<RestEntity> list = response.getEntities();
		
		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
		assertTrue(ent1.getDefinitions() != null);
		assertTrue(ent1.getDefinitions().size() > 0);
		assertTrue(ent1.getMaps() == null);
		assertTrue(ent1.getProperties() != null);
		assertTrue(ent1.getProperties().size() > 0);
		assertTrue(ent1.getRoles() != null);
		assertTrue(ent1.getRoles().size() > 0);
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
		assertTrue(ent2.getRoles() != null);
		assertTrue(ent2.getRoles().size() > 0);
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Chemical_Or_Drug_Has_Mechanism_Of_Action")));
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Allele_Has_Activity")));
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Procedure_Has_Target_Disease")));
		assertTrue(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Associated_With_Disease")));
		assertFalse(ent2
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Is_Biomarker_Type")));
		
		
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
		assertTrue(ent3.getRoles() != null);
		assertTrue(ent3.getRoles().size() > 0);
		assertTrue(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Chemical_Or_Drug_Has_Mechanism_Of_Action")));
		assertTrue(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Allele_Has_Activity")));
		assertTrue(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Is_Biomarker_Type")));
		assertFalse(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Associated_With_Disease\"")));
		assertFalse(ent3
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropProcedure_Has_Target_Disease")));
		
		
RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
		
		assertTrue(ent4.getSynonyms() == null);
		assertTrue(ent4.getDefinitions() == null);
		assertTrue(ent4.getMaps() != null);
		assertTrue(ent4.getMaps().size() > 0);
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
		assertTrue(ent4.getRoles() != null);
		assertTrue(ent4.getRoles().size() > 0);
		assertTrue(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Gene_Found_In_Shoe")));
		assertFalse(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("PropProcedure_Has_Target_Disease")));
		assertFalse(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Chemical_Or_Drug_Has_Mechanism_Of_Action")));
		assertFalse(ent4
				.getRoles()
				.stream()
				.anyMatch(x -> 
					x.getType().equals("Allele_Has_Activity")));
		
		
	
	}
	
//	@Test 
//	void testAllClassPropsNoProps(){
//		
//		ExporterQueryResponse response = new GsonBuilder().create().fromJson(
//				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),roles,codes),  
//				new TypeToken<ExporterQueryResponse>(){}.getType()); 
//		List<RestEntity> list = response.getEntities();
//		
//		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
//		assertTrue(ent1.getSynonyms() != null);
//		assertTrue(ent1.getSynonyms().size() > 0);
//		assertTrue(ent1.getDefinitions() != null);
//		assertTrue(ent1.getDefinitions().size() > 0);
//		assertTrue(ent1.getMaps() == null);
//		assertTrue(ent1.getProperties() != null);
//		assertTrue(ent1.getProperties().size() == 0);
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType")));
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType2")));
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type2")));		
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop0Type")));
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("GO_Annotation")));	
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type")));	
//		
//		RestEntity ent2 = list.stream().filter(x -> x.getCode().equals("C000000")).findAny().get();
//		
//		assertTrue(ent2.getSynonyms() == null);
//		assertTrue(ent2.getDefinitions() == null);
//		assertTrue(ent2.getMaps() == null);
//		assertTrue(ent2.getProperties() != null);
//		assertTrue(ent2.getProperties().size() == 0);
//		assertFalse(ent2
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop0Type")));
//		assertFalse(ent2
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("GO_Annotation")));
//		
//		RestEntity ent3 = list.stream().filter(x -> x.getCode().equals("C999999")).findAny().get();
//		
//		assertTrue(ent3.getSynonyms() == null);
//		assertTrue(ent3.getDefinitions() == null);
//		assertTrue(ent3.getMaps() == null);
//		assertTrue(ent3.getProperties() != null);
//		assertTrue(ent3.getProperties().size() == 0);
//		assertFalse(ent3
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type")));
//		assertFalse(ent3
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type2")));
//		assertFalse(ent3
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType")));	
//		
//RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
//		
//		assertTrue(ent4.getSynonyms() == null);
//		assertTrue(ent4.getDefinitions() == null);
//		assertTrue(ent4.getMaps() != null);
//		assertTrue(ent4.getMaps().size() > 0);
//		assertTrue(ent4.getProperties() != null);
//		assertTrue(ent4.getProperties().size() == 0);
//		assertFalse(ent4
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop2Type")));
//		assertFalse(ent4
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop2Type2")));
//		assertFalse(ent4
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType")));	
//		
//	
//	}
//	
//	@Test 
//	void testAllNoClassPropsAllProps(){
//		List<RestEntity> list = new GsonBuilder().create().fromJson(
//				new JSONUtility().produceJsonRoleOutputFromListWithHeading(getRestRoleEntityList(),roles,codes),  
//				new TypeToken<ExporterQueryResponse>(){}.getType());
////						"PropType,PropType2,"
////						+ "Prop0Type,GO_Annotation,"
////						+ "Prop9Type,Prop9Type2,"
////						+ "Prop2Type,Prop2Type2"))), 
////				new TypeToken<List<RestEntity>>(){}.getType());
//		
//		RestEntity ent1 = list.stream().filter(x -> x.getCode().equals("C123234")).findAny().get();
//		assertTrue(ent1.getSynonyms() != null);
//		assertTrue(ent1.getSynonyms().size() == 0);
//		assertTrue(ent1.getDefinitions() != null);
//		assertTrue(ent1.getDefinitions().size() == 0);
//		assertTrue(ent1.getMaps() == null);
//		assertTrue(ent1.getProperties() != null);
//		assertTrue(ent1.getProperties().size() > 0);
//		assertTrue(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType")));
//		assertTrue(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType2")));
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type2")));		
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop0Type")));
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("GO_Annotation")));	
//		assertFalse(ent1
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type")));	
//		
//		RestEntity ent2 = list.stream().filter(x -> x.getCode().equals("C000000")).findAny().get();
//		
//		assertTrue(ent2.getSynonyms() == null);
//		assertTrue(ent2.getDefinitions() == null);
//		assertTrue(ent2.getMaps() == null);
//		assertTrue(ent2.getProperties() != null);
//		assertTrue(ent2.getProperties().size() > 0);
//		assertTrue(ent2
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop0Type")));
//		assertTrue(ent2
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("GO_Annotation")));
//		
//		RestEntity ent3 = list.stream().filter(x -> x.getCode().equals("C999999")).findAny().get();
//		
//		assertTrue(ent3.getSynonyms() == null);
//		assertTrue(ent3.getDefinitions() == null);
//		assertTrue(ent3.getMaps() == null);
//		assertTrue(ent3.getProperties() != null);
//		assertTrue(ent3.getProperties().size() > 0);
//		assertTrue(ent3
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type")));
//		assertTrue(ent3
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop9Type2")));
//		assertTrue(ent3
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType")));	
//		
//RestEntity ent4 = list.stream().filter(x -> x.getCode().equals("C2222")).findAny().get();
//		
//		assertTrue(ent4.getSynonyms() == null);
//		assertTrue(ent4.getDefinitions() == null);
//		assertTrue(ent4.getMaps() != null);
//		assertTrue(ent4.getMaps().size() == 0);
//		assertTrue(ent4.getProperties() != null);
//		assertTrue(ent4.getProperties().size() > 0);
//		assertTrue(ent4
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop2Type")));
//		assertTrue(ent4
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("Prop2Type2")));
//		assertTrue(ent4
//				.getProperties()
//				.stream()
//				.anyMatch(x -> 
//					x.getType().equals("PropType")));	
//		
//	
//	}
	
	
	private List<RestEntity> getRestRoleEntityList() {
		
		//Configure an entity with roles
		List<RestEntity> list = new ArrayList<RestEntity>();
		RestEntity ent = new RestEntity();
		ent.setCode("C123234");
		ent.setName("Myent");
		ent.setTerminology("ncit");
		
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("Chemical_Or_Drug_Has_Mechanism_Of_Action");
		role.setRelatedCode("roleName");
		role.setRelatedName("NCIt");

		Role role2 = new Role();
		role2.setType("Chemical_Or_Drug_Has_Mechanism_Of_Action");
		role2.setRelatedCode("roleName");
		role2.setRelatedName("NCIt");
		
		Role role3 = new Role();
		role3.setType("Allele_Has_Activity");
		role3.setRelatedCode("roleName");
		role3.setRelatedName("NCIt");

		Role role4 = new Role();
		role4.setType("Gene_Associated_With_Disease");
		role4.setRelatedCode("roleName");
		role4.setRelatedName("NCIt");
		
		roles.add(role);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		ent.setRoles(roles);
		
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
		
		//Configure an entity with roles
		RestEntity ent1 = new RestEntity();
		ent1.setCode("C000000");
		ent1.setName("0ent");
		ent1.setTerminology("ncit");

		List<Role> roles1 = new ArrayList<Role>();
		Role rolea = new Role();
		rolea.setType("Chemical_Or_Drug_Has_Mechanism_Of_Action");
		rolea.setRelatedCode("roleName");
		rolea.setRelatedName("NCIt");

		Role role2a = new Role();
		role2a.setType("Allele_Has_Activity");
		role2a.setRelatedCode("roleName");
		role2a.setRelatedName("NCIt");
		
		Role role3a = new Role();
		role3a.setType("Gene_Associated_With_Disease");
		role3a.setRelatedCode("roleName");
		role3a.setRelatedName("NCIt");

		Role role4a = new Role();
		role4a.setType("Procedure_Has_Target_Disease");
		role4a.setRelatedCode("roleName");
		role4a.setRelatedName("NCIt");
		
		roles1.add(rolea);
		roles1.add(role2a);
		roles1.add(role3a);
		roles1.add(role4a);
		ent1.setRoles(roles1);
		
		
		//Configure an entity with roles
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
		
		List<Role> roles2 = new ArrayList<Role>();
		Role roleb = new Role();
		roleb.setType("Chemical_Or_Drug_Has_Mechanism_Of_Action");
		roleb.setRelatedCode("roleName");
		roleb.setRelatedName("NCIt");

		Role role2b = new Role();
		role2b.setType("Chemical_Or_Drug_Has_Mechanism_Of_Action");
		role2b.setRelatedCode("roleName");
		role2b.setRelatedName("NCIt");
		
		Role role3b = new Role();
		role3b.setType("Allele_Has_Activity");
		role3b.setRelatedCode("roleName");
		role3b.setRelatedName("NCIt");

		Role role4b = new Role();
		role4b.setType("Gene_Is_Biomarker_Type");
		role4b.setRelatedCode("roleName");
		role4b.setRelatedName("NCIt");
		
		roles2.add(roleb);
		roles2.add(role2b);
		roles2.add(role3b);
		roles2.add(role4b);
		ent9.setRoles(roles2);
		
		//Configure an entity with roles
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
		
		List<Role> roles3 = new ArrayList<Role>();
		Role rolec = new Role();
		rolec.setType("Gene_Found_In_Shoe");
		rolec.setRelatedCode("roleName");
		rolec.setRelatedName("NCIt");

		Role role2c = new Role();
		role2c.setType("Gene_Found_In_Shoe");
		role2c.setRelatedCode("roleName");
		role2c.setRelatedName("NCIt");
		
		Role role3c = new Role();
		role3c.setType("Gene_Found_In_Shoe");
		role3c.setRelatedCode("roleName");
		role3c.setRelatedName("NCIt");

		Role role4c = new Role();
		role4c.setType("Gene_Found_In_Shoe");
		role4c.setRelatedCode("roleName");
		role4c.setRelatedName("NCIt");
		
		roles3.add(rolec);
		roles3.add(role2c);
		roles3.add(role3c);
		roles3.add(role4c);
		ent2.setRoles(roles3);
		

		list.add(ent);
		list.add(ent1);
		list.add(ent9);
		list.add(ent2);
		return list;
	}	

}
