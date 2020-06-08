package gov.nih.nci.evs.report.exporter.util;

import java.util.List;

public class CommonServices {
	
	public static <T> String getListValues(List<T> list) {
		return list != null?"\"" + list.toString() + "\"": null;
	}
	
	public static String cleanListOutPut(String list){
		if (list == null)  return null;
		return list.replace("[", "").replace("]", "");
	}

}
