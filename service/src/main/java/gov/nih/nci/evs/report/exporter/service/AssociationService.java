package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.Association;
import gov.nih.nci.evs.report.exporter.model.Rel;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.util.CommonServices;
import gov.nih.nci.evs.report.exporter.util.DelimitedRoleOutputUtility;
import gov.nih.nci.evs.report.exporter.util.ExcelUtility;
import gov.nih.nci.evs.report.exporter.util.JSONUtility;

@Service
public class AssociationService {
	
	@Autowired
	EVSAPIBaseService service;
	
	DelimitedRoleOutputUtility utility;
	
	public List<Association> getAssociationsForCode(String code){
		return service.getRestAssociation(code);
	}
	
	public List<Rel> getDistinctWeightedRelsForEntityCodes(List<? extends Rel> rawRels){
		Hashtable<String,Rel> distinctRels = new Hashtable<String,Rel>();	
		rawRels.stream().forEach(x -> saveOrUpdateWeightedRels(x, distinctRels));
		return distinctRels.values().stream().collect(Collectors.toList());
	}
	
	public void saveOrUpdateWeightedRels(Rel rel, Hashtable<String, Rel> wRels) {
		Rel rStored = wRels.get(rel.getType());
		rel.setWeight(1);
		if(rStored == null){wRels.put(rel.getType(), rel);
		}else {rStored.setWeight(rStored.getWeight() + 1);
		}
	}
	
	
	public RestEntity getRestEntityForRelNode(String code) {
		return service.getEntity(code);
	}
	
	public RestEntity getRestAssociationEntityForRoleNode(String code) {

		RestEntity entity = new RestEntity();
		
		RestEntity rEntity = service.getEntity(code);
		if(rEntity == null) {throw new RuntimeException("Code has no associations or does not exist");}
		entity.setCode(rEntity.getCode());
		entity.setName(rEntity.getName());
		entity.setAssociations(service.getRestAssociation(code));
		
		return entity;
	}
	
	public List<RestEntity> getRestRelEntitiesForRelNode(String codes) {
		return CommonServices.splitInput(codes)
				.stream()
				.map(code -> getRestAssociationEntityForRoleNode(code))
				.collect(Collectors.toList());
		
	}

	public List<? extends Rel> getAssociationsForCodes(String codes) {
		return CommonServices.splitInput(codes)
				.stream()
				.map(code -> getAssociationsForCode(code)).flatMap(List::stream).
				collect(Collectors.toList());
	}
	
	public List<Rel> sortRelListByWeight(List<Rel> rels){
		Collections.sort(rels, new Comparator<Rel>() {            @Override
            public int compare(Rel r1, Rel r2) {
            return r2.getWeight() - r1.getWeight();
        }});
		return rels;
	}
	
	public List<? extends Rel> getSortedRels(List<? extends Rel> rels){
		return sortRelListByWeight(getDistinctWeightedRelsForEntityCodes(rels));
	}
	
	public List<? extends Rel> getSortedAssociations(String codes){
		return getSortedRels(getAssociationsForCodes(codes));
	}

	public InputStream getChildCSVBytesForRestAssociationParams(String codes, String associations) {
		List<RestEntity> entities =  filterEntitiesAndRelsforRels(associations,getRestRelEntitiesForRelNode(codes));
		return new ByteArrayInputStream( 
				new DelimitedRoleOutputUtility().produceDelimitedAssociationOutputFromListWithHeading(entities, associations, codes, ",").getBytes());
	}
	
	public InputStream getChildTabDelBytesForRestAssociationParams(String codes, String associations) {
		List<RestEntity> entities =  filterEntitiesAndRelsforRels(associations,getRestRelEntitiesForRelNode(codes));
		return new ByteArrayInputStream( 
				new DelimitedRoleOutputUtility().produceDelimitedAssociationOutputFromListWithHeading(entities, associations, codes, "\t").getBytes());
	}

	private List<RestEntity> filterEntitiesAndRelsforRels(String associations, List<RestEntity> entities) {
		return entities.stream()
				.filter(x -> entityContainAssociationFilter(associations, x))
				.collect(Collectors.toList());
	}
	
	private boolean entityContainAssociationFilter(String associations, RestEntity entity) {
		List<String> associationList = CommonServices.splitInput(associations);
		List<Association> associationModelList = entity
				.getAssociations()
				.stream()
				.filter(x -> associationList.contains(x.getType()))
				.collect(Collectors.toList());
		if(associationModelList == null || associationModelList.size() == 0) {
		return false;
		}
		else {
			entity.setAssociations(associationModelList);
			return true;
		}
	}

	public InputStream getJsonBytesForRestAssociationParams(String codes, String associations) {
		return new ByteArrayInputStream(new JSONUtility().produceJsonRoleOutputFromListWithHeading(
				filterEntitiesAndRelsforRels(associations,getRestRelEntitiesForRelNode(codes)), associations, codes).getBytes());
	}

	public ByteArrayInputStream getChildExcelBytesForRestAssociationParams(String codes, String associations) throws IOException {
		return new ByteArrayInputStream(new ExcelUtility().produceExcelAssociationOutputFromListWithHeading(
				filterEntitiesAndRelsforRels(associations,getRestRelEntitiesForRelNode(codes)), associations, codes).toByteArray());
	}

}
