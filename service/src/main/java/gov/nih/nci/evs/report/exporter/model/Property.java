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
		if(super.getType().equals("Maps_To")) {
			return getQualsValue(qualifiers, "Target Terminology") + " "
						+ getQualsValue(qualifiers, "Target_Terminology_Version") + " "
					    + getQualsValue(qualifiers, "Target_Term_Type") + " "
					    + getQualsValue(qualifiers, "Target_Code") + " "
						+ value + ":"
						+ getQualsValue(qualifiers, "Relationship_to_Target");
		}
		if(super.getType().equals("GO_Annotation")) {
			return getQualsValue(qualifiers, "go_id") + " "
					+ value + ":"
						+ getQualsValue(qualifiers, "go_evi");
		}
		
		return (super.getType()==null?NOTYPE:super.getType())
				+ ":" + (value==null?UNDEFINED:value);
	}
	
	private String getQualsValue(List<Qualifier> quals, String type) {
		return quals.stream()
				.filter(x -> x.getType()
						.equals(type))
						.findFirst()
						.get()
						.getValue();
	}
	

}
