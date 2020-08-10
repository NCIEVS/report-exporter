package gov.nih.nci.evs.report.exporter.model;

public class Root {
	
	private String code;
	private String name;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return (code == null?"NOCODE":code) + ":" + 
						(name == null?"UNDEFINED":name);
	}
	
	

}
