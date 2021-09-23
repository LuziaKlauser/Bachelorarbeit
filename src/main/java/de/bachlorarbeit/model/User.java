package de.bachlorarbeit.model;

import org.springframework.stereotype.Component;

@Component
public class User {

	private String name;
	private String emailAddress;

	public String getName() {
		return name;
	}

	public void setName(String firstName) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
