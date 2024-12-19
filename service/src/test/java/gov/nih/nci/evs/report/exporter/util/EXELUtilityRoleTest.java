package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import gov.nih.nci.evs.report.exporter.model.Definition;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyMap;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

class EXELUtilityRoleTest {
	
	ExcelUtility util;
	
	
	String excelOutLine1 = "concept code,concept name,type,target name,target code";
	
	String roles = "Chemical_Or_Drug_Has_Mechanism_Of_Action,Allele_Has_Activity,Gene_Associated_With_Disease,Procedure_Has_Target_Disease,GGene_Is_Biomarker_Type,Gene_Found_In_Shoe";
	

	Workbook wb;
	
	CodeReadService svc;
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@BeforeEach
	void setUp() throws Exception {
		util = new ExcelUtility();
		svc = new CodeReadService();
	}

	@Test
	void testProduceOutputFromListWithHeading() throws IOException {
		assertNotNull(util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), roles, "C123456"));
	}
	
	@Test
	void testProduceOutputFromListWithHeadingStreamedToPOIObject() throws IOException {

		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), roles, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index) != null?row.getCell(index).getStringCellValue():"empty");
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);
			Row row5 = sheet.getRow(5);
			Row row6 = sheet.getRow(6);
			Row row7 = sheet.getRow(7);
			Row row8 = sheet.getRow(8);
			Row row9 = sheet.getRow(9);
			Row row10 = sheet.getRow(10);
			Row row11 = sheet.getRow(11);
			Row row12 = sheet.getRow(12);
			Row row13 = sheet.getRow(13);
			Row row14 = sheet.getRow(14);
			Row row15 = sheet.getRow(15);
			Row row16 = sheet.getRow(16);
			
			
			String header1 = headerRow.getCell(0).getStringCellValue();
			String cell1 = row1.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code");  
			assertEquals(cell1,"C123234");
			
			String header2 = headerRow.getCell(1).getStringCellValue();
			String cell2 = row1.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell2,"Myent");
			
			String header3 = headerRow.getCell(2).getStringCellValue();
			String cell3 = row1.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell3,"Chemical_Or_Drug_Has_Mechanism_Of_Action");
			
			String header4 = headerRow.getCell(3).getStringCellValue();
			String cell4 = row1.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cell4,"code of target");
			
			String header5 = headerRow.getCell(4).getStringCellValue();
			String cell5 = row1.getCell(4).getStringCellValue();
			assertEquals(header5,"target name"); 
			assertEquals(cell5,"name of target");
			
			
			
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cell0a,"C123234");
			
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell1a,"Myent");
			
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell2a,"Chemical_Or_Drug_Has_Mechanism_Of_Action");
			

			String cell3a = row2.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code");
			assertEquals(cell3a,"code of target");

			String cell4a = row2.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cell4a,"name of target");
			


			String cell0b = row3.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cell0b,"C123234");
			
	
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell1b,"Myent");
			

			String cell2b = row3.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell2b,"Allele_Has_Activity");
			

			String cell3b = row3.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cell3b,"code of target");
			

			String cell4b = row3.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cell4b,"name of target");
			


			String cell0c = row4.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cell0c,"C123234");
			
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell1c,"Myent");
			
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell2c,"Gene_Associated_With_Disease");
			
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cell3c,"code of target");
			
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cell4c,"name of target");
			
			
			

			String cell1x = row5.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code");  
			assertEquals(cell1x,"C000000");
			

			String cell2x = row5.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell2x,"0ent");
			

			String cell3x = row5.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell3x,"Chemical_Or_Drug_Has_Mechanism_Of_Action");
			

			String cell4x = row5.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cell4x,"code of target");
			

			String cell5x = row5.getCell(4).getStringCellValue();
			assertEquals(header5,"target name"); 
			assertEquals(cell5x,"name of target");
			
			
			

			String cell0z = row6.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cell0z,"C000000");
			

			String cell1z = row6.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell1z,"0ent");
			

			String cell2z = row6.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell2z,"Allele_Has_Activity");
			

			String cell3z = row6.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code");
			assertEquals(cell3z,"code of target");
			

			String cell4z = row6.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cell4z,"name of target");

			

			String cell0y = row7.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cell0y,"C000000");
			

			String cell1y = row7.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell1y,"0ent");
			

			String cell2y = row7.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell2y,"Gene_Associated_With_Disease");
			

			String cell3y = row7.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cell3y,"code of target");
			

			String cell4y = row7.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cell4y,"name of target");

			

			String cell0w = row8.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cell0w,"C000000");
			

			String cell1w = row8.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cell1w,"0ent");
			

			String cell2w = row8.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cell2w,"Procedure_Has_Target_Disease");
			

			String cell3w = row8.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cell3w,"code of target");
			

			String cell4w = row8.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cell4w,"name of target");
			


			String cella = row9.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code");  
			assertEquals(cella,"C999999");
			

			String cellb = row9.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cellb,"My9");
			

			String cellc = row9.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellc,"Chemical_Or_Drug_Has_Mechanism_Of_Action");
			

			String celld = row9.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(celld,"code of target");
			

			String celle = row9.getCell(4).getStringCellValue();
			assertEquals(header5,"target name"); 
			assertEquals(celle,"name of target");
			
			
			
			String cellf = row10.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cellf,"C999999");
			
			String cellg = row10.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cellg,"My9");
			
			String cellh = row10.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellh,"Chemical_Or_Drug_Has_Mechanism_Of_Action");
			

			String celli = row10.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code");
			assertEquals(celli,"code of target");

			String cellj = row10.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cellj,"name of target");
			


			String cellk = row11.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cellk,"C999999");
			
	
			String celll = row11.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(celll,"My9");
			

			String cellm = row11.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellm,"Allele_Has_Activity");
			

			String celln = row11.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(celln,"code of target");
			

			String cello = row11.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cello,"name of target");
			


			String cellp = row12.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cellp,"C999999");
			
			String cellq = row12.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cellq,"My9");
			
			String cellr = row12.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellr,"Gene_Is_Biomarker_Type");
			
			String cell = row12.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cell,"code of target");
			
			String cellt = row12.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cellt,"name of target");

			
			String cella1 = row13.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code");  
			assertEquals(cella1,"C2222");
			

			String cellb1 = row13.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cellb1,"My2");
			

			String cellc1 = row13.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellc1,"Gene_Found_In_Shoe");
			

			String celld1 = row13.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(celld1,"code of target");
			

			String celle1 = row13.getCell(4).getStringCellValue();
			assertEquals(header5,"target name"); 
			assertEquals(celle1,"name of target");
			
			
			
			String cellf1 = row14.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cellf1,"C2222");
			
			String cellg1 = row14.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cellg1,"My2");
			
			String cellh1 = row14.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellh1,"Gene_Found_In_Shoe");
			

			String celli1 = row14.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code");
			assertEquals(celli1,"code of target");

			String cellj1 = row14.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cellj1,"name of target");
			


			String cellk1 = row15.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cellk1,"C2222");
			
	
			String celll1 = row15.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(celll1,"My2");
			

			String cellm1 = row15.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellm1,"Gene_Found_In_Shoe");
			

			String celln1 = row15.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(celln1,"code of target");
			

			String cello1 = row15.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cello1,"name of target");
			


			String cellp1 = row16.getCell(0).getStringCellValue();	
			assertEquals(header1,"concept code"); 
			assertEquals(cellp1,"C2222");
			
			String cellq1 = row16.getCell(1).getStringCellValue();	
			assertEquals(header2,"concept name"); 
			assertEquals(cellq1,"My2");
			
			String cellr1 = row16.getCell(2).getStringCellValue();	
			assertEquals(header3,"role"); 
			assertEquals(cellr1,"Gene_Found_In_Shoe");
			
			String cellnn1 = row16.getCell(3).getStringCellValue();	
			assertEquals(header4,"target code"); 
			assertEquals(cellnn1,"code of target");
			
			String cellt1 = row16.getCell(4).getStringCellValue();	
			assertEquals(header5,"target name"); 
			assertEquals(cellt1,"name of target");

		stream.close();
		workbook.close();
	}
	
	@Test
