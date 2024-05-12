package com.stg.recruit.security.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.stg.recruit.constants.SecurityConstants;
import com.stg.recruit.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtils {

	public static String generateToken(User user, Collection<? extends GrantedAuthority> authorities) {
		Map<String, Object> claims = new HashMap<>();

		claims.put("email", user.getEmail());
		claims.put("id", user.getUserId());
		claims.put(SecurityConstants.AUTHORITIES_KEY,
				authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
//        System.err.println(claims);

		return Jwts.builder().setSubject(user.getEmail())
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.setClaims(claims).signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
	}

	public static boolean validateToken(String token, UserDetails userDetails) {
		if (token == null || userDetails == null) {
			return false;
		}

		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);

			String usernameFromToken = (String) claims.getBody().get("email");
																							

			// Check username match
			if (usernameFromToken != null && usernameFromToken.equals(userDetails.getUsername())) {

				return true;

			}
			return false;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public static UsernamePasswordAuthenticationToken getAuthenticationToken(final String token,
			final Authentication existingAuth, final UserDetails userDetails) {

		final JwtParser jwtParser = Jwts.parser().setSigningKey(SecurityConstants.SECRET);

		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

		final Claims claims = claimsJws.getBody();

		final Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(SecurityConstants.AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

	private static Collection<? extends GrantedAuthority> extractAuthorities(Jws<Claims> claims) {
		List<String> authorityStrings = claims.getBody().get(SecurityConstants.AUTHORITIES_KEY, List.class);
		return authorityStrings.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	public static String extractUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
		return (String) claims.get("email");
	}

	public static Claims extractUser(String token) {
		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
		return claims;
	}

}
