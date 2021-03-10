package gov.nih.nci.evs.report.exporter.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nih.nci.evs.report.exporter.model.Root;

@Service
public class RestRootNodeService {

	@Autowired
	EVSAPIBaseService service;
	
	
	@Value("${REST_ROOT_FILTER_LIST:C28428}")
	private String rootFilterList;

	
	public Root[] getRestRoots(RestTemplate template){
		return Stream.of(service.getRestRoots(template))
				.filter(x -> !x.getCode().equals(service.getRootFilterList()))
				.toArray(Root[]::new);
	}


	public EVSAPIBaseService getService() {
		return service;
	}


	public void setService(EVSAPIBaseService service) {
		this.service = service;
	}
	
	

}

