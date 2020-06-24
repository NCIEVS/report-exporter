package gov.nih.nci.evs.report.exporter.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;

public class ExcelUtility {
	

	  public ByteArrayOutputStream  produceExcelOutputFromListWithHeading(List<RestEntity> entities) throws IOException {
	    
		Field[] fields = RestEntity.class.getDeclaredFields();
	   
	    List<String> cols = Stream.of(fields).map(x -> x.getName()).collect(Collectors.toList());
	    
	    Workbook workbook = new XSSFWorkbook();
	 
	    Sheet sheet = workbook.createSheet("entities");
	 
	    Font headerFont = workbook.createFont();
	    headerFont.setBold(true);
	    headerFont.setColor(IndexedColors.BLUE.getIndex());
	 
	    CellStyle headerCellStyle = workbook.createCellStyle();
	    headerCellStyle.setFont(headerFont);
	 
	    // Row for Header
	    Row headerRow = sheet.createRow(0);
	 
	    // Header
	    for (int col = 0; col < fields.length; col++) {
	      Cell cell = headerRow.createCell(col);
	      cell.setCellValue(fields[col].getName());
	      cell.setCellStyle(headerCellStyle);
	    }
	 
	 
	    int i = 1;
	    for (RestEntity entity : entities) {
	      Row row = sheet.createRow(i++);
	 
	      row.createCell(0).setCellValue(entity.getCode());
	      row.createCell(1).setCellValue(entity.getName());
	      row.createCell(2).setCellValue(entity.getTerminology());
	      row.createCell(3).setCellValue(entity.getParent());
	      row.createCell(4).setCellValue(
	    		  CommonServices.cleanListOutPut(
	    				  entity.getSynonyms() != null?
	    						  entity.getSynonyms().toString():
	    							  "no synonyms"));
	      row.createCell(6).setCellValue(
	    		  CommonServices.cleanListOutPut(
	    				  entity.getDefinitions() != null?
	    						  entity.getDefinitions().toString():
	    							  "no definitions"));
	      row.createCell(6).setCellValue(
	    		  CommonServices.cleanListOutPut(
	    				  entity.getProperties() != null?
	    						  entity.getProperties().toString():
	    							  "no properties"));
	    }
	 
	   ByteArrayOutputStream stream = new ByteArrayOutputStream();
	   workbook.write(stream);
	   workbook.close();
	   return stream;
	  }
	  
	  public ByteArrayOutputStream  produceChildExcelOutputFromListWithHeading(List<ChildEntity> entities) throws IOException {
		    
			Field[] fields = ChildEntity.class.getDeclaredFields();
		   
		    List<String> cols = Stream.of(fields).map(x -> x.getName()).collect(Collectors.toList());
		    
		    Workbook workbook = new XSSFWorkbook();
		 
		    Sheet sheet = workbook.createSheet("entities");
		 
		    Font headerFont = workbook.createFont();
		    headerFont.setBold(true);
		    headerFont.setColor(IndexedColors.BLUE.getIndex());
		 
		    CellStyle headerCellStyle = workbook.createCellStyle();
		    headerCellStyle.setFont(headerFont);
		 
		    // Row for Header
		    Row headerRow = sheet.createRow(0);
		 
		    // Header
		    for (int col = 0; col < cols.size(); col++) {
		      Cell cell = headerRow.createCell(col);
		      cell.setCellValue(cols.get(col));
		      cell.setCellStyle(headerCellStyle);
		    }
		 
		 
		    int i = 1;
		    for (ChildEntity entity : entities) {
		      Row row = sheet.createRow(i++);
		 
		      row.createCell(0).setCellValue(entity.getCode());
		      row.createCell(1).setCellValue(entity.getName());
		      row.createCell(2).setCellValue(entity.getLevel());
		      row.createCell(3).setCellValue(entity.isLeaf());
		      if(entity.getChildren() != null && entity.getChildren().size() > 0) {
		      row.createCell(4).setCellValue(
		    		  CommonServices.cleanListOutPut(
		    				  entity.getChildren().toString()));
		      }
		    }
		 
		   ByteArrayOutputStream stream = new ByteArrayOutputStream();
		   workbook.write(stream);
		   workbook.close();
		   return stream;
		  }


}
