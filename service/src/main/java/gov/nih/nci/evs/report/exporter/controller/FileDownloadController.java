package gov.nih.nci.evs.report.exporter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

@RestController
@RequestMapping("/download")
public class FileDownloadController {
	
	@Autowired
	CodeReadService service;
	
	@GetMapping(
			  value = "/get-file/{id}/JsonFile.json",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFile(@PathVariable String id) throws IOException {
			    InputStream in = new ByteArrayInputStream(new Gson().toJson(
			    		service.getRestProperties(
			    				service.getRestTemplate(
			    						new RestTemplateBuilder()), 
			    				service.getCodes(id))).getBytes());
			    return IOUtils.toByteArray(in);
			}
	

}
