package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class TabDelUtility extends FormatUtility{

	public String produceTabDelOutputFromListWithHeading(List<RestEntity> list, String props) {
		CommonServices services = new CommonServices();
		services.setNoSynonyms(!props.contains("FULL_SYN"));
		services.setNoDefinitions(!(props.contains("DEFINITION") || props.contains("ALT_DEFINITION")));
		services.setNoMaps(!props.contains("Maps_To"));
		TripleBoolean flags = new TripleBoolean();
		StringBuffer firstLine = new StringBuffer();
		String separator = "\t";
		StringBuffer oneLine = new StringBuffer();
		list.stream().forEach(x -> {
//		Caching the property type for any property in any enitity;
			x.getProperties()
			.stream()
			.forEach(z -> services.addPropertyTypeAndPositionToCache(z)); 

			oneLine.append(
				"\r\n" + x.getTerminology() + 
				separator + x.getCode() + 
				separator + x.getName() + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getParents())) +
				services.fullyCuratedProperties(x.getSynonyms(), separator, "synonyms", flags) + 
				services.fullyCuratedProperties(x.getDefinitions(), separator, "definitions", flags) + 
				services.fullyCuratedProperties(x.getMaps(), separator, "Maps_To", flags) + 
				separator + services.calculateAndProduceSpacedTerms(separator));
			    services.clearPropertyListsFromHeaderMap();});
		
		services.filterHeadings(services).stream()
			.forEach(x -> firstLine.append(x + separator));
		String firstHeaderString = CommonServices.cleanListOutPut(firstLine.toString());
		firstLine.replace(firstHeaderString.lastIndexOf(separator), firstHeaderString.length(), "");
		String secondHeader = services.getHeadersByPosition(
				services.getPropHeaderMap())
						.stream()
						.collect(Collectors.joining(separator));
		oneLine.insert(0, firstHeaderString + secondHeader);
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
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getParents()))  +
				separator + x.isLeaf() + 
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getChildren()))));
		return oneLine.toString();
	}
	

	
	public static void main(String ...args) {
		new TabDelUtility().produceTabDelOutputFromListWithHeading(null, null);
	}

}
