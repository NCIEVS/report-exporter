package gov.nih.nci.evs.report.exporter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/download")
@CrossOrigin(origins = "http://localhost:8081")
public class FileDownloadController {
	
	public enum formats{JSON,CSV,TABD, EXCEL};
	
	
	@Autowired
	CodeReadService service;
	
	@GetMapping(
			  value = "/get-file/{id}/JsonFile.json",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFile(@PathVariable String id) throws IOException {
			    InputStream in = new ByteArrayInputStream(service.getGsonForPrettyPrint().toJson(
			    		service.getRestProperties( 
			    				service.getCodes(id))).getBytes());
			    return IOUtils.toByteArray(in);
			}
	
	@GetMapping(
			  value = "/get-file-for-props/{codes}/{props}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFileForProps(
					@PathVariable String codes, 
					@PathVariable String props) throws IOException {
			   // service.getJsonBytesForRestParams(codes, props);
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
			   // service.getCSVBytesForRestParams(codes, props);
			    return IOUtils.toByteArray(
			    		service.getCSVBytesForRestParams(codes, props));
			}
	
	
		@GetMapping("/output-formats")
		public List<String> getFormatOutput(){
			return Stream.of(formats.values()).map(
					x -> x.name()).collect(Collectors.toList());
		}

}
