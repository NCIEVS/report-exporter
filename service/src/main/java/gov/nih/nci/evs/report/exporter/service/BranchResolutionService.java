package gov.nih.nci.evs.report.exporter.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class BranchResolutionService {
	
	public List<ChildEntity> getEnitiesForBranchTopNode(List<String> codes){
		return 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				"https://api-evsrest-dev.nci.nih.gov/api/v1/concept/ncit/"  + code + "/children"
						,ChildEntity[].class)).flatMap(Arrays::stream)
						.collect(Collectors.toList());
	}

}
