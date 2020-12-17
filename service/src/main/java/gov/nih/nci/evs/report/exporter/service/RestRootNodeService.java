package gov.nih.nci.evs.report.exporter.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Root;

@Service
public class RestRootNodeService {

	@Autowired
	EVSAPIBaseService service;

	
	public Root[] getRestRoots(RestTemplate template){
		return Stream.of(service.getRestRoots(template))
				.filter(x -> !x.getCode().equals("C28428"))
				.toArray(Root[]::new);
	}

}

