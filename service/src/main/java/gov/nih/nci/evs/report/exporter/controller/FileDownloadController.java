package gov.nih.nci.evs.report.exporter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutService;
import gov.nih.nci.evs.report.exporter.service.FormattedOutputService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@RestController
@RequestMapping("/download")
@CrossOrigin(origins = "http://localhost:8081")
public class FileDownloadController {
	
	public enum Formats{JSON,CSV,TABD,EXCEL};
	public enum BranchFormats{JSON,JSON_FLAT,CSV,TABD,EXCEL};
	
	
	@Autowired
	FormattedOutputService service;
	
	@Autowired
	FormattedBranchOutPutService branchService;
	
	@Autowired
	CodeReadService codeReadService;
	
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
		public List<String> getFormatOutput(){
			return Stream.of(Formats.values()).map(
					x -> x.name()).collect(Collectors.toList());
		}
		
		@GetMapping(
				  value = "/get-file-for-resolve-branch/{id}/{props}/{max}/{format}/{filename}",
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
		
		@GetMapping("/get-file-for-resolve-branch/{id}/{props}/{max}/EXCEL/{filename}")
		public ResponseEntity<InputStreamResource> fileReportForExcelBranch(@PathVariable String id, 
				@PathVariable String props, @PathVariable String max, @PathVariable String filename)  throws IOException {
		    ByteArrayInputStream in = branchService.getXSLBytesForRestParams(id, props, max);
		    // return IO ByteArray(in);
		    HttpHeaders headers = new HttpHeaders();
		    // set filename in header
		    headers.add("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
		    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		}
		
		

}
