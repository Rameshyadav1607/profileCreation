package com.tapso.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	private JavaMailSender javaMailSender;

	public EmailService(JavaMailSender javaMailSender) {

		this.javaMailSender = javaMailSender;
	}
	
	 public void sendRegistrationEmail(String to, String subject, String body) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        javaMailSender.send(message);
	    }

	public void sendOtpEmail(String to, String subject, String body) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
		System.out.println("Sending email to " + to + ": " + subject);
	}

	public void sendPasswordUpdateEmail(String to, String subject, String body) {
	    // Implement the method to send an email
	    // You can use JavaMailSender or any other email service
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(to);
	    message.setSubject(subject);
	    message.setText(body);
	    javaMailSender.send(message);
	}

}
