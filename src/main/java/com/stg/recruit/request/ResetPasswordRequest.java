package com.stg.recruit.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	
	private String email;
	private String resetToken;
	private String newPassword;

}
