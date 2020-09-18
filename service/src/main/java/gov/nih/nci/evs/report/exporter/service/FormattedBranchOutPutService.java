package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

	
	public InputStream getJsonBytesForRestChildParams(String code, String max) {
		return new ByteArrayInputStream(
				CommonServices.getGsonForPrettyPrint().toJson(
								service.getAllChildrenForBranchTopNode(
									code, max)).getBytes());
	}
	
	public InputStream getChildCSVBytesForRestParams(String code, String max) {
						return new ByteArrayInputStream(new CSVUtility()
								.produceChildCSVOutputFromListWithHeading(service.getAllChildrenForBranchTopNode(
										code, max)).getBytes());
	}
	
	public InputStream getChildTabDelBytesForRestParams(String code, String max) {
		return new ByteArrayInputStream(new TabDelUtility()
				.produceChildTabDelOutputFromListWithHeading(service.getAllChildrenForBranchTopNode(
						code, max)).getBytes());
	}
	
	public ByteArrayInputStream getChildXSLBytesForRestParams(String code, String max) {
		try {
			return new ByteArrayInputStream(new ExcelUtility()
					.produceChildExcelOutputFromListWithHeading(service.getAllChildrenForBranchTopNode(
							code, max)).toByteArray());
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
	
	public InputStream getCSVBytesForRestParams(String codes, String props, String max) {
						return new ByteArrayInputStream(new CSVUtility()
								.produceCSVOutputFromListWithHeading(
										service.getResolvedChildFlatListFromTopNode( 
										codes, props, max) , props).getBytes());
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
