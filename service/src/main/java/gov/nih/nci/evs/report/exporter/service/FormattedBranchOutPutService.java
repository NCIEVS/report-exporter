package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.util.CSVUtility;
import gov.nih.nci.evs.report.exporter.util.CommonServices;
import gov.nih.nci.evs.report.exporter.util.ExcelUtility;
import gov.nih.nci.evs.report.exporter.util.TabDelUtility;

@Service
public class FormattedBranchOutPutService {
	
	@Autowired
	BranchResolutionService service;
	
	@Autowired
	CodeReadService readService;

	
	public InputStream getJsonBytesForRestChildParams(String codes, String max) {
		return new ByteArrayInputStream(
				CommonServices.getGsonForPrettyPrint().toJson(
								service.getAllChildrenForBranchTopNode(
									CommonServices.splitInput(codes), 
								max)).getBytes());
	}
	
	public InputStream getChildCSVBytesForRestParams(String codes, String max) {
						return new ByteArrayInputStream(new CSVUtility()
								.produceChildCSVOutputFromListWithHeading(service.getAllChildrenForBranchTopNode(
										CommonServices.splitInput(codes), 
										max)).getBytes());
	}
	
	public InputStream getChildTabDelBytesForRestParams(String codes, String max) {
		return new ByteArrayInputStream(new TabDelUtility()
				.produceChildTabDelOutputFromListWithHeading(service.getAllChildrenForBranchTopNode(
						CommonServices.splitInput(codes), 
					max)).getBytes());
	}
	
	public ByteArrayInputStream getChildXSLBytesForRestParams(String codes, String max) {
		try {
			return new ByteArrayInputStream(new ExcelUtility()
					.produceChildExcelOutputFromListWithHeading(service.getAllChildrenForBranchTopNode(
							CommonServices.splitInput(codes), 
						max)).toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Input/Output failure in Excel formatted output: ",e);
		}
	}
	
	public InputStream getJsonBytesForRestParams(String codes, String props, String max) {
		return new ByteArrayInputStream(
				CommonServices.getGsonForPrettyPrint().toJson(
								service.getResolvedChildFlatListFromTopNode( 
									codes, props, max)).getBytes());
	}
	
	public InputStream getCSVBytesForRestParams(String codes, String props,String max) {
						return new ByteArrayInputStream(new CSVUtility()
								.produceCSVOutputFromListWithHeading(
										service.getResolvedChildFlatListFromTopNode( 
										codes, props, max)).getBytes());
	}
	
	public InputStream getTabDelBytesForRestParams(String codes, String props,String max) {
		return new ByteArrayInputStream(new TabDelUtility()
				.produceTabDelOutputFromListWithHeading(
						service.getResolvedChildFlatListFromTopNode( 
								codes, props, max)).getBytes());
	}
	
	public ByteArrayInputStream getXSLBytesForRestParams(String codes, String props, String max) {
		try {
			return new ByteArrayInputStream(new ExcelUtility()
					.produceExcelOutputFromListWithHeading(
							service.getResolvedChildFlatListFromTopNode( 
									codes, props, max)).toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Input/Output failure in Excel formatted output: ",e);
		}
	}

}