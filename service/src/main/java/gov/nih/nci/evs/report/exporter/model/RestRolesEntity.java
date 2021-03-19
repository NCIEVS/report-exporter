package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestRolesEntity {
	
	private String code;
	
	private String name;
	
	private List<Role> roles;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name.replaceAll(",", " ");
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}


	public String toString() {
		return "Code: " + code + " name: " + name;
	}
	
}
