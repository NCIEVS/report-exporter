package gov.nih.nci.evs.report.exporter.util;

import java.util.List;

import gov.nih.nci.evs.report.exporter.model.ExporterQueryResponse;
import gov.nih.nci.evs.report.exporter.model.RestEntity;

public class JSONUtility extends FormatUtility {
	
	public String produceJsonOutputFromListWithHeading(
			List<RestEntity> entities, String props, String codes, int level) { 
		ExporterQueryResponse response = new ExporterQueryResponse(entities, codes, props, level);
		return CommonServices.getGsonForPrettyPrint().toJson(response);	
	}

}
