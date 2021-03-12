package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.Survey;

class SurveyEmailUtilityTest {

	@Test
	void test() {
		Survey survey = new Survey();
		survey.setRating(9);
		survey.setEmail("scott.bauer@nih.gov");
		survey.setFeatures("this will be where we have features");
		survey.setRecommendatons("this is where we'll have recommendations");
		String result = SurveyEmailUtility.getSurveyMessage(survey);
		assertTrue(result.contains("9"));
		assertTrue(result.contains("this will be where we have features"));
		assertTrue(result.contains("this is where we'll have recommendations"));	
	}

}
