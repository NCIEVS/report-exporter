package gov.nih.nci.evs.report.exporter.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import gov.nih.nci.evs.report.exporter.model.Survey;

public class SurveyEmailUtility {

	public static void sendSurveyEmail(String smtpServer, String emailList, String defaultFromEmail, Survey survey) {
		Properties prop = new Properties();
		prop.put("mail.smtp.host",smtpServer);

		Session session = Session.getInstance(prop);
		
		Message message = new MimeMessage(session);
		try {
		message.setFrom(new InternetAddress(defaultFromEmail));
		message.setRecipients(
		  Message.RecipientType.TO, InternetAddress.parse(emailList));
		message.setSubject("Automated Survey Results");

		String msg = getSurveyMessage(survey);
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");
		

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		Transport.send(message);
		} catch(MessagingException e) {
			throw new RuntimeException("Email Message Failed For Survey", e);
		}
	}
	
	public static String getSurveyMessage(Survey survey) {
		return "<b>Survey Results</b>"
				+ "<br/>"
				+ "<br/>"
				+ "Rating: "
				+ survey.getRating()
				+ "<br/>"
				+ "<br/>"
				+ "Features:<br/>"
				+ survey.getFeatures() 
				+ "<br/>"
				+ "<br/>"
				+ "Recommendations:<br/>"
				+ survey.getRecommendations() 
				+"<br/>"
				+ "<br/>"
				+ "Email: "
				+ survey.getEmail() +"<br/>"
				+ "<br/>";

	}
	
	public static void main(String ...args) {
		Survey survey = new Survey();
		survey.setRating(9);
		survey.setEmail("scott.bauer@nih.gov");
		survey.setFeatures("this will be where we have features");
		survey.setRecommendations("this is where we'll have recommendations");
		System.out.println(getSurveyMessage(survey));
		
		SurveyEmailUtility.sendSurveyEmail("mailfwd.nih.gov", "scott.bauer@nih.gov", "scott.bauer@nih.gov", survey);
	}

}
