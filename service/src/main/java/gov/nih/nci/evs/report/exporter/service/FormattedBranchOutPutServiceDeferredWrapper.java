package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import gov.nih.nci.evs.report.exporter.ReportExplorerApplication;
import gov.nih.nci.evs.report.exporter.util.CSVUtility;
import gov.nih.nci.evs.report.exporter.util.CommonServices;
import gov.nih.nci.evs.report.exporter.util.ExcelUtility;
import gov.nih.nci.evs.report.exporter.util.TabDelUtility;

@Service
public class FormattedBranchOutPutServiceDeferredWrapper {
	
	private static final Logger log = LoggerFactory.getLogger(FormattedBranchOutPutServiceDeferredWrapper.class);
	
	
	@Autowired
	BranchResolutionService service;

	
	public InputStream getJsonBytesForRestChildParams(String code, String max) {
		return new ByteArrayInputStream(
				CommonServices.getGsonForPrettyPrint().toJson(
								service.getAllChildrenForBranchTopNode(
									code, max)).getBytes());
	}
	
	public DeferredResult<byte[]> getChildCSVBytesForRestParams(String code, String props, String max) {
		final DeferredResult<byte[]> deferredStream = new DeferredResult<byte[]>();

			startThread(deferredStream, code, props, max);

			log.info("Request processing finished");

			return deferredStream;
	}
	
	private void startThread(DeferredResult<byte[]> deferredStream, String code, String props, String max){
	new Thread(() -> {
		log.info("Stream processing thread started");
		deferredStream.setResult(new CSVUtility()
				.produceCSVOutputFromListWithHeading(
						service.getResolvedChildFlatListFromTopNode( 
						code, props, max) ,props).getBytes());
	}).start();
	log.info("Stream processing thread complete");
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
										codes, props, max) ,props).getBytes());
	}
	
	public InputStream getTabDelBytesForRestParams(String codes, String props,String max) {
		return new ByteArrayInputStream(new TabDelUtility()
				.produceTabDelOutputFromListWithHeading(
						service.getResolvedChildFlatListFromTopNode( 
								codes, props, max), props).getBytes());
	}
	
	public ByteArrayInputStream getXSLBytesForRestParams(String codes, String props, String max) {
		try {
			return new ByteArrayInputStream(new ExcelUtility()
					.produceExcelOutputFromListWithHeading(
							service.getResolvedChildFlatListFromTopNode( 
									codes, props, max), props).toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Input/Output failure in Excel formatted output: ",e);
		}
	}

}
