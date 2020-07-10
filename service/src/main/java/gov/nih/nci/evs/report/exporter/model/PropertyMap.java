package gov.nih.nci.evs.report.exporter.model;

public class PropertyMap extends PropertyPrime{
	
    private String targetName;
    private String targetTermGroup;
    private String targetCode;
    private String targetTerminology;
    private String targetTerminologyVersion;
    
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetTermGroup() {
		return targetTermGroup;
	}
	public void setTargetTermGroup(String targetTermGroup) {
		this.targetTermGroup = targetTermGroup;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}
	public String getTargetTerminology() {
		return targetTerminology;
	}
	public void setTargetTerminology(String targetTerminology) {
		this.targetTerminology = targetTerminology;
	}
	public String getTargetTerminologyVersion() {
		return targetTerminologyVersion;
	}
	public void setTargetTerminologyVersion(String targetTerminologyVersion) {
		this.targetTerminologyVersion = targetTerminologyVersion;
	}
	
	public String toString() {

			return  (targetTerminology == null?"":targetTerminology  + " ") +
						 (targetTerminologyVersion == null?"":targetTerminologyVersion + " ") +
					     (targetTermGroup  == null?"":targetTermGroup + " ") +
						 (targetCode == null?"":targetCode + " ") +
					     (targetName  == null?"":targetName + ":") +
						 (getType() == null?"":getType());

	}
    
    

}
