package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class CSVUtility extends FormatUtility {

	

	public String produceCSVOutputFromListWithHeading(List<RestEntity> list, String props) {

		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
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
				services.fullyCuratedProperties(x.getSynonyms(), separator, "synonyms", flags) + 
				services.fullyCuratedProperties(x.getDefinitions(), separator, "definitions", flags) + 
				services.fullyCuratedProperties(x.getMaps(), separator, "Maps_To",flags) + 
				separator + services.calculateAndProduceSpacedTerms(separator));				
				services.clearPropertyListsFromHeaderMap();});
		// If we have any columns flagged for removal clean up the rows here. 
		StringBuffer fullColSet = new StringBuffer(
				services.cleanColumns(flags, oneLine, separator));
		services.filterHeadings(services, flags).stream()
			.forEach(x -> firstLine.append(x + separator));
		String firstHeaderString = CommonServices.cleanListOutPut(firstLine.toString());
		firstLine.replace(firstHeaderString.lastIndexOf(separator), firstHeaderString.length(), "");
//		String secondHeader = services.getHeadersByPosition(
//				services.getPropHeaderMap())
//						.stream()
//						.collect(Collectors.joining(separator));
		services.getHeadersByPosition(services.getPropHeaderMap())
									.stream()
									.forEach(type -> 
									firstLine.append(separator + type));
		fullColSet.insert(0, firstLine);
		return fullColSet.toString();
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
