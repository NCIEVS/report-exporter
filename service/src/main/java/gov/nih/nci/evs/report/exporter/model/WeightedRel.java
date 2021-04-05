package gov.nih.nci.evs.report.exporter.model;

public class WeightedRel<T extends Rel> implements Comparable<WeightedRel<Rel>> {
	Rel rel;
	int weight;
	
	public WeightedRel(Rel x, int i) {
		this.rel = x;
	    this.weight = i;
	}

	public Rel getRel() {
		return rel;
	}

	public void setRel(Rel rel) {
		this.rel = rel;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(WeightedRel<Rel> o) {
		return o.weight - this.weight;
	}
	
	
}
