package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public DeferredResult<InputStream> getChildCSVBytesForRestParams(String code, String max) {
		final DeferredResult<InputStream> deferredStream = new DeferredResult<InputStream>();


			startThread(deferredStream, code, max);

			log.info("Request processing finished");

			return deferredStream;
	}
	
	private void startThread(DeferredResult<InputStream> deferredStream, String code, String max){
	new Thread(() -> {
		log.info("Stream processing thread started");
		deferredStream.setResult(new ByteArrayInputStream(new CSVUtility()
				.produceChildCSVOutputFromListWithHeading(service.getAllChildrenForBranchTopNode(
						code, max)).getBytes()));
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
