package com.stg.recruit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.stg.recruit.service.AuthService;
import com.stg.recruit.service.EmailService;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


@Service
public class EmailServiceImpl implements EmailService {

	  private final JavaMailSender javaMailSender;
	  private final SpringTemplateEngine templateEngine;

	  @Autowired
	  public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
	    this.javaMailSender = javaMailSender;
	    this.templateEngine = templateEngine;
	  }
	  @Value("${spring.mail.username}")
	    private String username;
	
	@Override
	public void sendOtpMail(String to, String name, String duration, String otp) {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			message.setFrom(new InternetAddress(username));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("OTP From Recruit");

			// Use Thymeleaf to generate email body
			Context context = new Context();
			context.setVariable("name", name);
			context.setVariable("otp", otp);
			context.setVariable("duration", duration);
			String content = templateEngine.process("otp-mail-template.html", context);

			message.setContent(content, "text/html; charset=UTF-8");

			javaMailSender.send(message);
		} catch (MessagingException e) {
			// Handle email sending exceptions
			e.printStackTrace();
		}
	}

}
