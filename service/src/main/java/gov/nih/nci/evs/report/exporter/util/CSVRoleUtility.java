package gov.nih.nci.evs.report.exporter.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;


public class CSVRoleUtility extends FormatUtility {

	

	public String produceCSVOutputFromListWithHeading(List<RestEntity> list, String props, String searchCodes, int level) {

		CommonServices services = new CommonServices();
		StringBuffer firstLine = new StringBuffer();
		String separator = ",";
		StringBuffer oneLine = new StringBuffer();
		list.stream().forEach(x ->  
				oneLine.append(
				"\r\n" + x.getTerminology() + 
				separator + x.getCode() + 
				separator + x.getName() +  
				separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x.getParents())) +
				services.fullyCuratedProperties(x.getSynonyms(), separator, CommonServices.SYNONYMS) + 
				services.fullyCuratedProperties(x.getDefinitions(), separator, CommonServices.DEFINITIONS) + 
				services.fullyCuratedProperties(x.getMaps(), separator, CommonServices.MAPS) + 
				separator + services.calculateAndProduceSpacedTerms(separator)));				
		services.filterHeadings(services).stream()
			.forEach(x -> firstLine.append(x + separator));
		String firstHeaderString = CommonServices.cleanListOutPut(firstLine.toString());
		firstLine.replace(firstHeaderString.lastIndexOf(separator), firstHeaderString.length(), "");
		String secondHeader = services.getHeadersByPosition(
				services.getPropHeaderMap())
						.stream()
						.collect(Collectors.joining(separator));
		oneLine.insert(0, firstHeaderString + secondHeader);
		oneLine.append(produceDelimitedQueryRecord(separator,searchCodes,level,props));
		return oneLine.toString();
	}

}
