package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.util.CSVUtility;
import gov.nih.nci.evs.report.exporter.util.ExcelUtility;
import gov.nih.nci.evs.report.exporter.util.TabDelUtility;

@Service
public class FormattedOutputService {
	
	@Autowired
	CodeReadService service;

	
	public InputStream getJsonBytesForRestParams(String codes, String props) {
		return new ByteArrayInputStream(
				service.getGsonForPrettyPrint().toJson(
						service.getEntitiesForPropertyNameFilter(
								service.getRestProperties( 
									service.getCodes(codes)), 
									service.getCodes(props))).getBytes());
	}
	
	public InputStream getCSVBytesForRestParams(String codes, String props) {
						return new ByteArrayInputStream(new CSVUtility()
								.produceCSVOutputFromListWithHeading(
								service.getEntitiesForPropertyNameFilter(
								service.getRestProperties( 
										service.getCodes(codes)), 
										service.getCodes(props))).getBytes());
	}
	
	public InputStream getTabDelBytesForRestParams(String codes, String props) {
		return new ByteArrayInputStream(new TabDelUtility()
				.produceTabDelOutputFromListWithHeading(
				service.getEntitiesForPropertyNameFilter(
				service.getRestProperties( 
						service.getCodes(codes)), 
						service.getCodes(props))).getBytes());
	}
	
	public ByteArrayInputStream getXSLBytesForRestParams(String codes, String props) {
		try {
			return new ByteArrayInputStream(new ExcelUtility()
					.produceExcelOutputFromListWithHeading(
					service.getEntitiesForPropertyNameFilter(
					service.getRestProperties( 
							service.getCodes(codes)), 
							service.getCodes(props))).toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Input/Output failure in Excel formatted output: ",e);
		}
	}
	
}
