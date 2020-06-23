package gov.nih.nci.evs.report.exporter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class BranchResolutionService {
	
	@Autowired
	CodeReadService readService;
	
    @Value("${NODE_LIST}")
	private String curatedTopNodeList;
    
    @Value("${BASE_URL}")
    private String baseURL;
    
    @Value("${CHILDREN}")
    private String children;
    
    @Value("${DESCENDANTS}")
    private String descendants;
    
	
	public List<ChildEntity> getChildrenForBranchTopNode(List<String> codes){
		return 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				baseURL  + code + children
						,ChildEntity[].class)).flatMap(Arrays::stream)
						.collect(Collectors.toList());
	}
	
	public List<ChildEntity> getAllChildrenForBranchTopNode(List<String> codes, String max){
		List<ChildEntity> entityList = new ArrayList<ChildEntity>();
		getUnprocessedChildrenForBranchTopNode(codes, max)
		.stream()
		.forEach(x -> resolveChildEntityGraph(x.getCode() + ":" + x.getName(), x, entityList));
		return entityList;
	}
	
	public List<ChildEntity> getUnprocessedChildrenForBranchTopNode(List<String> codes, String max){
		return 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				baseURL 
				+ code 
				+ descendants + max
						,ChildEntity[].class))
							.flatMap(Arrays::stream)
							.collect(Collectors.toList());
	}

	  
	public void resolveChildEntityGraph(String parent, ChildEntity child, List<ChildEntity> list){
		  
		  if(child != null &&!child.isLeaf() && child.getChildren() != null) {
			  child.getChildren()
			  .stream()
			  .forEach(x ->
			  resolveChildEntityGraph(child.getCode() + ":" + child.getName(), x, list));}
		 
		if(!child.isLeaf()){child.setChildren(null); child.setParent(parent);}
		child.setParent(parent);
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
	
	public List<String> getCuratedTopNodeList(){
		return Stream.of(
				curatedTopNodeList
				.split(","))
				.collect(Collectors
						.toList());
	}
	
	
}
