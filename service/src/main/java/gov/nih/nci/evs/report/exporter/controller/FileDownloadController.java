package gov.nih.nci.evs.report.exporter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResult.DeferredResultHandler;

import gov.nih.nci.evs.report.exporter.model.Format;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutService;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutServiceDeferredWrapper;
import gov.nih.nci.evs.report.exporter.service.FormattedOutputService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
@RequestMapping("/download")
public class FileDownloadController {
	
	public enum Formats{JSON,CSV,TABD,EXCEL};
	public enum BranchFormats{JSON,JSON_FLAT,CSV,TABD,EXCEL};
	
	
	public Format[] formats = new Format[]
			{new Format(Formats.JSON.name(), "JavaScript Object Notation Format", "json" ),
			 new Format(Formats.CSV.name(), "Comma Separated Value Format", "csv" ),
			 new Format(Formats.TABD.name(), "Tab Delimited Value Format", "txt" ),
			 new Format(Formats.EXCEL.name(), "Microsoft Excel Format", "xlsx" )};
	
	@Autowired
	FormattedOutputService service;
	
	@Autowired
	FormattedBranchOutPutService branchService;
	
	@Autowired
	FormattedBranchOutPutServiceDeferredWrapper deferredBranchService;
	
	@Autowired
	CodeReadService codeReadService;

	private ConcurrentHashMap<Integer, DeferredResult<byte[]>> dRHash = new ConcurrentHashMap<Integer, DeferredResult<byte[]>>();
	
