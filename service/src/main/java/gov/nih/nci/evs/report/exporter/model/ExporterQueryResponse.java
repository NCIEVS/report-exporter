package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

public class ExporterQueryResponse {
	
	public static final String REPORT_SEARCH_PARAMETERS = "Public Search Parameters";
	private List<RestEntity> entities;
	private String input;
	private String propertiesSelected;
	private int hierarchyLevel;
	
	public ExporterQueryResponse(
			List<RestEntity> entities,
			String input, 
			String propertiesSelected,
			int hierarchyLevel) {
		this.entities = entities;
		this.input = input;
		this.propertiesSelected = propertiesSelected;
		this.hierarchyLevel = hierarchyLevel;
	}
	
	public List<RestEntity> getEntities() {
		return entities;
	}
	
	public void setEntities(List<RestEntity> entities) {
		this.entities = entities;
	}
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public String getPropertiesSelected() {
		return propertiesSelected;
	}
	
	public void setPropertiesSelected(String propertiesSelected) {
		this.propertiesSelected = propertiesSelected;
	}
	
	public int getHierarchyLevel() {
		return hierarchyLevel;
	}
	
	public void setHierarchyLevel(int hierarchyLevel) {
		this.hierarchyLevel = hierarchyLevel;
	}


}
