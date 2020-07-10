package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

public class PropertyPrime {
	
	public static final String NOSOURCE = "NOSOURCE";
	public static final String NOTYPE = "NOTYPE";
	public static final String UNDEFINED = "UNDEFINED";
	
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	 String getQualsValue(List<Qualifier> quals, String type) {
		return quals.stream()
				.filter(x -> x.getType()
						.equals(type))
						.findFirst()
						.get()
						.getValue();
	}
	
}
