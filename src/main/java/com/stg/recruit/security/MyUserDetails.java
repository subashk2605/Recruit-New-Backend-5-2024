package com.stg.recruit.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.stg.recruit.entity.User;

import lombok.Getter;
import lombok.ToString;

import java.util.Collection;


@Getter
@ToString
public class MyUserDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return user.getAuthorities();    
    	}


    @Override
    public boolean isAccountNonExpired() {
        return true; // Return true if the account is not expired, false otherwise
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountLockStatus(); // Return true if the account is not locked, false otherwise
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Return true if the credentials are not expired, false otherwise
    }

    @Override
    public boolean isEnabled() {
        return true; // Return true if the account is enabled, false otherwise
    }
}
