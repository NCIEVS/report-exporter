package gov.nih.nci.evs.report.exporter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Root;

@Service
public class RestRootNodeService {

	@Autowired
	EVSAPIBaseService service;

	
	public Root[] getRestRoots(RestTemplate template){
		return service.getRestRoots(template);
	}

}

