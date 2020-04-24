package gov.nih.nci.evs.report.exporter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPropertyMetadata {
/**
 * This is a property id
 */
private String code;
/**
 * This is a property name
 */
private String name;

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

}
