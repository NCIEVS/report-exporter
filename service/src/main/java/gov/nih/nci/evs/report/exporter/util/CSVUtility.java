package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;

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
				separator + x.getDefinitions() + 
				separator + x.getProperties() + 
				separator + x.getSynomyms()));
		
	    System.out.println(oneLine.toString());
		return oneLine.toString();
	}
	
	public static void main(String ...args) {
		CodeReadService service = new CodeReadService();
		List<String> codes = new ArrayList<String>();
		codes.add("C12434");
		codes.add("C1909");
		codes.add("C61410");
		List<String> props = new ArrayList<String>();
		props.add("Preferred_Name");
		List<RestEntity> RElist = service.getEntitiesForPropertyNameFilter(service.getRestProperties(codes), props);
		new CSVUtility().produceCSVOutputFromListWithHeading(RElist);
	}

}
