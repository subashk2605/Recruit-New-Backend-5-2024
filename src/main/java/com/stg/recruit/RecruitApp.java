package com.stg.recruit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.stg.recruit.service.AuthService;
//import com.stg.recruit.ldap.LdapForStgEmployees;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class RecruitApp {
	
	private final AuthService authService;
	
//	private final LdapForStgEmployees ldapForStgEmployees;

	public static void main(String[] args) {
		SpringApplication.run(RecruitApp.class, args);
	}

	@Bean
	CommandLineRunner execAppStartupScript() {
		return args -> {
			if (authService.doUserExists()) {
				authService.createDefaultAdminUser();
			}
			
//			if(authService.doSTGEmployeesExists()) {
//				ldapForStgEmployees.getUserDetails();
//			}
		};
	}

}
