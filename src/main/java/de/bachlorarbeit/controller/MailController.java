package de.bachlorarbeit.controller;

import de.bachlorarbeit.model.User;
import de.bachlorarbeit.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

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
	@RequestMapping("send-mail")
	public String send() {

		String subject= "Survey for digital forensic readiness";
		String message="Send survey";
		//Receiver's name and email address
		user.setName("Luzia");
		user.setEmailAddress("luzia.klauser@t-online.de");


		//Call sendEmail() from Class MailService for Sending mail to the sender.
		try {
			notificationService.sendEmail(user,subject,message);
		} catch (MailException mailException) {
			System.out.println(mailException);
		}
		return "Congratulations! Your mail has been send to the user.";
	}
	@GetMapping("/surve")
	public String getSurvey() {
		return "survey";
	}
}
