package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestEntity {
	
	private String code;
	
	private String name;
	
	private String terminology;
	
	private List<Synonym> synonyms;
	
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

	public String getTerminology() {
		return terminology;
	}

	public void setTerminology(String terminolgy) {
		this.terminology = terminolgy;
	}

	public List<Synonym> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<Synonym> synonyms) {
		this.synonyms = synonyms;
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
	
	public String toString() {
		return "Code: " + code + " name: " + name;
	}
	
	

}
