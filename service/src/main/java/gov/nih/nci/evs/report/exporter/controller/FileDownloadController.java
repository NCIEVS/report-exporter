package gov.nih.nci.evs.report.exporter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import gov.nih.nci.evs.report.exporter.model.Format;
import gov.nih.nci.evs.report.exporter.service.FormattedBranchOutPutServiceDeferredWrapper;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

	public enum Formats {
		JSON, CSV, TABD, EXCEL
	};

	public Format[] formats = new Format[] {
			new Format(Formats.JSON.name(), "JavaScript Object Notation Format", "json"),
			new Format(Formats.CSV.name(), "Comma Separated Value Format", "csv"),
			new Format(Formats.TABD.name(), "Tab Delimited Value Format", "txt"),
			new Format(Formats.EXCEL.name(), "Microsoft Excel Format", "xlsx") };

	@Autowired
	FormattedBranchOutPutServiceDeferredWrapper deferredBranchService;

	private ConcurrentHashMap<Integer, DeferredResult<byte[]>> dRHash = new ConcurrentHashMap<Integer, DeferredResult<byte[]>>();

	@RequestMapping(value = "deferred/getURLHashForDeferredResult/{id}/{props}/{max}/{format}")
	public @ResponseBody String getUrlForAsyncProcess(@PathVariable String id, @PathVariable String props,
			@PathVariable String max, @PathVariable String format) throws IOException {
		DeferredResult<byte[]> deferredResult;
		Formats fmt = Formats.valueOf(format);
		switch (fmt) {
		case JSON:
			deferredResult = deferredBranchService.getJsonBytesForRestParams(id, props, max);
			dRHash.put(deferredResult.hashCode(), deferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
		case CSV:
			deferredResult = deferredBranchService.getChildCSVBytesForRestParams(id, props, max);
			dRHash.put(deferredResult.hashCode(), deferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());

		case TABD:
			deferredResult = deferredBranchService.getTabDelBytesForRestParams(id, props, max);
			dRHash.put(deferredResult.hashCode(), deferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
		case EXCEL:
			deferredResult = deferredBranchService.getXSLBytesForRestParams(id, props, max);
			dRHash.put(deferredResult.hashCode(), deferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());

		default:
			deferredResult = deferredBranchService.getJsonBytesForRestParams(id, props, max);
			dRHash.put(deferredResult.hashCode(), deferredResult);
			return "deferred/checkURLHashForDeferredStatus/" + Integer.toString(deferredResult.hashCode());
		}

	}

	@GetMapping("deferred/checkURLHashForDeferredStatus/{hash}")
	public String checkURLHashForDeferredStatus(@PathVariable String hash) {
		return String.valueOf(dRHash.get(new Integer(hash)).hasResult());
	}

	@GetMapping(value = "deferred/checkFileForHashFormatResponseEntity/{hash}/{format}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> getDeferredResponseEntityResult(@PathVariable String hash,
			@PathVariable String format, @PathVariable String fileName) {
		ByteArrayInputStream in = new ByteArrayInputStream((byte[]) dRHash.remove(new Integer(hash)).getResult());
		HttpHeaders headers = new HttpHeaders();

		Formats fmt = Formats.valueOf(format);
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

}