//	
	
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
		role.setRelatedCode("code of target");
		role.setRelatedName("name of target");

		Role role2 = new Role();
		role2.setType("Chemical_Or_Drug_Has_Mechanism_Of_Action");
		role2.setRelatedCode("code of target");
		role2.setRelatedName("name of target");
		
		Role role3 = new Role();
		role3.setType("Allele_Has_Activity");
		role3.setRelatedCode("code of target");
		role3.setRelatedName("name of target");

		Role role4 = new Role();
		role4.setType("Gene_Associated_With_Disease");
		role4.setRelatedCode("code of target");
		role4.setRelatedName("name of target");
		
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
		rolea.setRelatedCode("code of target");
		rolea.setRelatedName("name of target");

		Role role2a = new Role();
		role2a.setType("Allele_Has_Activity");
		role2a.setRelatedCode("code of target");
		role2a.setRelatedName("name of target");
		
		Role role3a = new Role();
		role3a.setType("Gene_Associated_With_Disease");
		role3a.setRelatedCode("code of target");
		role3a.setRelatedName("name of target");

		Role role4a = new Role();
		role4a.setType("Procedure_Has_Target_Disease");
		role4a.setRelatedCode("code of target");
		role4a.setRelatedName("name of target");
		
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
		roleb.setRelatedCode("code of target");
		roleb.setRelatedName("name of target");

		Role role2b = new Role();
		role2b.setType("Chemical_Or_Drug_Has_Mechanism_Of_Action");
		role2b.setRelatedCode("code of target");
		role2b.setRelatedName("name of target");
		
		Role role3b = new Role();
		role3b.setType("Allele_Has_Activity");
		role3b.setRelatedCode("code of target");
		role3b.setRelatedName("name of target");

		Role role4b = new Role();
		role4b.setType("Gene_Is_Biomarker_Type");
		role4b.setRelatedCode("code of target");
		role4b.setRelatedName("name of target");
		
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
		rolec.setRelatedCode("code of target");
		rolec.setRelatedName("name of target");

		Role role2c = new Role();
		role2c.setType("Gene_Found_In_Shoe");
		role2c.setRelatedCode("code of target");
		role2c.setRelatedName("name of target");
		
		Role role3c = new Role();
		role3c.setType("Gene_Found_In_Shoe");
		role3c.setRelatedCode("code of target");
		role3c.setRelatedName("name of target");

		Role role4c = new Role();
		role4c.setType("Gene_Found_In_Shoe");
		role4c.setRelatedCode("code of target");
		role4c.setRelatedName("name of target");
		
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

	
	private List<Role> getRoleList() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("C1111");
		role.setRelatedName("target1");
		Role role1 = new Role();
		role1.setType("hasAnyRoleOf");
		role1.setRelatedCode("C2222");
		role1.setRelatedName("target2");
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("C3333");
		role2.setRelatedName("target3");
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("C4444");
		role3.setRelatedName("target4");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("C5555");
		role4.setRelatedName("target5");
		Role role5 = new Role();
		role5.setType("hasRoleOf");
		role5.setRelatedCode("C6666");
		role5.setRelatedName("target6");
		roles.add(role);
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		roles.add(role5);
		return roles;	
	}
	
	private List<Role> getRoleList1() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("C1112");
		role.setRelatedName("target12");
		Role role1 = new Role();
		role1.setType("hasAnyRoleOf");
		role1.setRelatedCode("C2223");
		role1.setRelatedName("target23");
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("C3334");
		role2.setRelatedName("target34");
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("C4445");
		role3.setRelatedName("target45");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("C5556");
		role4.setRelatedName("target56");
		Role role5 = new Role();
		role5.setType("hasUnrelatedRol");
		role5.setRelatedCode("C6667");
		role5.setRelatedName("target67");
		roles.add(role);
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		roles.add(role5);
		return roles;	
	}
	
	private List<Role> getRoleList2() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("C1110");
		role.setRelatedName("target10");
		Role role1 = new Role();
		role1.setType("hasAnyRoleOf");
		role1.setRelatedCode("C2221");
		role1.setRelatedName("target21");
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("C3332");
		role2.setRelatedName("target32");
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("C4443");
		role3.setRelatedName("target43");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("C5554");
		role4.setRelatedName("target54");
		Role role5 = new Role();
		role5.setType("hasRoleOf");
		role5.setRelatedCode("C6665");
		role5.setRelatedName("target65");
		roles.add(role);
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		roles.add(role5);
		return roles;	
		
	}
	
	
}
