package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestEntity {
	
	private String code;
	
	private String name;
	
	private String terminolgy;
	
	private List<Synonym> synomyms;
	
	private List<Definition> definitions;
	
	private List<Property> properties;

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

	public String getTerminolgy() {
		return terminolgy;
	}

	public void setTerminolgy(String terminolgy) {
		this.terminolgy = terminolgy;
	}

	public List<Synonym> getSynomyms() {
		return synomyms;
	}

	public void setSynomyms(List<Synonym> synomyms) {
		this.synomyms = synomyms;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<Definition> definitions) {
		this.definitions = definitions;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	

}
