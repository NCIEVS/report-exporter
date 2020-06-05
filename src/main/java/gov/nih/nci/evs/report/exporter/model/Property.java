package gov.nih.nci.evs.report.exporter.model;

public class Property {
	
	private String type;
	private String value;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return "type: " + type + " value: " + value;
	}

}
