package com.stg.recruit.entity.enumuration;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {

	RECRUITER_ADMIN,RECRUITER,INTERVIEWER;
	
	   @Override
	    public String getAuthority() {
	        return this.name(); 
	    }
	
}
