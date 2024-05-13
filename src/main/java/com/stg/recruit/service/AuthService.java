package com.stg.recruit.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.stg.recruit.entity.User;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.request.LoginRequest;

public interface AuthService {

	public abstract User saveUser(User user);

	public abstract void createDefaultAdminUser() throws Exception;

	public abstract boolean doUserExists() throws Exception;

	public boolean doSTGEmployeesExists() throws Exception;

	public abstract ResponseEntity<String> signin(LoginRequest loginRequest);

	public abstract CustomUserDetails customUserPrincipal();

	public abstract String generateOTP(String email);

	public abstract String verifyOTP(String email, String otp) throws RecruitException;

	public abstract String resetPassword(String email, String resetToken, String newPassword);

	public boolean checkUserByEmail(String email) throws RecruitException;

}
