package gov.nih.nci.evs.report.exporter.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gov.nih.nci.evs.report.exporter.model.RestEntity;

public class ExcelUtility {
	

	  private byte[] writeToExcel(List<RestEntity> entities) throws IOException {
	    String[] COLUMNs = {"Id", "Name", "Address", "Age"};
	    
	    Workbook workbook = new XSSFWorkbook();
	     
	    CreationHelper createHelper = workbook.getCreationHelper();
	 
	    Sheet sheet = workbook.createSheet("entitys");
	 
	    Font headerFont = workbook.createFont();
	    headerFont.setBold(true);
	    headerFont.setColor(IndexedColors.BLUE.getIndex());
	 
	    CellStyle headerCellStyle = workbook.createCellStyle();
	    headerCellStyle.setFont(headerFont);
	 
	    // Row for Header
	    Row headerRow = sheet.createRow(0);
	 
	    // Header
	    for (int col = 0; col < COLUMNs.length; col++) {
	      Cell cell = headerRow.createCell(col);
	      cell.setCellValue(COLUMNs[col]);
	      cell.setCellStyle(headerCellStyle);
	    }
	 
	 
	    int i = 1;
	    for (RestEntity entity : entities) {
	      Row row = sheet.createRow(i++);
	 
	      row.createCell(0).setCellValue(entity.getCode());
	      row.createCell(1).setCellValue(entity.getName());
	      row.createCell(2).setCellValue(entity.getTerminology());
	      row.createCell(3).setCellValue(entity.getSynonyms().toString());
	      row.createCell(4).setCellValue(entity.getDefinitions().toString());
	      row.createCell(5).setCellValue(entity.getProperties().toString());
	    }
	 
	   byte[] wkbkString = workbook.toString().getBytes();
	   workbook.close();
	   return wkbkString;
	  }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