	@GetMapping(
			  value = "/get-file/{id}/JsonFile.json",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFile(@PathVariable String id) throws IOException {
			    InputStream in = new ByteArrayInputStream(CommonServices.getGsonForPrettyPrint().toJson(
			    		codeReadService.getRestEntities( 
			    				CommonServices.splitInput(id))).getBytes());
			    return IOUtils.toByteArray(in);
			}
	
	@GetMapping(
			  value = "/get-file-for-readCodes/{id}/{props}/{format}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFileByFormat(@PathVariable String id,
					@PathVariable String props,
					@PathVariable String format, 
					@PathVariable String filename) throws IOException {
				Formats fmt = Formats.valueOf(format);
				switch(fmt) {
		            case JSON: 
		            	return IOUtils.toByteArray(
		         			    		service.getJsonBytesForRestParams(id, props));
		            case CSV:
					    return IOUtils.toByteArray(
					    		service.getCSVBytesForRestParams(id, props));
		            case TABD: 
					    return IOUtils.toByteArray(
					    		service.getTabDelBytesForRestParams(id, props));
		            default:
		            	return IOUtils.toByteArray(new ByteArrayInputStream(CommonServices.getGsonForPrettyPrint().toJson(
					    		codeReadService.getRestEntities( 
					    				CommonServices.splitInput(id))).getBytes()));
				}

			}
	
	@GetMapping("/get-file-for-readCodes/{id}/{props}/EXCEL/{filename}")
	public ResponseEntity<InputStreamResource> excelCustomersByFormatReport(@PathVariable String id, 
			@PathVariable String props, @PathVariable String filename)  throws IOException {
	    ByteArrayInputStream in = service.getXSLBytesForRestParams(id, props);
	    // return IO ByteArray(in);
	    HttpHeaders headers = new HttpHeaders();
	    // set filename in header
	    headers.add("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
	    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
	
	
	
	
	@GetMapping(
			  value = "/get-file-for-props/{codes}/{props}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFileForProps(
					@PathVariable String codes, 
					@PathVariable String props) throws IOException {
			    return IOUtils.toByteArray(
			    		service.getJsonBytesForRestParams(codes, props));
			}
	
	@GetMapping(
			  value = "/get-file-for-csv/{codes}/{props}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFileForCSV(
					@PathVariable String codes, 
					@PathVariable String props) throws IOException {
			    return IOUtils.toByteArray(
			    		service.getCSVBytesForRestParams(codes, props));
			}
	
	@GetMapping(
			  value = "/get-file-for-tabdel/{codes}/{props}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFileForTabDel(
					@PathVariable String codes, 
					@PathVariable String props) throws IOException {
			    return IOUtils.toByteArray(
			    		service.getTabDelBytesForRestParams(codes, props));
			}
	
	@GetMapping("/get-file-for-excel/{codes}/{props}/{filename}")
	public ResponseEntity<InputStreamResource> excelCustomersReport(@PathVariable String codes, 
			@PathVariable String props, @PathVariable String filename)  throws IOException {
	    ByteArrayInputStream in = service.getXSLBytesForRestParams(codes, props);
	    // return IO ByteArray(in);
	    HttpHeaders headers = new HttpHeaders();
	    // set filename in header
	    headers.add("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
	    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
	
	
	
		@GetMapping("/output-formats")
		public Format[] getFormatOutput(){
			return  formats;
		}
		
		@GetMapping(
				  value = "/get-file-for-resolved-branch/{id}/{props}/{max}/{format}/{filename}",
				  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
				)
				public @ResponseBody byte[] getFileByFormatForBranch(@PathVariable String id,
						@PathVariable String props,
						@PathVariable String max,
						@PathVariable String format,
						@PathVariable String filename) throws IOException {
					Formats fmt = Formats.valueOf(format);
					switch(fmt) {
			            case JSON: 
			            	return IOUtils.toByteArray(
			         			    branchService.getJsonBytesForRestParams(id, props, max));
			            case CSV:
						    return IOUtils.toByteArray(
						    		branchService.getCSVBytesForRestParams(id, props, max));
			            case TABD: 
						    return IOUtils.toByteArray(
						    		branchService.getTabDelBytesForRestParams(id, props, max));
			            default:
			            	return IOUtils.toByteArray(
			            			branchService.getJsonBytesForRestParams(id, props, max));
					}

				}
				
				
				@RequestMapping(
						  value = "deferred/getURLHashForDeferredResult/{id}/{props}/{max}/{format}/{filename}" 
						)
						public @ResponseBody String getUrlForAsyncProcess(@PathVariable String id,
								@PathVariable String props,
								@PathVariable String max,
								@PathVariable String format,
								@PathVariable String filename) throws IOException {
					DeferredResult<byte[]> deferredResult;
							Formats fmt = Formats.valueOf(format);
							switch(fmt) {
					            case JSON: 
					            	 deferredResult = 
					            			deferredBranchService.getJsonBytesForRestParams(id, props, max);
										    dRHash.put(deferredResult.hashCode(), deferredResult);
										    return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
					            case CSV:
								   deferredResult = 
								    		deferredBranchService.getChildCSVBytesForRestParams(id, props, max);
								    		dRHash.put(deferredResult.hashCode(), deferredResult);
								    		return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());

								 case TABD: 
									 deferredResult = 
								    		deferredBranchService.getTabDelBytesForRestParams(id, props, max);
										    dRHash.put(deferredResult.hashCode(), deferredResult);
										    return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
								 case EXCEL: 
									 deferredResult = 
								    		deferredBranchService.getXSLBytesForRestParams(id, props, max);
										    dRHash.put(deferredResult.hashCode(), deferredResult);
										    return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
					            
								 default:
					            	 deferredResult = 
					            			deferredBranchService.getJsonBytesForRestParams(id, props, max);
										    dRHash.put(deferredResult.hashCode(), deferredResult);
										    return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
							}
								   
						}
		
		@GetMapping("deferred/checkURLHashForDeferredStatus/{hash}")
				public String checkURLHashForDeferredStatus(@PathVariable String hash){
					return String.valueOf(dRHash.get(new Integer(hash)).hasResult());
				}
		
		
		@GetMapping(value = "deferred/checkFileForHashFormat/{hash}/{format}/{fileName}",
				produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
		public @ResponseBody byte[] getDeferredResult(@PathVariable String hash, @PathVariable String format, @PathVariable String fileName){
			return(byte[]) dRHash.remove(new Integer(hash)).getResult();
		}
		
		@GetMapping(value = "deferred/checkFileForHashFormat/{hash}/EXCEL/{fileName}",
				produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
		public ResponseEntity<InputStreamResource> getDeferredExcelResult(@PathVariable String hash, @PathVariable String fileName){
			ByteArrayInputStream in = new ByteArrayInputStream((byte[]) dRHash.remove(new Integer(hash)).getResult());
		    HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
		    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		}

}
