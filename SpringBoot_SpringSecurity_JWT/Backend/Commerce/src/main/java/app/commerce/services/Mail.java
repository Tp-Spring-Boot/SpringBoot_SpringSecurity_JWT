package app.commerce.services;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import app.commerce.entities.User;



@Service
public class Mail {
	

	@Autowired
	private JavaMailSender mailSender;
	
	private final TemplateEngine templateEngine ;
	
	public Mail(TemplateEngine templateEngine) {
		this.templateEngine=templateEngine;
	}
	
	public void sendVerificationEmail(User user)
			throws MessagingException, UnsupportedEncodingException {

		Context context=new Context();
		context.setVariable("title", "Verify Your Email address");
		context.setVariable("link","http://localhost:8080/login?email="+user.getEmail());
		String body=templateEngine.process("Verify", context);
		
		String fromAddress = "myeduconnect20@gmail.com";
		String senderName = "MyEduConnect";
         
		MimeMessage message = mailSender.createMimeMessage();
		boolean Multipart;
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		String toAddress = user.getEmail();
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject("Email address Verification");

		helper.setText(body, true);

		mailSender.send(message);
	}
	
	
	public void ForgotPassword(String Token,User user)
			throws MessagingException, UnsupportedEncodingException {

		Context context=new Context();
		context.setVariable("Token", Token);
		context.setVariable("Name",user.getFirstName());
		String body=templateEngine.process("forgotPassword", context);
		
		String fromAddress = "myeduconnect20@gmail.com";
		String senderName = "MyEduConnect";
         
		MimeMessage message = mailSender.createMimeMessage();
		boolean Multipart;
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		String toAddress = user.getEmail();
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject("Forgot Password");

		helper.setText(body, true);

		mailSender.send(message);
	}
}
