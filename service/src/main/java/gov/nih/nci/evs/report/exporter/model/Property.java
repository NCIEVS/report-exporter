package gov.nih.nci.evs.report.exporter.model;

public class Property extends PropertyPrime {

	private String value;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return "type: " + super.getType() + " value: " + value;
	}

}