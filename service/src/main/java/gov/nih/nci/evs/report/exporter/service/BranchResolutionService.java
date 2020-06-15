package gov.nih.nci.evs.report.exporter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class BranchResolutionService {
	
	public List<ChildEntity> getChildrenForBranchTopNode(List<String> codes){
		return 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				"https://api-evsrest-dev.nci.nih.gov/api/v1/concept/ncit/"  + code + "/children"
						,ChildEntity[].class)).flatMap(Arrays::stream)
						.collect(Collectors.toList());
	}
	
	public List<ChildEntity> getAllChildrenForBranchTopNode(List<String> codes, String max){
		List<ChildEntity> entityList = new ArrayList<ChildEntity>();
		getUnprocessedChildrenForBranchTopNode(codes, max)
		.stream()
		.forEach(x -> resolveChildEntityGraph(x, entityList));
		return entityList;
	}
	
	public List<ChildEntity> getUnprocessedChildrenForBranchTopNode(List<String> codes, String max){
		return 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				"https://api-evsrest-dev.nci.nih.gov/api/v1/concept/ncit/"  
				+ code 
				+ "/descendants?maxLevel=" + max
						,ChildEntity[].class))
							.flatMap(Arrays::stream)
							.collect(Collectors.toList());
	}

	  
	public List<ChildEntity> resolveChildEntityGraph(ChildEntity child, List<ChildEntity> list){
		  if(child.isLeaf()) {list.add(child); return list;}
		  else child.getChildren().stream().forEach(x -> resolveChildEntityGraph(x, list));
	    return list;  
	}

}
