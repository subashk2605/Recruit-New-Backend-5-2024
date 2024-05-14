package com.stg.recruit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stg.recruit.entity.User;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.request.LoginRequest;
import com.stg.recruit.request.ResetPasswordRequest;
import com.stg.recruit.request.VerifyOtpRequest;
import com.stg.recruit.service.AuthService;
import com.stg.recruit.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

//	@Autowired
//	private VaultTemplate vaultTemplate;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@PostMapping("/adduser")
	public ResponseEntity<String> signup(@RequestBody CustomUserDetails user) throws RecruitException {
		String savedUser = authService.saveUser(user);
		return ResponseEntity.ok(savedUser);
	}

	@PostMapping("/signin")
	public ResponseEntity<String> signin(@RequestBody LoginRequest loginRequest) {
		return authService.signin(loginRequest);
	}

	@GetMapping("/all")
//    @PreAuthorize("hasAuthority('RECRUITER_ADMIN')")
	public ResponseEntity<List<CustomUserDetails>> getAllUsers() {
		List<CustomUserDetails> users = userService.findAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("my")
	public ResponseEntity<CustomUserDetails> getUserById() {
		return ResponseEntity.ok(userService.getUserById());
	}
//
//	@GetMapping("vault")
//	public String vault() {
//		VaultResponse response = vaultTemplate.read("/application/data/database");
//		Map<String, Object> map = response.getRequiredData();
//		if (map == null) {
//			System.out.println("Response data is missing.");
//		}
//
//		Object dataObj = map.get("data");
//		if (!(dataObj instanceof Map<?, ?>)) {
//			System.out.println("'data' is not formatted as expected.");
//		}
//
//		@SuppressWarnings("unchecked")
//		Map<String, Object> dataMap = (Map<String, Object>) dataObj;
//		Object usernameObj = dataMap.get("TOKEN_PREFIX");
//		if (!(usernameObj instanceof String)) {
//			System.out.println("Username is either missing or not a string");
//		}
//
//		String username = (String) usernameObj;
//		System.err.println("Username: " + username);
//		return username;
//	}

	@PostMapping("/generate-otp")
	public String generateOTP(@RequestParam String userName) {
		return authService.generateOTP(userName);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOTP(@RequestBody VerifyOtpRequest verifyOtpRequest) throws RecruitException {
		String resetToken = authService.verifyOTP(verifyOtpRequest.getEmail(), verifyOtpRequest.getOtp());
		if (resetToken != null) {
			return ResponseEntity.ok(resetToken);
		} else {
			return ResponseEntity.badRequest().body("Invalid OTP");
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
		String response = authService.resetPassword(request.getEmail(), request.getResetToken(),
				request.getNewPassword());
		if (response.equals("Password reset successful")) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping(value = "check")
	public ResponseEntity<Boolean> checkUserByEmail(@RequestParam String userName) throws RecruitException {

		return ResponseEntity.ok(authService.checkUserByEmail(userName));
	}

}
