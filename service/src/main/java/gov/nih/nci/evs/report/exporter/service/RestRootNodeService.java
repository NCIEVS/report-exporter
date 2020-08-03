package gov.nih.nci.evs.report.exporter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Root;

@Service
public class RestRootNodeService {
	@Value("${BASE_URL}")
	private String baseURL;


	
	public Root[] getRestRoots(RestTemplate template){
		Root[] roots = 
				 template
		.getForObject(
		 baseURL + "/roots"
				,Root[].class);
		return roots;
	}

}

