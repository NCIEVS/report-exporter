package gov.nih.nci.evs.report.exporter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.evs.report.exporter.model.Survey;
import gov.nih.nci.evs.report.exporter.util.SurveyEmailUtility;

@Service
public class SurveyEmailService{
	
	@Autowired
	EVSAPIBaseService service;
	
	public void sendSurveyEmail(Survey survey) {
		SurveyEmailUtility.sendSurveyEmail(service.getSmtpServer(), service.getEmailList(), survey);
			
	}

}
