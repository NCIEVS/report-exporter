package gov.nih.nci.evs.report.exporter.model;

public class Role extends Rel {

	
	public String toString() {
		return "Role: " + getType() +  "Code: " + getRelatedCode() + " Name: " + getRelatedName();
	}

}
