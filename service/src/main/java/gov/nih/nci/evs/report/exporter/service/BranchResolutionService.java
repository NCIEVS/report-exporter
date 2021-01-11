package gov.nih.nci.evs.report.exporter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.CuratedTopNode;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class BranchResolutionService {
	
	@Autowired
	CodeReadService readService;
	
	@Autowired
	EVSAPIBaseService service;
	
	public List<ChildEntity> getChildrenForBranchTopNode(List<String> codes){
		return service.getChildrenForBranchTopNode(codes);
	}
	
	public List<ChildEntity> getAllChildrenForBranchTopNode(String code, String max){
		List<ChildEntity> entityList = new ArrayList<ChildEntity>();
		getUnprocessedChildrenForBranchTopNode(code, max)
		.stream()
		.forEach(x -> resolveChildEntityGraph( x, entityList));
		return entityList;
	}
	
	public List<ChildEntity> getUnprocessedChildrenForBranchTopNode(String code, String max){
		return service.getUnprocessedChildrenForBranchTopNode(code, max);
	}

	  
	public void resolveChildEntityGraph(ChildEntity child, List<ChildEntity> list){
		  
		  if(child != null &&!child.isLeaf() && child.getChildren() != null) {
			  child.getChildren()
			  .stream()
			  .forEach(x ->
			  resolveChildEntityGraph( x, list));}
		 
		if(!child.isLeaf()){child.setChildren(null);}
//		if(CommonServices.isChildParent(parent,child.getCode())) {
//			child.setParents(service.getRestParents(child.getCode()));
//		}
//		else{child.setParents(service.getRestParents(child.getCode()));}
		list.add(child);
	 }
	
	public List<RestEntity> getResolvedChildFlatListFromTopNode(
			String code, 
			String props, 
			String maximum){
		return getAllChildrenForBranchTopNode(code, maximum)
				.parallelStream()
				.map(x -> readService.getEntityForPropertyNameFilter(
				  readService.getRestEntityWithParent(x.getCode(), x.getParents()), 
						  CommonServices.splitInput(props))).collect(Collectors.toList());
	}
	
	public List<RestEntity> getResolvedChildFlatListFromTopNodeBatch(
			String code, 
			String props, 
			String maximum){
		//Partition and concat list of codes to a csv string
		List<String> listsOfCodes = Lists.partition(
				getAllChildrenForBranchTopNode(code, maximum)
				.stream()
				.map(x -> x.getCode()).collect(Collectors.toList()), 500).stream()
				.map(codes -> codes.stream()
						.collect(Collectors.joining(","))).collect(Collectors.toList());
		//retrieve a raw list of entities from the evs api
		List<RestEntity> entities = listsOfCodes
				.parallelStream()
				.map(codesToString -> 
				readService.getRestEntitiesWithParent(codesToString))
				.flatMap(x -> Arrays.asList(x)
						.stream())
				.collect(Collectors.toList());
		//return a list of entities with curated property set
		List<RestEntity> curatedEntities = entities.stream().map(entity -> 
		readService.getEntityForPropertyNameFilter(entity,
								CommonServices.splitInput(props))).collect(Collectors.toList());
//		curatedEntities.add( new RestEntity());
//		curatedEntities.add( new RestEntity());
//		curatedEntities.add( new RestEntity());
		RestEntity queryParamEntity = new RestEntity();
		queryParamEntity.setTerminology("Branch Resolution Query Parameters");
		queryParamEntity.setCode("|Codes: |" + code);
		queryParamEntity.setName("|Properties: " + props + "|Resolution depth: " + maximum + "|");
		curatedEntities.add( queryParamEntity);
		return curatedEntities;
	}
	
	public List<CuratedTopNode> getCuratedTopNodeList(){
		return Stream.of(
				service.getCuratedTopNodeList()
				.split(",")).map(x -> getTopNodeFromArray(x.split(":")))
				.collect(Collectors
						.toList());
	}
	
	public CuratedTopNode getTopNodeFromArray(String[] strings) {
		CuratedTopNode node = new CuratedTopNode();
		node.setCode(strings[0]);
		node.setName(strings[1]);
		return node;
	}

	public EVSAPIBaseService getService() {
		return service;
	}

	public void setService(EVSAPIBaseService service) {
		this.service = service;
	}

	
	
}
