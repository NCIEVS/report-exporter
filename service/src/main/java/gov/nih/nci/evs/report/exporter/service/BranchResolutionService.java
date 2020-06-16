package gov.nih.nci.evs.report.exporter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class BranchResolutionService {
	
	@Autowired
	CodeReadService readService;
	
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

	  
	public void resolveChildEntityGraph(ChildEntity child, List<ChildEntity> list){
		  
		  if(child != null &&!child.isLeaf() && child.getChildren() != null) {
			  child.getChildren()
			  .stream()
			  .forEach(x ->
			  resolveChildEntityGraph(x, list));}
		 
		if(!child.isLeaf()){child.setChildren(null);}
		list.add(child);
	 }
	
	public List<RestEntity> getResolvedChildFlatListFromTopNode(String codes, String props, String maximum){
		return getAllChildrenForBranchTopNode(CommonServices.splitInput(codes), maximum)
				.parallelStream()
				.map(x -> readService.getEntitiesForPropertyNameFilter(
				  readService.getRestEntities(
						  CommonServices.getCodesListForCode(x.getCode())), 
						  CommonServices.splitInput(props)))
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}
}
