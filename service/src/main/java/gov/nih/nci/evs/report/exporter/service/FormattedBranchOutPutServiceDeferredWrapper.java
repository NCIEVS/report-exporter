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
import gov.nih.nci.evs.report.exporter.util.JSONUtility;

@Service
public class FormattedBranchOutPutServiceDeferredWrapper {
	
	private static final Logger log = LoggerFactory.getLogger(FormattedBranchOutPutServiceDeferredWrapper.class);
	
	
	@Autowired
	BranchResolutionService service;
	
	
	
	public DeferredResult<byte[]> getChildCSVBytesForRestParams(String code, String props, String max) {
		final DeferredResult<byte[]> deferredStream = new DeferredResult<byte[]>();

			startCSVThread(deferredStream, code, props, max);

			log.info("Request processing finished");

			return deferredStream;
	}
	
	private void startCSVThread(DeferredResult<byte[]> deferredStream, String code, String props, String max){
	new Thread(() -> {
		log.info("Stream processing thread started");
		deferredStream.setResult(new CSVUtility()
				.produceCSVOutputFromListWithHeading(
						service.getResolvedChildFlatListFromTopNodeBatch( 
						code, props, max) ,props, code, Integer.valueOf(max)).getBytes());
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
	
	public DeferredResult<byte[]> getJsonBytesForRestParams(String code, String props, String max) {
		final DeferredResult<byte[]> deferredStream = new DeferredResult<byte[]>();
		
		startJSONThread(deferredStream, code, props, max);

		log.info("Request processing finished");
		
		return deferredStream;

	}
	
	private void startJSONThread(DeferredResult<byte[]> deferredStream, String codes, String props, String max){
	new Thread(() -> {
		log.info("Stream processing JSON thread started");
		deferredStream.setResult(new JSONUtility().produceJsonOutputFromListWithHeading(
				service.getResolvedChildFlatListFromTopNodeBatch( 
						codes, props, max),props, codes, Integer.valueOf(max)).getBytes());
	}).start();
	log.info("Stream processing JSON thread complete");
	}
	
	public InputStream getCSVBytesForRestParams(String codes, String props, String max) {
						return new ByteArrayInputStream(new CSVUtility()
								.produceCSVOutputFromListWithHeading(
										service.getResolvedChildFlatListFromTopNodeBatch( 
										codes, props, max) ,props, codes, Integer.valueOf(max)).getBytes());
	}
	
	public DeferredResult<byte[]> getTabDelBytesForRestParams(String code, String props,String max) {
		
		final DeferredResult<byte[]> deferredStream = new DeferredResult<byte[]>();
		
		startTABDelThread(deferredStream, code, props, max);

		log.info("Request processing finished");
		
		return deferredStream;
		
	}
	
	private void startTABDelThread(DeferredResult<byte[]> deferredStream, String codes, String props, String max){
	new Thread(() -> {
		log.info("Stream processing TAB Delimited thread started");
		deferredStream.setResult(new TabDelUtility()
				.produceTabDelOutputFromListWithHeading(
						service.getResolvedChildFlatListFromTopNodeBatch( 
								codes, props, max), props, codes, Integer.valueOf(max)).getBytes());
	}).start();
	log.info("Stream processing TAB Delimited  complete");
	}
	
	public DeferredResult<byte[]> getXSLBytesForRestParams(String code, String props, String max) {
		
		final DeferredResult<byte[]> deferredStream = new DeferredResult<byte[]>();
		
		startExcelThread(deferredStream, code, props, max);

		log.info("Request processing finished");
		
		return deferredStream;

	}
	
	private void startExcelThread(DeferredResult<byte[]> deferredStream, String codes, String props, String max){
	new Thread(() -> {
		log.info("Stream processing Excel thread started");
		try {
			deferredStream.setResult(new ExcelUtility()
					.produceExcelOutputFromListWithHeading(
							service.getResolvedChildFlatListFromTopNodeBatch( 
									codes, props, max), props, codes, Integer.valueOf(max)).toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}).start();
	log.info("Stream processing Excel complete");
	}

}
