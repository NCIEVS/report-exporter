package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.util.CSVUtility;
import gov.nih.nci.evs.report.exporter.util.CommonServices;
import gov.nih.nci.evs.report.exporter.util.ExcelUtility;
import gov.nih.nci.evs.report.exporter.util.JSONUtility;
import gov.nih.nci.evs.report.exporter.util.TabDelUtility;

@Service
public class FormattedOutputService {
	
	@Autowired
	CodeReadService crservice;

	
	public InputStream getJsonBytesForRestParams(String codes, String props) {
		return new ByteArrayInputStream(
				new JSONUtility().produceJsonOutputFromListWithHeading(
						crservice.getEntitiesForPropertyNameFilter(
								crservice.getRestEntitiesWithParents( 
									CommonServices.splitInput(codes), props), 
									CommonServices.splitInput(props)),props,codes, 0).getBytes());
	}
	
	public InputStream getCSVBytesForRestParams(String codes, String props) {
						return new ByteArrayInputStream(new CSVUtility()
								.produceCSVOutputFromListWithHeading(
								crservice.getEntitiesForPropertyNameFilter(
								crservice.getRestEntitiesWithParents( 
										CommonServices.splitInput(codes), props), 
										CommonServices.splitInput(props)), props , codes, 0).getBytes());
	}
	
	public InputStream getTabDelBytesForRestParams(String codes, String props) {
		return new ByteArrayInputStream(new TabDelUtility()
				.produceTabDelOutputFromListWithHeading(
				crservice.getEntitiesForPropertyNameFilter(
				crservice.getRestEntitiesWithParents( 
						CommonServices.splitInput(codes), props), 
						CommonServices.splitInput(props)), props, codes, 0).getBytes());
	}
	
	public ByteArrayInputStream getXSLBytesForRestParams(String codes, String props) {
		try {
			return new ByteArrayInputStream(new ExcelUtility()
					.produceExcelOutputFromListWithHeading(
					crservice.getEntitiesForPropertyNameFilter(
					crservice.getRestEntitiesWithParents( 
							CommonServices.splitInput(codes), props), 
							CommonServices.splitInput(props)), props, codes, 0).toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Input/Output failure in Excel formatted output: ",e);
		}
	}
	
}
