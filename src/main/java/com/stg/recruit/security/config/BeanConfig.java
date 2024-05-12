package com.stg.recruit.security.config;

import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class BeanConfig {
	
	  @Bean
	    public BCryptPasswordEncoder bCryptPasswordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	  
		@Bean
		public ModelMapper modelMapper() {
			return new ModelMapper();
		}

	//Mail config
		
		 @Value("${spring.mail.host}")
		    private String host;

		    @Value("${spring.mail.port}")
		    private int port;

		    @Value("${spring.mail.username}")
		    private String username;
		    
		    @Value("${spring.mail.password}")
		    private String password;
		    
		    @Bean
		    public JavaMailSender javaMailSender() {
		        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		        mailSender.setHost(host);
		        mailSender.setPort(port);
		        mailSender.setUsername(username);
		        mailSender.setPassword(password);

		        Properties props = mailSender.getJavaMailProperties();
		        props.put("mail.transport.protocol", "smtp");
		        props.put("mail.smtp.auth", "true");
		        props.put("mail.smtp.starttls.enable", "true");
		        props.put("mail.debug", "true"); // Enable debug mode if needed

		        return mailSender;
		    }
		    
		    
		    
	 // thymeleaf config	
		    
		    @Bean
		    public ITemplateResolver thymeleafTemplateResolver() {
		        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		        templateResolver.setPrefix("templates/");
		        templateResolver.setSuffix(".html");
		        templateResolver.setTemplateMode("HTML");
		        templateResolver.setCharacterEncoding("UTF-8");
		        return templateResolver;
		    }
		    
		    @Bean
		    public SpringTemplateEngine thymeleafTemplateEngine() {
		        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
		        return templateEngine;
		    }
		
		
}
