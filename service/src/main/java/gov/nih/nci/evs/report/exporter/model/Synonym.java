package gov.nih.nci.evs.report.exporter.model;

public class Synonym extends PropertyPrime {

	
    private String name;
    private String termGroup;
    private String source;
    private String code;
    private String subSource;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTermGroup() {
		return termGroup;
	}
	public void setTermGroup(String termGroup) {
		this.termGroup = termGroup;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSubSource() {
		return subSource;
	}
	public void setSubSource(String subSource) {
		this.subSource = subSource;
	}
    
    public String toString() {
    	return  (source == null?"UNDEFINED":source) + " " 
    			+ (super.getType()==null?"No Type defined":super.getType()) 
    					+ ":" + (name == null?"No Name Defined":name);
    }
}
