package de.bachlorarbeit.service;

import de.bachlorarbeit.model.User;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.List;

@Service
public class MailService {

	private JavaMailSender javaMailSender;

	/**
	 * 
	 * @param javaMailSender
	 */
	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendMail(String subject, String surveyId) throws MailException, SQLException {
		SimpleMailMessage mail = new SimpleMailMessage();
		DatabaseService databaseService = new DatabaseService();
		String message="Please fill out the following survey"+"\n";
		message+="Link: http://localhost:8080/survey/"+surveyId;
		String employee= (String) databaseService.getEmployeeIdForSurvey(surveyId).get(0).get("EMPLOYEE_ID");
		String email=databaseService.getEmail(employee);

		//sends Mail
		mail.setTo(email);
		mail.setSubject(subject);
		mail.setText(message);

		//Sends Mail
		javaMailSender.send(mail);

	}
}
