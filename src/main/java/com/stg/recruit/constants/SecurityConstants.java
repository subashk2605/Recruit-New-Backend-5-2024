package com.stg.recruit.constants;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;


@Configuration
public class SecurityConstants {
	
//	public static String TOKEN_PREFIX;
//	public static  String SECRET;
//	public static String HEADER_STRING;
//	public static String AUTHORITIES_KEY;
////	public static long EXPIRATION_TIME;
//
//	@Autowired
//	private VaultTemplate vaultTemplate;
//	
//	
//	@PostConstruct
//	public void init() {
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
//		Object tokenPrefixObject = dataMap.get("TOKEN_PREFIX");
//		Object secretObject = dataMap.get("SECRET");
//		Object headerObject = dataMap.get("HEADER_STRING");
//		Object authorityKeyObject = dataMap.get("AUTHORITIES_KEY");
////		Object expirationTimeObject = dataMap.get("EXPIRATION_TIME");
//		String tokenPrefix = (String) tokenPrefixObject;
//		String secret = (String) secretObject;
//		String header = (String) headerObject;
//		String authorityKey = (String) authorityKeyObject;
////		long expirationTime = (long) expirationTimeObject;
//		TOKEN_PREFIX = tokenPrefix;
//		SECRET=secret;
//		HEADER_STRING =header;
//		AUTHORITIES_KEY=authorityKey;
////		EXPIRATION_TIME=expirationTime;
//	}
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String SECRET = "your_secret_key"; 
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "Authorities";
	public static final long EXPIRATION_TIME = 86400000; // 24 hours

}
