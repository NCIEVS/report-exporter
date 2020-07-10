package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class TabDelUtility extends FormatUtility{

	public String produceTabDelOutputFromListWithHeading(List<RestEntity> list) {
		CommonServices services = new CommonServices();
		StringBuffer firstLine = new StringBuffer();
		String separator = "\t";
		StringBuffer oneLine = new StringBuffer();
		list.stream().forEach(x -> {x.getProperties()
			.stream()
			.forEach(z -> services.addPropertyTypeAndPositionToCache(z));  
			oneLine.append(
				"\r\n" + x.getTerminology() + 
				separator + x.getCode() + 
				separator + x.getName() + 
				separator + x.getParent() +
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getSynonyms())) + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getDefinitions())) + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getMaps())) +
				separator + services.calculateAndProduceSpacedTerms(separator));

			    services.clearPropertyListsFromHeaderMap();});
			Stream.of(FIELDS).forEach(x -> firstLine.append(x + separator));
			firstLine.replace(firstLine.lastIndexOf(separator), firstLine.length(), "");
			services.getHeadersByPosition(services.getPropHeaderMap()).stream().forEach(type -> firstLine.append(separator + type));
			oneLine.insert(0, firstLine);
			System.out.println(oneLine.toString());
		return oneLine.toString();
	}
	
	public String produceChildTabDelOutputFromListWithHeading(List<ChildEntity> list) {

		StringBuffer firstLine = new StringBuffer();
		String separator = "\t";
		Field[] fields = ChildEntity.class.getDeclaredFields();
		Stream.of(fields).forEach(x -> firstLine.append(x.getName() + separator));
		StringBuffer oneLine =
		firstLine.replace(firstLine.length() - 1, firstLine.length(), "");
		list.stream().forEach(x -> oneLine.append(
				"\r\n" + x.getCode() + 
				separator + x.getName() + 
				separator + x.getLevel() + 
				separator + x.getParent()  +
				separator + x.isLeaf() + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getChildren()))));
	    System.out.println(oneLine.toString());
		return oneLine.toString();
	}
	

	
	public static void main(String ...args) {
		new TabDelUtility().produceTabDelOutputFromListWithHeading(null);
	}

}
