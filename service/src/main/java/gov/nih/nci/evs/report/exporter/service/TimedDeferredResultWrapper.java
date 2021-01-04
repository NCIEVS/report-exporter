package gov.nih.nci.evs.report.exporter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;


public class TimedDeferredResultWrapper {
	
	private Logger log = LoggerFactory.getLogger(TimedDeferredResultWrapper.class);
	
	public TimedDeferredResultWrapper(boolean stale, DeferredResult<byte[]> result, long deferredTimeOut) {
		super();
		this.stale = stale;
		this.result = result;
		this.deferredTimeOut = deferredTimeOut + System.currentTimeMillis();
		System.out.print("Time to expire: " + this.deferredTimeOut);
	}
	private boolean stale = false;
	private DeferredResult<byte[]> result;
	private long deferredTimeOut;
	public boolean isStale() {
		log.info("deferredTimeOut: " + this.deferredTimeOut);
		log.info("System current time mill: " + System.currentTimeMillis());
		log.info("Result Exists?: " + result.hasResult());
		if(result.hasResult() && deferredTimeOut < System.currentTimeMillis())
		{return true; }
		else {return false;}
	}
	
	public void setStale(boolean stale) {
		this.stale = stale;
	}
	public DeferredResult<byte[]> getResult() {
		return result;
	}
	public void setResult(DeferredResult<byte[]> result) {
		this.result = result;
	}
	public long getDeferredTimeOut() {
		return deferredTimeOut;
	}
	public void setDeferredTimeOut(int deferredTimeOut) {
		this.deferredTimeOut = deferredTimeOut + System.currentTimeMillis();
	}
	
	

}
