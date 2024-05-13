package com.stg.recruit.entity.dto;

import com.stg.recruit.entity.User;
import com.stg.recruit.entity.enumuration.ERole;
import com.stg.recruit.entity.enumuration.EStatus;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails {
	
	    private Long id;
	    private String firstName;
	    private String lastName;
	    private String email;
		private String designation;
	    private ERole role;

}
