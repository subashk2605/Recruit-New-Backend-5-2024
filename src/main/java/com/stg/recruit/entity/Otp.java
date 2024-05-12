package com.stg.recruit.entity;

import java.time.LocalDateTime;

import com.stg.recruit.entity.enumuration.ERole;
import com.stg.recruit.entity.enumuration.EStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Otp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String otp;
	private LocalDateTime otpExpiry;
	private String resetToken;
	private LocalDateTime resetTokenExpiry;
	
	  @OneToOne
	    @JoinColumn(name = "user_id")
	    private User user;

	
	

}
