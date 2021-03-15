package gov.nih.nci.evs.report.exporter.util;

public class FormatUtility {
	
	public final static String[] FIELDS = {"terminology", "code", "name", "parents", "synonyms", "definitions", "Maps_To"};
	public final static String[] ROLE_FIELDS = {"concept code", "name", "roles", "target name", "target code"};

	
	public String produceDelimitedQueryRecord(String separator, String codes, int level, String props) {
		String appendedQuery = "\r\n" + "\r\n" + "\r\n" +
				"\r\n" + "Report Search Parameters: " + 
				"\r\n" +  "\"|Input:  " + codes + "|\"" +
				"\r\n" + "Hierarchy level: " + level +
				"\r\n" +  "\"|Properties Selected: " + props + "|\"";
		return appendedQuery;
	}
}
