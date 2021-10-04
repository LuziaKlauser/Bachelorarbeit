package de.bachlorarbeit.controller;

import de.bachlorarbeit.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class MailController {

	@Autowired
	private MailService mailService;

	/**
	 * 
	 * @return String
	 */
	@RequestMapping(value="/survey/{surveyId:.+}/send", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.POST)
	public ResponseEntity<?> send(@PathVariable String surveyId) {
		String subject= "Survey for digital forensic readiness";

		//Call sendEmail() from Class MailService for Sending mail to the sender.
		try {
			mailService.sendMail(subject,surveyId);
		} catch (MailException | SQLException mailException) {
			System.out.println(mailException);
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				//TODO body
				.body("Congratulations! Your mail has been send to the user.");
	}

}
