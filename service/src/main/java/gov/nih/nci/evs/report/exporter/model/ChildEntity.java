package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

public class ChildEntity {
	
	private String code;
	private String name;
	private String level;
	private boolean leaf;
	private List<ChildEntity> children;
	
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public List<ChildEntity> getChildren() {
		return children;
	}
	public void setChildren(List<ChildEntity> children) {
		this.children = children;
	}
	
	

}
