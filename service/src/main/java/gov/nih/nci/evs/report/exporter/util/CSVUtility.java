package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class CSVUtility extends FormatUtility {

	

	public String produceCSVOutputFromListWithHeading(List<RestEntity> list, String props) {
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("MapsTo"));
		StringBuffer firstLine = new StringBuffer();
		String separator = ",";
		StringBuffer oneLine = new StringBuffer();
		list.stream().forEach(x -> { x.getProperties()
			.stream()
			.forEach(z -> services.addPropertyTypeAndPositionToCache(z));       
				oneLine.append(
				"\r\n" + x.getTerminology() + 
				separator + x.getCode() + 
				separator + x.getName() +  
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getParents())) +
				services.fullyCuratedProperties(x.getSynonyms(), separator) + 
				services.fullyCuratedProperties(x.getDefinitions(), separator) + 
				services.fullyCuratedProperties(x.getMaps(), separator) + 
				separator + services.calculateAndProduceSpacedTerms(separator));				
				services.clearPropertyListsFromHeaderMap();});
		
		Stream.of(services.filterHeadings(services))
			.forEach(x -> firstLine.append(x + separator));
		firstLine.replace(firstLine.lastIndexOf(separator), firstLine.length(), "");
		services.getHeadersByPosition(services.getPropHeaderMap()).stream().forEach(type -> firstLine.append(separator + type));
		oneLine.insert(0, firstLine);
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
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getParents())) +
				separator + x.isLeaf() + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getChildren()))));
		return oneLine.toString();
	}
	
	

}
