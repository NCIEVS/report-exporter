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

	public static void sendSurveyEmail(String smtpServer, String emailList, Survey survey) {
		Properties prop = new Properties();
		prop.put("mail.smtp.host",smtpServer);

		Session session = Session.getInstance(prop);
		
		Message message = new MimeMessage(session);
		try {
		message.setFrom(new InternetAddress(survey.getEmail()== null?"scott.bauer@nih.gov":survey.getEmail()));
		message.setRecipients(
		  Message.RecipientType.TO, InternetAddress.parse(emailList));
		message.setSubject("Automated Survey Results");

		String msg = "Survey Results<br/>"
				+ "<br/>"
				+ "Rating:"
				+ survey.getRating()
				+ "<br/>"
				+ "Features:<br/>"
				+ survey.getFeatures() + "<br/>"
						+ "<br/>"
				+ "Recommendations:<br/>"
				+ survey.getRecommendatons() +"<br/>"
						+ "<br/>";

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
	
	public static void main(String ...args) {
		Survey survey = new Survey();
		survey.setEmail("scott.bauer@nih.gov");
		survey.setFeatures("this will be where we have features");
		survey.setRecommendatons("this is where we'll have recommendations");
		
		SurveyEmailUtility.sendSurveyEmail("mailfwd.nih.gov", "scott.bauer@nih.gov", survey);
	}

}
