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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import gov.nih.nci.evs.report.exporter.model.DeferredStatus;
import gov.nih.nci.evs.report.exporter.model.Format;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutService;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutServiceDeferredWrapper;
import gov.nih.nci.evs.report.exporter.service.FormattedOutputService;
import gov.nih.nci.evs.report.exporter.service.RoleService;
import gov.nih.nci.evs.report.exporter.service.TimedDeferredResultWrapper;
import gov.nih.nci.evs.report.exporter.util.CommonServices;
import gov.nih.nci.evs.report.exporter.util.TimedEvictionConcurrentMap;

@RestController
@RequestMapping("/download")
public class FileDownloadController {
	
	private static final Logger log = LoggerFactory.getLogger(FileDownloadController.class);
	
	@Value("${RESULT_TIME_OUT:3600000}")
	public int resultTimeOut;
	
	
	@Autowired
	FormattedOutputService service;
	
	@Autowired
	FormattedBranchOutPutService branchService;
	
	@Autowired
	FormattedBranchOutPutServiceDeferredWrapper deferredBranchService;
	
	@Autowired
	CodeReadService codeReadService;
	
	@Autowired
	RoleService roleService;

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
			public ResponseEntity<InputStreamResource>  getFileByFormat(@PathVariable String id,
					@PathVariable String props,
					@PathVariable String format, 
					@PathVariable String filename) {
						ByteArrayInputStream in;
						HttpHeaders headers = new HttpHeaders();
						CommonServices.Formats fmt = CommonServices.Formats.valueOf(format);
				switch(fmt) {
		            case JSON: 
		    			headers.add("Content-Disposition", "attachment; filename=" + filename + ".json");
		            	in = (ByteArrayInputStream) service.getJsonBytesForRestParams(id, props);
		    			break;
		            case CSV:
		    			headers.add("Content-Disposition", "attachment; filename=" + filename + ".csv");
		            	in = (ByteArrayInputStream)
					    		service.getCSVBytesForRestParams(id, props);
		            	break;
		            case TABD: 
		    			headers.add("Content-Disposition", "attachment; filename=" + filename + ".txt");
		            	in = (ByteArrayInputStream)
					    		service.getTabDelBytesForRestParams(id, props);
		    			break;
		            case EXCEL:
		    			headers.add("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
		            	in = service.getXSLBytesForRestParams(id, props);
		    			break;
		            default:
		    			headers.add("Content-Disposition", "attachment; filename=" + filename + ".json");
		            	in = (ByteArrayInputStream) service.getJsonBytesForRestParams(id, props);
				}
		            	return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

	}

	@GetMapping("/output-formats")
	public Format[] getFormatOutput(){
		return  CommonServices.formats;
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
				CommonServices.Formats fmt = CommonServices.Formats.valueOf(format);
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
	
	@GetMapping("/get-file-for-resolved-branch/{id}/{props}/{max}/EXCEL/{filename}")
	public ResponseEntity<InputStreamResource> fileReportForExcelBranch(@PathVariable String id, 
			@PathVariable String props, @PathVariable String max, @PathVariable String filename)  throws IOException {
	    ByteArrayInputStream in = branchService.getXSLBytesForRestParams(id, props, max);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
	    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	@RequestMapping(value = "deferred/getURLHashForDeferredResult/{id}/{props}/{max}/{format}")
	public @ResponseBody String getUrlForAsyncProcess(@PathVariable String id, @PathVariable String props,
			@PathVariable String max, @PathVariable String format) throws IOException {
		DeferredResult<byte[]> deferredResult;
		TimedDeferredResultWrapper timedDeferredResult;
		CommonServices.Formats fmt = CommonServices.Formats.valueOf(format);
		switch (fmt) {
		case JSON:
			deferredResult = deferredBranchService.getJsonBytesForRestParams(id, props, max);
			timedDeferredResult = new TimedDeferredResultWrapper(false, deferredResult, resultTimeOut, fmt);
			TimedEvictionConcurrentMap.getdRHash().put(String.valueOf(deferredResult.hashCode()), timedDeferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
		case CSV:
			deferredResult = deferredBranchService.getChildCSVBytesForRestParams(id, props, max);
			timedDeferredResult = new TimedDeferredResultWrapper(false, deferredResult, resultTimeOut, fmt);
			TimedEvictionConcurrentMap.getdRHash().put(String.valueOf(deferredResult.hashCode()), timedDeferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
		case TABD:
			deferredResult = deferredBranchService.getTabDelBytesForRestParams(id, props, max);
			timedDeferredResult = new TimedDeferredResultWrapper(false, deferredResult, resultTimeOut, fmt);
			TimedEvictionConcurrentMap.getdRHash().put(String.valueOf(deferredResult.hashCode()), timedDeferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
		case EXCEL:
			deferredResult = deferredBranchService.getXSLBytesForRestParams(id, props, max);
			timedDeferredResult = new TimedDeferredResultWrapper(false, deferredResult, resultTimeOut, fmt);
			TimedEvictionConcurrentMap.getdRHash().put(String.valueOf(deferredResult.hashCode()), timedDeferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());

		default:
			deferredResult = deferredBranchService.getJsonBytesForRestParams(id, props, max);
			timedDeferredResult = new TimedDeferredResultWrapper(false, deferredResult, resultTimeOut,fmt);
			TimedEvictionConcurrentMap.getdRHash().put(String.valueOf(deferredResult.hashCode()), timedDeferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
		}

	}

	@GetMapping("deferred/checkURLHashForDeferredStatus/{hash}")
	public DeferredStatus checkURLHashForDeferredStatus(@PathVariable String hash) {
		if(TimedEvictionConcurrentMap.getdRHash().get(hash) == null) {
			DeferredStatus status = new DeferredStatus(DeferredStatus.Status.EXPIRED, null);
			return status;
		}
		
		if (TimedEvictionConcurrentMap.getdRHash().get(hash).getResult().hasResult()) {
			DeferredStatus status = new DeferredStatus(
					DeferredStatus.Status.TRUE, 
					TimedEvictionConcurrentMap.getdRHash().get(hash).getFormat());
			return status;
		}
		else {
			DeferredStatus status = new DeferredStatus(
					DeferredStatus.Status.FALSE, 
					TimedEvictionConcurrentMap.getdRHash().get(hash).getFormat());
			return status;
		}
	}

	@GetMapping(value = "deferred/checkFileForHashFormatResponseEntity/{hash}/{format}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> getDeferredResponseEntityResult(@PathVariable String hash,
			@PathVariable String format, @PathVariable String fileName) {
//		log.info("Hash for download: " + hash);
//		log.info("Result wrapper exists? " + TimedEvictionConcurrentMap.getdRHash().containsKey(hash));
//		log.info("DeferredResult exists? " + (TimedEvictionConcurrentMap.getdRHash().get(hash) != null));
//		log.info("Result exists? " + TimedEvictionConcurrentMap.getdRHash().get(hash).getResult().hasResult());
		ByteArrayInputStream in = null;
		try {
		 in = new ByteArrayInputStream((byte[]) TimedEvictionConcurrentMap.getdRHash()
				.remove(hash)
				.getResult()
				.getResult());
		}catch(NullPointerException n) {
			log.info("Failed on null pointer exception for: " + hash);
		}
		
		HttpHeaders headers = new HttpHeaders();

		CommonServices.Formats fmt = CommonServices.Formats.valueOf(format);
		switch (fmt) {
		case JSON:
			headers.add("Content-Disposition", "attachment; filename=" + fileName + ".json");
			break;
		case CSV:
			headers.add("Content-Disposition", "attachment; filename=" + fileName + ".csv");
			break;
		case TABD:
			headers.add("Content-Disposition", "attachment; filename=" + fileName + ".txt");
			break;
		case EXCEL:
			headers.add("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
			break;
		default:
			headers.add("Content-Disposition", "attachment; filename=" + fileName + ".json");
		}
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
	
	@GetMapping(
			  value = "/get-minfile-for-resolved-branch/{id}/{props}/{max}/{format}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getMinimalFileByFormatForBranch(@PathVariable String id,
					@PathVariable String props,
					@PathVariable String max,
					@PathVariable String format,
					@PathVariable String filename) throws IOException {
		CommonServices.Formats fmt = CommonServices.Formats.valueOf(format);
		switch(fmt) {
            case JSON: 
            	return IOUtils.toByteArray(
         			    branchService.getJsonBytesForRestChildParams(id, max));
            case CSV:
			    return IOUtils.toByteArray(
			    		branchService.getChildCSVBytesForRestParams(id, max));
            case TABD: 
			    return IOUtils.toByteArray(
			    		branchService.getChildTabDelBytesForRestParams(id, max));
            default:
            	return IOUtils.toByteArray(
            			branchService.getJsonBytesForRestChildParams(id, max));
		}
			}
	

	
	@GetMapping("/get-minfile-for-resolved-branch/{id}/{props}/{max}/EXCEL/{filename}")
	public ResponseEntity<InputStreamResource> minimalFileReportForExcelBranch(@PathVariable String id, 
			@PathVariable String props, @PathVariable String max, @PathVariable String filename)  throws IOException {
	    ByteArrayInputStream in = branchService.getChildXSLBytesForRestParams(id, max);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
	    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
	
	@GetMapping(
			  value = "/get-file-for-resolved-roles/{codes}/{roles}/{format}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getRoleFileByFormatForBranch(@PathVariable String codes,
					@PathVariable String roles,
					@PathVariable String format,
					@PathVariable String filename) throws IOException {
				CommonServices.Formats fmt = CommonServices.Formats.valueOf(format);
				switch(fmt) {
		            case JSON: 
		            	return IOUtils.toByteArray(
		         			    roleService.getJsonBytesForRestRoleParams(codes));
		            case CSV:
					    return IOUtils.toByteArray(
					    		roleService.getChildCSVBytesForRestRoleParams(codes, roles));
		            case TABD: 
					    return IOUtils.toByteArray(
					    		roleService.getChildTabDelBytesForRestRoleParams(codes));
		            default:
		            	return IOUtils.toByteArray(
		            			roleService.getJsonBytesForRestRoleParams(codes));
				}

			}
	

}
