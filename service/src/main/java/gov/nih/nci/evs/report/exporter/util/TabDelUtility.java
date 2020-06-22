package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;


import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class TabDelUtility {
	

	public String produceTabDelOutputFromListWithHeading(List<RestEntity> list) {

		StringBuffer firstLine = new StringBuffer();
		String separator = "\t";
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
	

	
	public static void main(String ...args) {
		new TabDelUtility().produceTabDelOutputFromListWithHeading(null);
	}

}
