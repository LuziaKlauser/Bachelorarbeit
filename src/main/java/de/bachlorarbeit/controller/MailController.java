package de.bachlorarbeit.controller;

import de.bachlorarbeit.model.User;
import de.bachlorarbeit.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
public class MailController {

	@Autowired
	private MailService notificationService;

	@Autowired
	private User user;

	/**
	 * 
	 * @return String
	 */
	@RequestMapping(value="/send/{surveyId:.+}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.POST)
	public ResponseEntity<?> send(@PathVariable String surveyId, @RequestParam HashMap<String, Object> formData) {
		String subject= "Survey for digital forensic readiness";
		String message="Send survey";
		//Receiver's name and email address
		user.setName("Luzia");
		user.setEmailAddress("luzia.klauser@t-online.de");


		//Call sendEmail() from Class MailService for Sending mail to the sender.
		try {
			notificationService.sendEmailToUser(user,subject,message);
		} catch (MailException mailException) {
			System.out.println(mailException);
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				//TODO body
				.body("Congratulations! Your mail has been send to the user.");
	}

	@RequestMapping(value = "/survey/uploa",
			produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc-v1.0+json"},
			method = RequestMethod.POST)
	public ResponseEntity<?> uploadFiles(@RequestParam HashMap<String, Object> formData) throws SQLException {
		//taskService.postSurvey(formData);
		return ResponseEntity.status(HttpStatus.CREATED)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				//TODO body
				.body("Survey successfully uploaded");
	}

}
