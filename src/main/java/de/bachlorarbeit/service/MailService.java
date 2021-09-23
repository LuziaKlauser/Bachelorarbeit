package de.bachlorarbeit.service;

import de.bachlorarbeit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
	 *
	 * Sends an e-mail to 'user' with the given subject and message
	 *
	 * @param user
	 * @param subject
	 * @param message
	 * @throws MailException
	 */
	public void sendEmail(User user, String subject, String message) throws MailException {

		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setTo(user.getEmailAddress());
		mail.setSubject(subject);
		mail.setText(message);

		//Sends Mail
		javaMailSender.send(mail);
	}
}
