package com.example.smartcontactmanagerboot.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;



@Service
public class EmailService {
	
	public boolean sendEmail(String to, String from, String subject, 
			String text) {
		boolean flag = false;
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable",true);
		properties.put("mail.smtp.port","587");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		
		String username = "milind.jha.61";
		String password = "";
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);//username and password
			}
		});
		
		try {
			Message message = new MimeMessage(session);
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			message.setText(text);
			
			Transport.send(message);
			flag = true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}
