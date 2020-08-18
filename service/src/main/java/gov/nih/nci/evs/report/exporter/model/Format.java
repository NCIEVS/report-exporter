package gov.nih.nci.evs.report.exporter.model;

public class Format {

	private String name;
	private String description;
	private String extension;
	
	
	public Format(String name, String description, String extension) {
		super();
		this.name = name;
		this.description = description;
		this.extension = extension;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String toString() {
		return name + ":" + description;
	}

}
