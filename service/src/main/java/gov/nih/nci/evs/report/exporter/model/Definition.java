package gov.nih.nci.evs.report.exporter.model;

public class Definition extends PropertyPrime {
	
	private String definition;
	private String source;
	
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
		
	public String toString() {
		return (source == null?"":source + ":") + 
						(definition == null?UNDEFINED:definition);
	}

}
