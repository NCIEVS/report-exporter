package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;


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
				separator + cleanListOutPut(getListValues(x.getSynonyms())) + 
				separator + cleanListOutPut(getListValues(x.getDefinitions())) + 
				separator + cleanListOutPut(getListValues(x.getProperties()))));
	    System.out.println(oneLine.toString());
		return oneLine.toString();
	}
	
	public <T> String getListValues(List<T> list) {
		return list != null?"\"" + list.toString() + "\"": null;
	}
	
	public String cleanListOutPut(String list){
		if (list == null)  return null;
		return list.replace("[", "").replace("]", "");
	}
	
	public static void main(String ...args) {
		new CSVUtility().produceCSVOutputFromListWithHeading(null);
	}

}
