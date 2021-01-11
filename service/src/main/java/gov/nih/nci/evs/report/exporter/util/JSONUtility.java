package gov.nih.nci.evs.report.exporter.util;

import java.util.List;

import gov.nih.nci.evs.report.exporter.model.RestEntity;

public class JSONUtility extends FormatUtility {
	
	public String produceJsonOutputFromListWithHeading(List<RestEntity> list){
		return CommonServices.getGsonForPrettyPrint().toJson(list);	
	}

}
