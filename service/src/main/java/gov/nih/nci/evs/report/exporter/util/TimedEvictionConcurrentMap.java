package gov.nih.nci.evs.report.exporter.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.nih.nci.evs.report.exporter.service.TimedDeferredResultWrapper;

@Component
public class TimedEvictionConcurrentMap {
	
	private static final Logger log = LoggerFactory.getLogger(TimedEvictionConcurrentMap.class);
	

	public static ConcurrentHashMap<String, TimedDeferredResultWrapper> dRHash = new ConcurrentHashMap<String, TimedDeferredResultWrapper>();
	
	@Scheduled(fixedRate = 300000, initialDelay = 300000)
	public void pollandCleanMap() {
		log.info("Running Cache Cleanup Thread");
		dRHash.forEach((x,y) -> {if(y.isStale()) {
			log.info("Removing file: " +  x);
			dRHash.remove(x);}});
	}

	public static ConcurrentHashMap<String, TimedDeferredResultWrapper> getdRHash() {
		return dRHash;
	}

}
