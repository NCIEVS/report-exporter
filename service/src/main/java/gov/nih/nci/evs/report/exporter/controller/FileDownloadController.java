package gov.nih.nci.evs.report.exporter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.evs.report.exporter.service.CodeReadService;

@RestController
@RequestMapping("/download")
public class FileDownloadController {
	
	@Autowired
	CodeReadService service;
	
	@GetMapping(
			  value = "/get-file/JsonFile.json",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFile() throws IOException {
			    InputStream in = new ByteArrayInputStream("this is json or something".getBytes());
			    return IOUtils.toByteArray(in);
			}
	

}
