package de.bachlorarbeit.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
		String message = "Please fill out the following survey" + "\n";
		message += "Link: http://localhost:8080/survey/" + surveyId;
		List<JSONObject> employee = databaseService.getEmployeeIdForSurvey(surveyId);
		String employeeId = (String) employee.get(0).get("EMPLOYEE_ID");
		String email = databaseService.getEmail(employeeId);
		mail.setTo(email);
		mail.setSubject(subject);
		mail.setText(message);
		//Sends Mail
		javaMailSender.send(mail);

	}
}

