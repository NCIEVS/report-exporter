package gov.nih.nci.evs.report.exporter.model;

public class WeightedRole implements Comparable<WeightedRole> {
	Role role;
	int weight;
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(WeightedRole o) {
		return this.weight - o.weight;
	}
	
	
}
