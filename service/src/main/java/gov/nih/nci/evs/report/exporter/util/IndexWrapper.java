package gov.nih.nci.evs.report.exporter.util;
public class IndexWrapper{
	IndexWrapper(int index){
		this.setIndex(index);
	}
	
	private int index;
	
	public void increment() {
		setIndex(getIndex() + 1);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}