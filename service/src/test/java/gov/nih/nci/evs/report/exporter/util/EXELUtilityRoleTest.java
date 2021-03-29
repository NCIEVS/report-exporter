package gov.nih.nci.evs.report.exporter.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

class EXELUtilityRoleTest {
	
	ExcelUtility util;
	
	
	String excelOutLine1 = "concept code,concept name,type,target name,target code";
	String excelOutLine2 = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",\"|propvalue|propvalue1|\",\"|propvalue2|\"";
	String excelOutLine3 = "ncit,C000000,0ent,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"";
	String excelOutline4 = "ncit,C999999,My9,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String excelOutline5 = "ncit,C2222,My2,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";

	String excelOutLine1a = "terminology,code,name,parents,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
	String excelOutLine2a = "ncit,C123234,Myent,,,\"|propvalue|propvalue1|\",\"|propvalue2|\"";
	String excelOutLine3a = "ncit,C000000,0ent,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"";
	String excelOutline4a = "ncit,C999999,My9,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String excelOutline5a = "ncit,C2222,My2,,\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\",\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";

	String excelOutLine1b = "terminology,code,name,parents,definitions,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
	String excelOutLine2b = "ncit,C123234,Myent,,\"|NCI:defvalue|defvalue2|\",\"|propvalue|propvalue1|\",\"|propvalue2|\"";
	String excelOutLine3b = "ncit,C000000,0ent,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"";
	String excelOutline4b = "ncit,C999999,My9,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String excelOutline5b = "ncit,C2222,My2,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";

	
	String excelOutLine1c = "terminology,code,name,parents,synonyms,definitions,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
	String excelOutLine2c = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",\"|NCI:defvalue|defvalue2|\",\"|propvalue|propvalue1|\",\"|propvalue2|\"";
	String excelOutLine3c = "ncit,C000000,0ent,,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"";
	String excelOutline4c = "ncit,C999999,My9,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String excelOutline5c = "ncit,C2222,My2,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";

	
	String excelOutLine1d = "terminology,code,name,parents,synonyms,definitions,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
	String excelOutLine2d = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",\"|NCI:defvalue|defvalue2|\",\"|propvalue|propvalue1|\",\"|propvalue2|\"";
	String excelOutLine3d = "ncit,C000000,0ent,,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"";
	String excelOutline4d = "ncit,C999999,My9,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String excelOutline5d = "ncit,C2222,My2,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";

	String excelOutLine1e = "terminology,code,name,parents,definitions,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
	String excelOutLine2e = "ncit,C123234,Myent,,\"|NCI:defvalue|defvalue2|\",,\"|propvalue|propvalue1|\",\"|propvalue2|\"";
	String excelOutLine3e = "ncit,C000000,0ent,,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"";
	String excelOutline4e = "ncit,C999999,My9,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String excelOutline5e = "ncit,C2222,My2,,,\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\",\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";

