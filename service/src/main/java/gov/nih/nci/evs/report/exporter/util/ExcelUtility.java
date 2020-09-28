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

public class ExcelUtility extends FormatUtility {
	

	
	  public ByteArrayOutputStream  produceExcelOutputFromListWithHeading(List<RestEntity> entities, String props) throws IOException {
	   
	    //Init the workbook for Excel
	    Workbook workbook = new XSSFWorkbook();
	    //Init the services to maintain an instance of the prooerty cache
	    CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
	    //Create a sheet for the workbook
	    Sheet sheet = workbook.createSheet("entities");
	    //Set up the head configuration
	    Font headerFont = workbook.createFont();
	    headerFont.setBold(true);
	    headerFont.setColor(IndexedColors.BLUE.getIndex());
	    CellStyle headerCellStyle = workbook.createCellStyle();
	    headerCellStyle.setFont(headerFont);
	 
	    // Row for Header
	    Row headerRow = sheet.createRow(0);
	 
	    // First part of the header, before we know what properties are there
//	    int col = 0;
//	    for (String field: FIELDS) {
//	      if(field.equals("properties")) {break;}
//	      Cell cell = headerRow.createCell(col);
//	      cell.setCellValue(FIELDS[col]);
//	      cell.setCellStyle(headerCellStyle);
//	      col++;
//	    }
	 
	    //Iterate over the list of RestEntities to designated rows and columns
	    int i = 1;
	    for (RestEntity entity : entities) {
	    	Integer internalIndex = 0;
	    //Add each property list to the cache noting the position of the property in 
	    //the cache
	    	entity.getProperties()
			.stream()
			.forEach(z -> services.addPropertyTypeAndPositionToCache(z));
	      Row row = sheet.createRow(i++);
	      //Create a set of rows for the static values
	      row.createCell(internalIndex++).setCellValue(entity.getTerminology());
	      row.createCell(internalIndex++).setCellValue(entity.getCode());
	      row.createCell(internalIndex++).setCellValue(entity.getName());
	      row.createCell(internalIndex++).setCellValue(
	    	    CommonServices.cleanListOutPut(CommonServices.getListValuesForExcel(
	    	    		entity.getParents() != null?
	    	    				entity.getParents():
	    	    					null)));
	      //Process the Synonyms as a list
	      if(!services.isNoSynonyms() && 
	    		  !(entity.getSynonyms() == null || entity.getSynonyms().size() == 0)) {
	    	  			services.createCellInExcelRow(entity.getSynonyms(), internalIndex++, row);
	      }
	      //Process the definitions as a list
	      if(!services.isNoDefinitions() && 
	    		  !(entity.getDefinitions() == null || entity.getDefinitions().size() == 0)) {
	    	  			services.createCellInExcelRow(entity.getDefinitions(),internalIndex++, row);}
	      //Process the maps as a list
	      if(!services.isNoMaps() && 
	    		  !(entity.getMaps() == null || entity.getMaps().size() == 0)) {
	    	  			services.createCellInExcelRow(entity.getMaps(),internalIndex++, row);
	      }
	      //Process the properties to rows and columns adding properties as we go
	      		  services.setPropertyRowOutPut(
	    				  entity.getProperties(), row, internalIndex++);

	      //Clearing property list for the next entity, leaving type and position metadata
	    		  services.clearPropertyListsFromHeaderMap();		  
	    }
	    
	    // First part of the header, before we know what properties are there
	    List<String> fields = services.filterHeadings(services);
	    int col = 0;
	    for (String field: fields) {
	      if(field.equals("properties")) {break;}
	      Cell cell = headerRow.createCell(col);
	      cell.setCellValue(fields.get(col));
	      cell.setCellStyle(headerCellStyle);
	      col++;
	    }
		
	    //Finish setting up headers with each property type designation
	    List<String> postHeaders = services.getHeadersByPosition(services.getPropHeaderMap());
	    for(String s: postHeaders) {
	    
	    Cell cell = headerRow.createCell(col);
	      cell.setCellValue(s);
	      cell.setCellStyle(headerCellStyle);
	      col++;}

	   //Setup the output stream for download
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
