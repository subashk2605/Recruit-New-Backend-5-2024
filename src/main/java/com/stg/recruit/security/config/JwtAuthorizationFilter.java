package com.stg.recruit.security.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stg.recruit.constants.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		String token = null;

		if (header != null && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			token = header.replace(SecurityConstants.TOKEN_PREFIX, "");
		}

		if (token != null) {
			try {
				String username = JwtUtils.extractUsername(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (JwtUtils.validateToken(token, userDetails)) {
					Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, authorities);
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					response.setStatus(HttpStatus.FORBIDDEN.value());
					throw new RuntimeException("Invalid JWT token");
				}
			} catch (Exception e) {
				logger.error("Error while validating JWT token:", e); // Log the error with details
				response.setStatus(HttpStatus.UNAUTHORIZED.value()); // Set appropriate status code
			}
		} else {
			logger.warn("Missing JWT token in Authorization header");
		}

		chain.doFilter(request, response);

	}

}
