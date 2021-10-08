package de.bachlorarbeit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

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

	/**
	 * Sends email with a link to the survey with the given surveyId
	 * Sends email to the employee, who is connected to the dampertment, which is also connected to a survey
	 *
	 * @param subject
	 * @param surveyId
	 * @throws MailException
	 * @throws SQLException
	 */
	public void sendMail(String subject, String surveyId) throws MailException, SQLException {
		SimpleMailMessage mail = new SimpleMailMessage();
		DatabaseService databaseService = new DatabaseService();
		String message="Please fill out the following survey"+"\n";
		message+="Link: http://localhost:8080/survey/"+surveyId;
		String employee= (String) databaseService.getEmployeeIdForSurvey(surveyId).get(0).get("EMPLOYEE_ID");
		String email=databaseService.getEmail(employee);

		mail.setTo(email);
		mail.setSubject(subject);
		mail.setText(message);

		//Sends Mail
		javaMailSender.send(mail);

	}
}
