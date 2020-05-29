package gov.nih.nci.evs.report.exporter.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityProperties {
	
	private List<String> props;

	public List<String> getProps() {
		return props;
	}

	public void setProps(List<String> props) {
		this.props = props;
	}

}
