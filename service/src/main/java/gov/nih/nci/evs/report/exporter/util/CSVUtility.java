package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class CSVUtility {
	

	public String produceCSVOutputFromListWithHeading(List<RestEntity> list) {
		CommonServices services = new CommonServices();
		StringBuffer firstLine = new StringBuffer();
		String separator = ",";
		Field[] fields = RestEntity.class.getDeclaredFields();
		StringBuffer oneLine = new StringBuffer();
		list.stream().forEach(x -> { x.getProperties()
			.stream()
			.forEach(z -> services.addPropertyTypeAndPositionToCache(z));       
				oneLine.append(
				"\r\n" + x.getCode() + 
				separator + x.getName() + 
				separator + x.getTerminology() + 
				separator + x.getParent() +
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getSynonyms())) + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getDefinitions())) + 
				separator + services.calculateAndProduceSpacedTerms(separator) +
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getMaps())));
				services.clearPropertyListsFromHeaderMap();});
		
		Stream.of(fields).filter(item -> 
			!item.getName().equals("properties") && 
			!item.getName().equals("maps")).forEach(x -> firstLine.append(x.getName() + separator));
		firstLine.replace(firstLine.lastIndexOf(separator), firstLine.length(), "");
		services.getHeadersByPosition(services.getPropHeaderMap()).stream().forEach(type -> firstLine.append(separator + type));
		firstLine.append(separator + "Maps_To");
		oneLine.insert(0, firstLine);
	    System.out.println(oneLine.toString());
		return oneLine.toString();
	}
	
	public String produceChildCSVOutputFromListWithHeading(List<ChildEntity> list) {

		StringBuffer firstLine = new StringBuffer();
		String separator = ",";
		Field[] fields = ChildEntity.class.getDeclaredFields();
		Stream.of(fields).forEach(x -> firstLine.append(x.getName() + separator));
		StringBuffer oneLine =
		firstLine.replace(firstLine.length() - 1, firstLine.length(), "");
		list.stream().forEach(x -> oneLine.append(
				"\r\n" + x.getCode() + 
				separator + x.getName() + 
				separator + x.getLevel() +
				separator + x.getParent() +
				separator + x.isLeaf() + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getChildren()))));
	    System.out.println(oneLine.toString());
		return oneLine.toString();
	}

}
