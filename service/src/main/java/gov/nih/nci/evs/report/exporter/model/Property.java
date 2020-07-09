package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

public class Property extends PropertyPrime {

	private String value;
	private List<Qualifier> qualifiers;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public List<Qualifier> getQualifiers() {
		return qualifiers;
	}
	public void setQualifiers(List<Qualifier> qualifiers) {
		this.qualifiers = qualifiers;
	}
	public String toString() {
		if(super.getType().equals("GO_Annotation")) {
			return getQualsValue(qualifiers, "go-id") + " "
					+ value + ":"
						+ getQualsValue(qualifiers, "go-evi");
		}
		
		return value==null?UNDEFINED:value;
	}

}
