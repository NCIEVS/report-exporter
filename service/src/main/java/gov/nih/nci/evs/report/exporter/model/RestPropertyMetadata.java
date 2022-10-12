package gov.nih.nci.evs.report.exporter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class RestPropertyMetadata implements Comparable<RestPropertyMetadata>{ 
/**
 * This is a property id
 */
private String code;
/**
 * This is a property name
 */
private String name;

private String terminology;

private String version;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "RestPropertyMetadata"
			+ "{code=" + code + ", name=" + name + "}";
}
/**
 * @return the code
 */
public String getCode() {
	return code;
}
/**
 * @param code the code to set
 */
public void setCode(String code) {
	this.code = code;
}
/**
 * @return the name
 */
public String getName() {
	return name;
}
/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}
public String getTerminology() {
	return terminology;
}
public void setTerminology(String terminology) {
	this.terminology = terminology;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}

@Override
public int compareTo(RestPropertyMetadata o) {
	if( this.getName() == null) {this.setName("Property Name Not Found");};
	return this.getName().compareTo(o.getName());
}



}
