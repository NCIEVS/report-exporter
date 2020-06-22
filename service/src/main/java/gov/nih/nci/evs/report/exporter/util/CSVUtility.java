package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class CSVUtility {
	

	public String produceCSVOutputFromListWithHeading(List<RestEntity> list) {

		StringBuffer firstLine = new StringBuffer();
		String separator = ",";
		Field[] fields = RestEntity.class.getDeclaredFields();
		Stream.of(fields).forEach(x -> firstLine.append(x.getName() + separator));
		StringBuffer oneLine =
		firstLine.replace(firstLine.length() - 1, firstLine.length(), "");
		list.stream().forEach(x -> oneLine.append(
				"\r\n" + x.getCode() + 
				separator + x.getName() + 
				separator + x.getTerminology() + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getSynonyms())) + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getDefinitions())) + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getProperties()))));
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
				separator + x.isLeaf() + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getChildren()))));
	    System.out.println(oneLine.toString());
		return oneLine.toString();
	}
	

}
