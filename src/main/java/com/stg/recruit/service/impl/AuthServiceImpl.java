package com.stg.recruit.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.stg.recruit.entity.Otp;
import com.stg.recruit.entity.User;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.entity.enumuration.ERole;
import com.stg.recruit.entity.enumuration.EStatus;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.repository.OtpRepository;
import com.stg.recruit.repository.UserRepository;
import com.stg.recruit.request.LoginRequest;
import com.stg.recruit.security.MyUserDetails;
import com.stg.recruit.security.config.JwtUtils;
import com.stg.recruit.service.AuthService;
import com.stg.recruit.service.EmailService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OtpRepository otpRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private EmailService emailService;
	@Value("${app.default.admin.firstname}")
	private String defaultAdminFirstname;

	@Value("${app.default.admin.lastname}")
	private String defaultAdminLastname;

	@Value("${app.default.admin.email}")
	private String defaultAdminEmail;

	@Value("${app.default.admin.password}")
	private String defaultAdminPassword;

	@Override
	public User saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password before saving
		return userRepository.save(user);
	}

	@Override
	public void createDefaultAdminUser() throws Exception {
		User user = new User();
		user.setFirstName(defaultAdminFirstname);
		user.setLastName(defaultAdminLastname);
		user.setEmail(defaultAdminEmail);
		user.setPassword(passwordEncoder.encode(defaultAdminPassword));
		user.setRole(ERole.RECRUITER_ADMIN);
		userRepository.save(user);
	}

	@Override
	public boolean doUserExists() throws Exception {
		return userRepository.findAll().size() == 0;
	}

	@Override
	public ResponseEntity<String> signin(LoginRequest loginRequest) {
		User user = userRepository.findByEmail(loginRequest.getUsername());
		if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			String token = JwtUtils.generateToken(user, user.getAuthorities());
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}

	@Override
	public CustomUserDetails customUserPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		CustomUserDetails customUserDetails = modelMapper.map(user, CustomUserDetails.class);
		return customUserDetails;
	}

	@Override
	public String generateOTP(String email) {
	    // Generate a random 6-digit OTP
	    String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

	    User user = userRepository.findByEmail(email);
	    if (user != null) {
	        Otp otpTemp = otpRepository.findByUser(user);
	        if (otpTemp == null) {
	            otpTemp = new Otp();
	            otpTemp.setUser(user);
	        }
	        otpTemp.setOtp(otp);
	        otpTemp.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // Set expiry in 5 minutes
	        otpRepository.save(otpTemp);
	        emailService.sendOtpMail(email, user.getFirstName()+" "+ user.getLastName(), "5", otp);
	    }
	    
	    return otp; // For testing purposes (remove in production)
	}


	@Override
	public String verifyOTP(String email, String otp) throws RecruitException{
		String response;
	    User user = userRepository.findByEmail(email);
	    if (user != null && user.getOtpDetails() != null &&
	            user.getOtpDetails().getOtpExpiry().isAfter(LocalDateTime.now()) &&
	            user.getOtpDetails().getOtp().equals(otp)) {
	        SecureRandom random = new SecureRandom();
	        byte[] bytes = new byte[32]; 
	        random.nextBytes(bytes);
	        String encodedResetToken = Base64.getUrlEncoder().encodeToString(bytes); 
	        user.getOtpDetails().setResetToken(encodedResetToken); 	        user.getOtpDetails().setResetToken(encodedResetToken); // Set encoded reset token
	        user.getOtpDetails().setResetTokenExpiry(LocalDateTime.now().plusMinutes(15)); 
	        user.getOtpDetails().setOtp(null); 
	        userRepository.save(user);
	       response = encodedResetToken;
	    } else {
	       throw new RecruitException("Invalid Otp");
	    }
	    return response;
	}
	
	

	@Override
	public String resetPassword(String email, String resetToken, String newPassword) {
	    User user = userRepository.findByEmail(email);
	    if (user != null && user.getOtpDetails() != null &&
	            resetToken != null && resetToken.equals(user.getOtpDetails().getResetToken()) &&
	            user.getOtpDetails().getResetTokenExpiry().isAfter(LocalDateTime.now())) {
	        user.setPassword(passwordEncoder.encode(newPassword));
//	        user.getOtpDetails().setResetToken(null); // Clear reset token after successful reset
	        userRepository.save(user);
	        otpRepository.delete(user.getOtpDetails());
	        return "Password reset successful";
	    } else {
	        return "Invalid email or reset token";
	    }
	}

	
	
	@Override
	public boolean checkUserByEmail(String email) throws RecruitException {
		
		return userRepository.existsByEmail(email);
	}
	

	


}
