package gov.nih.nci.evs.report.exporter.model;

import gov.nih.nci.evs.report.exporter.util.CommonServices.Formats;

public class DeferredStatus {
	public static enum Status{TRUE, FALSE, EXPIRED}
	
	public Status status;
	public Formats format;

	public DeferredStatus(Status status, Formats format) {
		this.status = status;
		this.format = format;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Formats getFormat() {
		return format;
	}

	public void setFormat(Formats format) {
		this.format = format;
	}

}