	String excelOutLine1f = "terminology,code,name,parents,synonyms,definitions,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
	String excelOutLine2f = "ncit,C123234,Myent,,\"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |\",\"|NCI:defvalue|defvalue2|\",,\"|propvalue|propvalue1|\",\"|propvalue2|\"";
	String excelOutLine3f = "ncit,C000000,0ent,,,,,,,\"|prop0value|\",\"|GO:0000075 prop0value2:TAS|\"";
	String excelOutline4f = "ncit,C999999,My9,,,,,\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";
	String excelOutline5f = "ncit,C2222,My2,,,,\"|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|\",\"|prop9value3|\",,,,\"|prop9value|\",\"|prop9value2|\"";

	
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
		String props = "FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		assertNotNull(util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456"));
	}
	
	@Test
	void testProduceOutputFromListWithHeadingStreamedToPOIObject() throws IOException {

		String props = "FULL_SYN,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456");

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
			
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertEquals(header0a,"concept code"); 
			assertEquals(cell0a,"C123234");
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertEquals(header1a,"concept name"); 
			assertEquals(cell1a,"Myent");
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertEquals(header2a,"role"); 
			assertEquals(cell2a,"Chemical_Or_Drug_Has_Mechanism_Of_Action");
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertEquals(header3a,"target code");
			assertEquals(cell3a,"code of target");
			
			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertEquals(header4a,"target name"); 
			assertEquals(cell4a,"name of target");

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertEquals(header0b,"concept code"); 
			assertEquals(cell0b,"C123234");
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertEquals(header1b,"concept name"); 
			assertEquals(cell1b,"Myent");
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertEquals(header2b,"role"); 
			assertEquals(cell2b,"Allele_Has_Activity");
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertEquals(header3b,"target code"); 
			assertEquals(cell3b,"code of target");
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertEquals(header4b,"target name"); 
			assertEquals(cell4b,"name of target");

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertEquals(header0c,"concept code"); 
			assertEquals(cell0c,"C123234");
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertEquals(header1c,"concept name"); 
			assertEquals(cell1c,"Myent");
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertEquals(header2c,"role"); 
			assertEquals(cell2c,"Gene_Associated_With_Disease");
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertEquals(header3c,"target code"); 
			assertEquals(cell3c,"code of target");
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertEquals(header4c,"target name"); 
			assertEquals(cell4c,"name of target");


		stream.close();
		workbook.close();
	}
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingMap() throws IOException {

		String props = "Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1a.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

//		for(int i = 0; i < rows; i ++) {
//			Row row = sheet.getRow(i);
//			System.out.println();
//			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
//				System.out.println(row.getCell(index).getStringCellValue());
//			};
//		}
//		

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

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertTrue(header4.equals("Maps_To")  && cell4.equals(""));
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertTrue(header5.equals("PropType")  && cell5.equals("|propvalue|propvalue1|"));
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("PropType2")  && cell6.equals("|propvalue2|"));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7) != null?row1.getCell(7).getStringCellValue():null;	
			assertTrue(header7.equals("Prop0Type")  && (cell7 == null || cell7.equals("")));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8) != null? row1.getCell(8).getStringCellValue():null;	
			assertTrue(header8.equals("GO_Annotation")  && (cell8 == null || cell8.equals("")));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null?row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("Prop9Type")  && (cell9 == null || cell9.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null?row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("Prop9Type2") && (cell10 == null || cell10.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			
			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("Maps_To")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("PropType")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("PropType2")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("Prop0Type")  && cell7a.equals("|prop0value|"));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("GO_Annotation")  && cell8a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9) != null?row2.getCell(9).getStringCellValue():null;	
			assertTrue(header9a.equals("Prop9Type")  && (cell9a == null || cell9a.equals("")));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10) != null?row2.getCell(10).getStringCellValue():null;	
			assertTrue(header10a.equals("Prop9Type2")  && (cell10a == null || cell10a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("Maps_To")  && cell4b.equals(""));
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("PropType")  && cell5b.equals("|prop9value3|"));
			
			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("PropType2")  && cell6b.equals(""));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("Prop0Type")  && cell7b.equals(""));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("GO_Annotation")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9) != null?row3.getCell(9).getStringCellValue():null;	
			assertTrue(header9b.equals("Prop9Type")  && cell9b.equals("|prop9value|"));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10) != null?row3.getCell(10).getStringCellValue():null;	
			assertTrue(header10b.equals("Prop9Type2")  && cell10b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("Maps_To")  && cell4c.equals("|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|"));
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("PropType")  && cell5c.equals("|prop9value3|"));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("PropType2")  && cell6c.equals(""));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("Prop0Type")  && cell7c.equals(""));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("GO_Annotation")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9) != null?row4.getCell(9).getStringCellValue():null;	
			assertTrue(header9c.equals("Prop9Type")  && cell9c.equals("|prop9value|"));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10) != null?row4.getCell(10).getStringCellValue():null;	
			assertTrue(header10c.equals("Prop9Type2")  && cell10c.equals("|prop9value2|"));
			

			String cell11c = row5.getCell(0) != null?row5.getCell(0).getStringCellValue():null;	
			assertEquals(cell11c,null);			
			String cell12c = row6.getCell(0) != null?row5.getCell(0).getStringCellValue():null;	
			assertEquals(cell12c,null);
			String cell13c = row7.getCell(0) != null?row5.getCell(0).getStringCellValue():null;	
			assertEquals(cell13c,null);
			
			String cell14c = row9.getCell(0) != null?row8.getCell(0).getStringCellValue():null;	
			assertEquals(cell14c,"Report Search Parameters: ");		
			String cell15c = row9.getCell(0) != null?row9.getCell(0).getStringCellValue():null;	
			assertEquals(cell15c,"Input:  C123456");
			String cell16c = row10.getCell(0) != null?row10.getCell(0).getStringCellValue():null;	
			assertEquals(cell16c,"Hierarchy level: 0");
			String cell17c = row11.getCell(0) != null?row11.getCell(0).getStringCellValue():null;	
			assertEquals(cell17c,"Properties Selected: Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2");		

		stream.close();
		workbook.close();
	}
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingDefinition() throws IOException {

		String props = "DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1b.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index).getStringCellValue());
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertTrue(header4.equals("definitions")  && cell4.equals("|NCI:defvalue|defvalue2|"));
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertTrue(header5.equals("PropType")  && cell5.equals("|propvalue|propvalue1|"));
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("PropType2")  && cell6.equals("|propvalue2|"));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7) != null?row1.getCell(7).getStringCellValue():null;	
			assertTrue(header7.equals("Prop0Type")  && (cell7 == null || cell7.equals("")));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8) != null? row1.getCell(8).getStringCellValue():null;	
			assertTrue(header8.equals("GO_Annotation")  && (cell8 == null || cell8.equals("")));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null?row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("Prop9Type")  && (cell9 == null || cell9.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null?row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("Prop9Type2") && (cell10 == null || cell10.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			
			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("definitions")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("PropType")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("PropType2")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("Prop0Type")  && cell7a.equals("|prop0value|"));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("GO_Annotation")  && cell8a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9) != null?row2.getCell(9).getStringCellValue():null;	
			assertTrue(header9a.equals("Prop9Type")  && (cell9a == null || cell9a.equals("")));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10) != null?row2.getCell(10).getStringCellValue():null;	
			assertTrue(header10a.equals("Prop9Type2")  && (cell10a == null || cell10a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("definitions")  && cell4b.equals(""));
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("PropType")  && cell5b.equals("|prop9value3|"));
			
			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("PropType2")  && cell6b.equals(""));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("Prop0Type")  && cell7b.equals(""));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("GO_Annotation")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9) != null?row3.getCell(9).getStringCellValue():null;	
			assertTrue(header9b.equals("Prop9Type")  && cell9b.equals("|prop9value|"));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10) != null?row3.getCell(10).getStringCellValue():null;	
			assertTrue(header10b.equals("Prop9Type2")  && cell10b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("definitions")  && cell4c.equals(""));
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("PropType")  && cell5c.equals("|prop9value3|"));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("PropType2")  && cell6c.equals(""));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("Prop0Type")  && cell7c.equals(""));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("GO_Annotation")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9) != null?row4.getCell(9).getStringCellValue():null;	
			assertTrue(header9c.equals("Prop9Type")  && cell9c.equals("|prop9value|"));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10) != null?row4.getCell(10).getStringCellValue():null;	
			assertTrue(header10c.equals("Prop9Type2")  && cell10c.equals("|prop9value2|"));

		stream.close();
		workbook.close();
	}
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingBothDefinitions() throws IOException {

		String props = "ALT_DEFINITION,DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1b.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index).getStringCellValue());
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertTrue(header4.equals("definitions")  && cell4.equals("|NCI:defvalue|defvalue2|"));
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertTrue(header5.equals("PropType")  && cell5.equals("|propvalue|propvalue1|"));
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("PropType2")  && cell6.equals("|propvalue2|"));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7) != null?row1.getCell(7).getStringCellValue():null;	
			assertTrue(header7.equals("Prop0Type")  && (cell7 == null || cell7.equals("")));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8) != null? row1.getCell(8).getStringCellValue():null;	
			assertTrue(header8.equals("GO_Annotation")  && (cell8 == null || cell8.equals("")));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null?row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("Prop9Type")  && (cell9 == null || cell9.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null?row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("Prop9Type2") && (cell10 == null || cell10.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			
			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("definitions")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("PropType")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("PropType2")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("Prop0Type")  && cell7a.equals("|prop0value|"));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("GO_Annotation")  && cell8a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9) != null?row2.getCell(9).getStringCellValue():null;	
			assertTrue(header9a.equals("Prop9Type")  && (cell9a == null || cell9a.equals("")));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10) != null?row2.getCell(10).getStringCellValue():null;	
			assertTrue(header10a.equals("Prop9Type2")  && (cell10a == null || cell10a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("definitions")  && cell4b.equals(""));
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("PropType")  && cell5b.equals("|prop9value3|"));
			
			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("PropType2")  && cell6b.equals(""));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("Prop0Type")  && cell7b.equals(""));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("GO_Annotation")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9) != null?row3.getCell(9).getStringCellValue():null;	
			assertTrue(header9b.equals("Prop9Type")  && cell9b.equals("|prop9value|"));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10) != null?row3.getCell(10).getStringCellValue():null;	
			assertTrue(header10b.equals("Prop9Type2")  && cell10b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("definitions")  && cell4c.equals(""));
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("PropType")  && cell5c.equals("|prop9value3|"));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("PropType2")  && cell6c.equals(""));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("Prop0Type")  && cell7c.equals(""));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("GO_Annotation")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9) != null?row4.getCell(9).getStringCellValue():null;	
			assertTrue(header9c.equals("Prop9Type")  && cell9c.equals("|prop9value|"));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10) != null?row4.getCell(10).getStringCellValue():null;	
			assertTrue(header10c.equals("Prop9Type2")  && cell10c.equals("|prop9value2|"));

		stream.close();
		workbook.close();
	}
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingAltDefinition() throws IOException {

		String props = "ALT_DEFINITION,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1b.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index).getStringCellValue());
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertTrue(header4.equals("definitions")  && cell4.equals("|NCI:defvalue|defvalue2|"));
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertTrue(header5.equals("PropType")  && cell5.equals("|propvalue|propvalue1|"));
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("PropType2")  && cell6.equals("|propvalue2|"));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7) != null?row1.getCell(7).getStringCellValue():null;	
			assertTrue(header7.equals("Prop0Type")  && (cell7 == null || cell7.equals("")));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8) != null? row1.getCell(8).getStringCellValue():null;	
			assertTrue(header8.equals("GO_Annotation")  && (cell8 == null || cell8.equals("")));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null?row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("Prop9Type")  && (cell9 == null || cell9.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null?row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("Prop9Type2") && (cell10 == null || cell10.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			
			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("definitions")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("PropType")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("PropType2")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("Prop0Type")  && cell7a.equals("|prop0value|"));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("GO_Annotation")  && cell8a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9) != null?row2.getCell(9).getStringCellValue():null;	
			assertTrue(header9a.equals("Prop9Type")  && (cell9a == null || cell9a.equals("")));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10) != null?row2.getCell(10).getStringCellValue():null;	
			assertTrue(header10a.equals("Prop9Type2")  && (cell10a == null || cell10a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("definitions")  && cell4b.equals(""));
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("PropType")  && cell5b.equals("|prop9value3|"));
			
			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("PropType2")  && cell6b.equals(""));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("Prop0Type")  && cell7b.equals(""));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("GO_Annotation")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9) != null?row3.getCell(9).getStringCellValue():null;	
			assertTrue(header9b.equals("Prop9Type")  && cell9b.equals("|prop9value|"));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10) != null?row3.getCell(10).getStringCellValue():null;	
			assertTrue(header10b.equals("Prop9Type2")  && cell10b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("definitions")  && cell4c.equals(""));
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("PropType")  && cell5c.equals("|prop9value3|"));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("PropType2")  && cell6c.equals(""));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("Prop0Type")  && cell7c.equals(""));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("GO_Annotation")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9) != null?row4.getCell(9).getStringCellValue():null;	
			assertTrue(header9c.equals("Prop9Type")  && cell9c.equals("|prop9value|"));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10) != null?row4.getCell(10).getStringCellValue():null;	
			assertTrue(header10c.equals("Prop9Type2")  && cell10c.equals("|prop9value2|"));

		stream.close();
		workbook.close();
	}
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingAltDefSyn() throws IOException {

		String props = "DEFINITION,Display_Name,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(svc.getEntitiesForPropertyNameFilter(getRestRoleEntityList(), CommonServices.splitInput(props)),props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1c.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index).getStringCellValue());
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertEquals(header4,"synonyms");
			assertEquals(cell4,"|synSource2 NCI atermgrp:synName2 |Display_Name:CDISC |");
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertEquals(header5,"definitions");
			assertEquals(cell5,"|NCI:defvalue|");
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("PropType")  && cell6.equals("|propvalue|propvalue1|"));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7).getStringCellValue();	
			assertTrue(header7.equals("PropType2")  && cell7.equals("|propvalue2|"));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8) != null?row1.getCell(8).getStringCellValue():null;	
			assertTrue(header8.equals("Prop0Type")  && (cell8 == null || cell8.equals("")));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null? row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("GO_Annotation")  && (cell9 == null || cell9.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null?row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("Prop9Type")  && (cell10 == null || cell10.equals("")));
			
			String header11 = headerRow.getCell(11).getStringCellValue();
			String cell11 = row1.getCell(11) != null?row1.getCell(11).getStringCellValue():null;	
			assertTrue(header11.equals("Prop9Type2") && (cell11 == null || cell11.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			

			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("synonyms")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("definitions")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("PropType")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("PropType2")  && cell7a.equals(""));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("Prop0Type")  && cell8a.equals("|prop0value|"));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9).getStringCellValue();	
			assertTrue(header9a.equals("GO_Annotation")  && cell9a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10) != null?row2.getCell(10).getStringCellValue():null;	
			assertTrue(header10a.equals("Prop9Type")  && (cell10a == null || cell10a.equals("")));
			
			String header11a = headerRow.getCell(11).getStringCellValue();
			String cell11a = row2.getCell(11) != null?row2.getCell(11).getStringCellValue():null;	
			assertTrue(header11a.equals("Prop9Type2")  && (cell11a == null || cell11a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("synonyms")  && cell4b.equals(""));
			
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("definitions")  && cell5b.equals(""));

			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("PropType")  && cell6b.equals("|prop9value3|"));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("PropType2")  && cell7b.equals(""));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("Prop0Type")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9).getStringCellValue();	
			assertTrue(header9b.equals("GO_Annotation")  && cell9b.equals(""));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10) != null?row3.getCell(10).getStringCellValue():null;	
			assertTrue(header10b.equals("Prop9Type")  && cell10b.equals("|prop9value|"));
			
			String header11b = headerRow.getCell(11).getStringCellValue();
			String cell11b = row3.getCell(11) != null?row3.getCell(11).getStringCellValue():null;	
			assertTrue(header11b.equals("Prop9Type2")  && cell11b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("synonyms")  && cell4c.equals(""));
			
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("definitions")  && cell5c.equals(""));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("PropType")  && cell6c.equals("|prop9value3|"));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("PropType2")  && cell7c.equals(""));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("Prop0Type")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9).getStringCellValue();	
			assertTrue(header9c.equals("GO_Annotation")  && cell9c.equals(""));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10) != null?row4.getCell(10).getStringCellValue():null;	
			assertTrue(header10c.equals("Prop9Type")  && cell10c.equals("|prop9value|"));
			
			String header11c = headerRow.getCell(11).getStringCellValue();
			String cell11c = row4.getCell(11) != null?row4.getCell(11).getStringCellValue():null;	
			assertTrue(header11c.equals("Prop9Type2")  && cell11c.equals("|prop9value2|"));

		stream.close();
		workbook.close();
	}
	
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingAltMapsDef() throws IOException {

		String props = "DEFINITION,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1c.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index).getStringCellValue());
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertTrue(header4.equals("definitions")  && cell4.equals("|NCI:defvalue|defvalue2|"));
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertTrue(header5.equals("Maps_To")  && cell5.equals(""));
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("PropType")  && cell6.equals("|propvalue|propvalue1|"));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7).getStringCellValue();	
			assertTrue(header7.equals("PropType2")  && cell7.equals("|propvalue2|"));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8) != null?row1.getCell(8).getStringCellValue():null;	
			assertTrue(header8.equals("Prop0Type")  && (cell8 == null || cell8.equals("")));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null? row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("GO_Annotation")  && (cell9 == null || cell9.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null?row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("Prop9Type")  && (cell10 == null || cell10.equals("")));
			
			String header11 = headerRow.getCell(11).getStringCellValue();
			String cell11 = row1.getCell(11) != null?row1.getCell(11).getStringCellValue():null;	
			assertTrue(header11.equals("Prop9Type2") && (cell11 == null || cell11.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			

			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("definitions")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("Maps_To")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("PropType")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("PropType2")  && cell7a.equals(""));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("Prop0Type")  && cell8a.equals("|prop0value|"));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9).getStringCellValue();	
			assertTrue(header9a.equals("GO_Annotation")  && cell9a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10) != null?row2.getCell(10).getStringCellValue():null;	
			assertTrue(header10a.equals("Prop9Type")  && (cell10a == null || cell10a.equals("")));
			
			String header11a = headerRow.getCell(11).getStringCellValue();
			String cell11a = row2.getCell(11) != null?row2.getCell(11).getStringCellValue():null;	
			assertTrue(header11a.equals("Prop9Type2")  && (cell11a == null || cell11a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("definitions")  && cell4b.equals(""));
			
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("Maps_To")  && cell5b.equals(""));

			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("PropType")  && cell6b.equals("|prop9value3|"));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("PropType2")  && cell7b.equals(""));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("Prop0Type")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9).getStringCellValue();	
			assertTrue(header9b.equals("GO_Annotation")  && cell9b.equals(""));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10) != null?row3.getCell(10).getStringCellValue():null;	
			assertTrue(header10b.equals("Prop9Type")  && cell10b.equals("|prop9value|"));
			
			String header11b = headerRow.getCell(11).getStringCellValue();
			String cell11b = row3.getCell(11) != null?row3.getCell(11).getStringCellValue():null;	
			assertTrue(header11b.equals("Prop9Type2")  && cell11b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("definitions")  && cell4c.equals(""));
			
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("Maps_To")  && cell5c.equals("|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|"));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("PropType")  && cell6c.equals("|prop9value3|"));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("PropType2")  && cell7c.equals(""));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("Prop0Type")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9).getStringCellValue();	
			assertTrue(header9c.equals("GO_Annotation")  && cell9c.equals(""));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10) != null?row4.getCell(10).getStringCellValue():null;	
			assertTrue(header10c.equals("Prop9Type")  && cell10c.equals("|prop9value|"));
			
			String header11c = headerRow.getCell(11).getStringCellValue();
			String cell11c = row4.getCell(11) != null?row4.getCell(11).getStringCellValue():null;	
			assertTrue(header11c.equals("Prop9Type2")  && cell11c.equals("|prop9value2|"));

		stream.close();
		workbook.close();
	}
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingAltMapsSyn() throws IOException {

		String props = "FULL_SYN,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(getRestRoleEntityList(), props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1c.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index).getStringCellValue());
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertEquals(header4,"synonyms");
			assertEquals(cell4,"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |Display_Name:CDISC |");
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertTrue(header5.equals("Maps_To")  && cell5.equals(""));
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("PropType")  && cell6.equals("|propvalue|propvalue1|"));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7).getStringCellValue();	
			assertTrue(header7.equals("PropType2")  && cell7.equals("|propvalue2|"));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8) != null?row1.getCell(8).getStringCellValue():null;	
			assertTrue(header8.equals("Prop0Type")  && (cell8 == null || cell8.equals("")));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null? row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("GO_Annotation")  && (cell9 == null || cell9.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null?row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("Prop9Type")  && (cell10 == null || cell10.equals("")));
			
			String header11 = headerRow.getCell(11).getStringCellValue();
			String cell11 = row1.getCell(11) != null?row1.getCell(11).getStringCellValue():null;	
			assertTrue(header11.equals("Prop9Type2") && (cell11 == null || cell11.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			

			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("synonyms")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("Maps_To")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("PropType")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("PropType2")  && cell7a.equals(""));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("Prop0Type")  && cell8a.equals("|prop0value|"));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9).getStringCellValue();	
			assertTrue(header9a.equals("GO_Annotation")  && cell9a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10) != null?row2.getCell(10).getStringCellValue():null;	
			assertTrue(header10a.equals("Prop9Type")  && (cell10a == null || cell10a.equals("")));
			
			String header11a = headerRow.getCell(11).getStringCellValue();
			String cell11a = row2.getCell(11) != null?row2.getCell(11).getStringCellValue():null;	
			assertTrue(header11a.equals("Prop9Type2")  && (cell11a == null || cell11a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("synonyms")  && cell4b.equals(""));
			
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("Maps_To")  && cell5b.equals(""));

			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("PropType")  && cell6b.equals("|prop9value3|"));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("PropType2")  && cell7b.equals(""));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("Prop0Type")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9).getStringCellValue();	
			assertTrue(header9b.equals("GO_Annotation")  && cell9b.equals(""));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10) != null?row3.getCell(10).getStringCellValue():null;	
			assertTrue(header10b.equals("Prop9Type")  && cell10b.equals("|prop9value|"));
			
			String header11b = headerRow.getCell(11).getStringCellValue();
			String cell11b = row3.getCell(11) != null?row3.getCell(11).getStringCellValue():null;	
			assertTrue(header11b.equals("Prop9Type2")  && cell11b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("synonyms")  && cell4c.equals(""));
			
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("Maps_To")  && cell5c.equals("|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|"));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("PropType")  && cell6c.equals("|prop9value3|"));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("PropType2")  && cell7c.equals(""));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("Prop0Type")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9).getStringCellValue();	
			assertTrue(header9c.equals("GO_Annotation")  && cell9c.equals(""));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10) != null?row4.getCell(10).getStringCellValue():null;	
			assertTrue(header10c.equals("Prop9Type")  && cell10c.equals("|prop9value|"));
			
			String header11c = headerRow.getCell(11).getStringCellValue();
			String cell11c = row4.getCell(11) != null?row4.getCell(11).getStringCellValue():null;	
			assertTrue(header11c.equals("Prop9Type2")  && cell11c.equals("|prop9value2|"));

		stream.close();
		workbook.close();
	}
	
	@Test
	void testProduceExcelOutputFromByteArrayHeadingHeadingAll() throws IOException {

		String props = "DEFINITION,ALT_DEFINITION,FULL_SYN,Maps_To,PropType,PropType2,Prop0Type,GO_Annotation,Prop9Type,Prop9Type2";
		ByteArrayOutputStream stream = util.produceExcelRoleOutputFromListWithHeading(svc.getEntitiesForPropertyNameFilter(getRestRoleEntityList(), CommonServices.splitInput(props)), props, "C123456");

		 Workbook workbook = new XSSFWorkbook((new ByteArrayInputStream(stream.toByteArray())));
		
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int expected = excelOutLine1f.split(",").length;
 		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
 		assertEquals(expected,cells);

		for(int i = 0; i < rows; i ++) {
			Row row = sheet.getRow(i);
			System.out.println();
			for(int index = 0;index < row.getPhysicalNumberOfCells(); index++) {
				System.out.println(row.getCell(index).getStringCellValue());
			};
		}
		

			Row headerRow = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Row row3 = sheet.getRow(3);
			Row row4 = sheet.getRow(4);

			String header0 = headerRow.getCell(0).getStringCellValue();
			String cell0 = row1.getCell(0).getStringCellValue();	
			assertTrue(header0.equals("terminology")  && cell0.equals("ncit"));
			
			String header1 = headerRow.getCell(1).getStringCellValue();
			String cell1 = row1.getCell(1).getStringCellValue();	
			assertTrue(header1.equals("code")  && cell1.equals("C123234"));
			
			String header2 = headerRow.getCell(2).getStringCellValue();
			String cell2 = row1.getCell(2).getStringCellValue();	
			assertTrue(header2.equals("name")  && cell2.equals("Myent"));
			
			String header3 = headerRow.getCell(3).getStringCellValue();
			String cell3 = row1.getCell(3).getStringCellValue();	
			assertTrue(header3.equals("parents")  && cell3.equals(""));
			
			String header4 = headerRow.getCell(4).getStringCellValue();
			String cell4 = row1.getCell(4).getStringCellValue();	
			assertEquals(header4,"synonyms");  
			assertEquals(cell4,"|NCIt CDISC mytermgr:synName |synSource2 NCI atermgrp:synName2 |");
			
			String header5 = headerRow.getCell(5).getStringCellValue();
			String cell5 = row1.getCell(5).getStringCellValue();	
			assertEquals(header5,"definitions");  
			assertEquals(cell5,"|NCI:defvalue|defvalue2|");
			
			String header6 = headerRow.getCell(6).getStringCellValue();
			String cell6 = row1.getCell(6).getStringCellValue();	
			assertTrue(header6.equals("Maps_To")  && cell6.equals(""));
			
			String header7 = headerRow.getCell(7).getStringCellValue();
			String cell7 = row1.getCell(7).getStringCellValue();	
			assertTrue(header7.equals("PropType")  && cell7.equals("|propvalue|propvalue1|"));
			
			String header8 = headerRow.getCell(8).getStringCellValue();
			String cell8 = row1.getCell(8).getStringCellValue();	
			assertTrue(header8.equals("PropType2")  && cell8.equals("|propvalue2|"));
			
			String header9 = headerRow.getCell(9).getStringCellValue();
			String cell9 = row1.getCell(9) != null?row1.getCell(9).getStringCellValue():null;	
			assertTrue(header9.equals("Prop0Type")  && (cell9 == null || cell8.equals("")));
			
			String header10 = headerRow.getCell(10).getStringCellValue();
			String cell10 = row1.getCell(10) != null? row1.getCell(10).getStringCellValue():null;	
			assertTrue(header10.equals("GO_Annotation")  && (cell10 == null || cell9.equals("")));
			
			String header11 = headerRow.getCell(11).getStringCellValue();
			String cell11 = row1.getCell(11) != null?row1.getCell(11).getStringCellValue():null;	
			assertTrue(header11.equals("Prop9Type")  && (cell11 == null || cell11.equals("")));
			
			String header12 = headerRow.getCell(12).getStringCellValue();
			String cell12 = row1.getCell(12) != null?row1.getCell(12).getStringCellValue():null;	
			assertTrue(header12.equals("Prop9Type2") && (cell12 == null || cell12.equals("")));
			
			
			String header0a = headerRow.getCell(0).getStringCellValue();
			String cell0a = row2.getCell(0).getStringCellValue();	
			assertTrue(header0a.equals("terminology")  && cell0a.equals("ncit"));
			
			String header1a = headerRow.getCell(1).getStringCellValue();
			String cell1a = row2.getCell(1).getStringCellValue();	
			assertTrue(header1a.equals("code")  && cell1a.equals("C000000"));
			
			String header2a = headerRow.getCell(2).getStringCellValue();
			String cell2a = row2.getCell(2).getStringCellValue();	
			assertTrue(header2a.equals("name")  && cell2a.equals("0ent"));
			
			String header3a = headerRow.getCell(3).getStringCellValue();
			String cell3a = row2.getCell(3).getStringCellValue();	
			assertTrue(header3a.equals("parents")  && cell3a.equals(""));
			

			String header4a = headerRow.getCell(4).getStringCellValue();
			String cell4a = row2.getCell(4).getStringCellValue();	
			assertTrue(header4a.equals("synonyms")  && cell4a.equals(""));
			
			String header5a = headerRow.getCell(5).getStringCellValue();
			String cell5a = row2.getCell(5).getStringCellValue();	
			assertTrue(header5a.equals("definitions")  && cell5a.equals(""));
			
			String header6a = headerRow.getCell(6).getStringCellValue();
			String cell6a = row2.getCell(6).getStringCellValue();	
			assertTrue(header6a.equals("Maps_To")  && cell6a.equals(""));
			
			String header7a = headerRow.getCell(7).getStringCellValue();
			String cell7a = row2.getCell(7).getStringCellValue();	
			assertTrue(header7a.equals("PropType")  && cell7a.equals(""));
			
			String header8a = headerRow.getCell(8).getStringCellValue();
			String cell8a = row2.getCell(8).getStringCellValue();	
			assertTrue(header8a.equals("PropType2")  && cell8a.equals(""));
			
			String header9a = headerRow.getCell(9).getStringCellValue();
			String cell9a = row2.getCell(9).getStringCellValue();	
			assertTrue(header9a.equals("Prop0Type")  && cell9a.equals("|prop0value|"));
			
			String header10a = headerRow.getCell(10).getStringCellValue();
			String cell10a = row2.getCell(10).getStringCellValue();	
			assertTrue(header10a.equals("GO_Annotation")  && cell10a.equals("|GO:0000075 prop0value2:TAS|"));
			
			String header11a = headerRow.getCell(11).getStringCellValue();
			String cell11a = row2.getCell(11) != null?row2.getCell(11).getStringCellValue():null;	
			assertTrue(header11a.equals("Prop9Type")  && (cell11a == null || cell11a.equals("")));
			
			String header12a = headerRow.getCell(12).getStringCellValue();
			String cell12a = row2.getCell(12) != null?row2.getCell(12).getStringCellValue():null;	
			assertTrue(header12a.equals("Prop9Type2")  && (cell12a == null || cell12a.equals("")));

			
			String header0b = headerRow.getCell(0).getStringCellValue();
			String cell0b = row3.getCell(0).getStringCellValue();	
			assertTrue(header0b.equals("terminology")  && cell0b.equals("ncit"));
			
			String header1b = headerRow.getCell(1).getStringCellValue();
			String cell1b = row3.getCell(1).getStringCellValue();	
			assertTrue(header1b.equals("code")  && cell1b.equals("C999999"));
			
			String header2b = headerRow.getCell(2).getStringCellValue();
			String cell2b = row3.getCell(2).getStringCellValue();	
			assertTrue(header2b.equals("name")  && cell2b.equals("My9"));
			
			String header3b = headerRow.getCell(3).getStringCellValue();
			String cell3b = row3.getCell(3).getStringCellValue();	
			assertTrue(header3b.equals("parents")  && cell3b.equals(""));
			
			String header4b = headerRow.getCell(4).getStringCellValue();
			String cell4b = row3.getCell(4).getStringCellValue();	
			assertTrue(header4b.equals("synonyms")  && cell4b.equals(""));
			
			String header5b = headerRow.getCell(5).getStringCellValue();
			String cell5b = row3.getCell(5).getStringCellValue();	
			assertTrue(header5b.equals("definitions")  && cell5b.equals(""));

			String header6b = headerRow.getCell(6).getStringCellValue();
			String cell6b = row3.getCell(6).getStringCellValue();	
			assertTrue(header6b.equals("Maps_To")  && cell6b.equals(""));
			
			String header7b = headerRow.getCell(7).getStringCellValue();
			String cell7b = row3.getCell(7).getStringCellValue();	
			assertTrue(header7b.equals("PropType")  && cell7b.equals("|prop9value3|"));
			
			String header8b = headerRow.getCell(8).getStringCellValue();
			String cell8b = row3.getCell(8).getStringCellValue();	
			assertTrue(header8b.equals("PropType2")  && cell8b.equals(""));
			
			String header9b = headerRow.getCell(9).getStringCellValue();
			String cell9b = row3.getCell(9).getStringCellValue();	
			assertTrue(header9b.equals("Prop0Type")  && cell9b.equals(""));
			
			String header10b = headerRow.getCell(10).getStringCellValue();
			String cell10b = row3.getCell(10).getStringCellValue();	
			assertTrue(header10b.equals("GO_Annotation")  && cell10b.equals(""));
			
			String header11b = headerRow.getCell(11).getStringCellValue();
			String cell11b = row3.getCell(11) != null?row3.getCell(11).getStringCellValue():null;	
			assertTrue(header11b.equals("Prop9Type")  && cell11b.equals("|prop9value|"));
			
			String header12b = headerRow.getCell(12).getStringCellValue();
			String cell12b = row3.getCell(12) != null?row3.getCell(12).getStringCellValue():null;	
			assertTrue(header12b.equals("Prop9Type2")  && cell12b.equals("|prop9value2|"));

			
			String header0c = headerRow.getCell(0).getStringCellValue();
			String cell0c = row4.getCell(0).getStringCellValue();	
			assertTrue(header0c.equals("terminology")  && cell0c.equals("ncit"));
			
			String header1c = headerRow.getCell(1).getStringCellValue();
			String cell1c = row4.getCell(1).getStringCellValue();	
			assertTrue(header1c.equals("code")  && cell1c.equals("C2222"));
			
			String header2c = headerRow.getCell(2).getStringCellValue();
			String cell2c = row4.getCell(2).getStringCellValue();	
			assertTrue(header2c.equals("name")  && cell2c.equals("My2"));
			
			String header3c = headerRow.getCell(3).getStringCellValue();
			String cell3c = row4.getCell(3).getStringCellValue();	
			assertTrue(header3c.equals("parents")  && cell3c.equals(""));
			
			String header4c = headerRow.getCell(4).getStringCellValue();
			String cell4c = row4.getCell(4).getStringCellValue();	
			assertTrue(header4c.equals("synonyms")  && cell4c.equals(""));
			
			String header5c = headerRow.getCell(5).getStringCellValue();
			String cell5c = row4.getCell(5).getStringCellValue();	
			assertTrue(header5c.equals("definitions")  && cell5c.equals(""));
			
			String header6c = headerRow.getCell(6).getStringCellValue();
			String cell6c = row4.getCell(6).getStringCellValue();	
			assertTrue(header6c.equals("Maps_To")  && cell6c.equals("|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|ICDO3 3.1 PT 9861/3 Acute myeloid leukemia, NOS:Related To|GDC PT PD Acute myeloid leukemia, NOS:Has Synonym|"));
			
			String header7c = headerRow.getCell(7).getStringCellValue();
			String cell7c = row4.getCell(7).getStringCellValue();	
			assertTrue(header7c.equals("PropType")  && cell7c.equals("|prop9value3|"));
			
			String header8c = headerRow.getCell(8).getStringCellValue();
			String cell8c = row4.getCell(8).getStringCellValue();	
			assertTrue(header8c.equals("PropType2")  && cell8c.equals(""));
			
			String header9c = headerRow.getCell(9).getStringCellValue();
			String cell9c = row4.getCell(9).getStringCellValue();	
			assertTrue(header9c.equals("Prop0Type")  && cell9c.equals(""));
			
			String header10c = headerRow.getCell(10).getStringCellValue();
			String cell10c = row4.getCell(10).getStringCellValue();	
			assertTrue(header10c.equals("GO_Annotation")  && cell10c.equals(""));
			
			String header11c = headerRow.getCell(11).getStringCellValue();
			String cell11c = row4.getCell(11) != null?row4.getCell(11).getStringCellValue():null;	
			assertTrue(header11c.equals("Prop9Type")  && cell11c.equals("|prop9value|"));
			
			String header12c = headerRow.getCell(12).getStringCellValue();
			String cell12c = row4.getCell(12) != null?row4.getCell(12).getStringCellValue():null;	
			assertTrue(header12c.equals("Prop9Type2")  && cell12c.equals("|prop9value2|"));

		stream.close();
		workbook.close();
	}
	
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
