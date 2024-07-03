package com.api.main.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.api.main.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtServiceImpl implements JwtService  {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

	private static final String HEADER_AUTH = "Authorization";
	private static final String SALT = "testSecrethaethaet51435134527testSecaergaegetKey20aeth124";
	
	@Override
	public String create(Map<String, Object> paramClaim) {
		/* 토큰 만료 기간은 우선 1년으로 함. */
		Calendar cal = Calendar.getInstance();
		Date expDate = new Date();
		cal.setTime(expDate);
		//cal.setTime(expDate);
		cal.add(Calendar.YEAR, 1);
		expDate = cal.getTime();
		
		String jwt = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "HS256")
				.setHeaderParam("insertDt", System.currentTimeMillis())
				.setClaims(paramClaim)
				.setExpiration(expDate)
				.signWith(SignatureAlgorithm.HS256, this.generateKey())
				.compact();
		
		return jwt;
	}
	
	
	private byte[] generateKey() {
		byte[] key = null;
		try {
			key = SALT.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			if(logger.isInfoEnabled()) {
				ex.printStackTrace();
			} else {
				logger.error("Create Bytes of JWT key ERROR ::: {}", ex.getMessage());
			}
		}
		
		return key;
	}
	
	@Override
	public String getToken() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = (!StringUtils.isEmpty(request.getHeader(HEADER_AUTH)))
						? request.getHeader(HEADER_AUTH).split(";")[0]
						: request.getHeader(HEADER_AUTH);
		return jwt;
	}
	
	@Override
	public String get(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = (!StringUtils.isEmpty(request.getHeader(HEADER_AUTH)))
				? request.getHeader(HEADER_AUTH).split(";")[0]
				: request.getHeader(HEADER_AUTH);
		
		if (jwt != null) {
			// Bearer replace
			jwt = jwt.replaceAll("^Bearer( )*", "");
		}

		String value = "";
		Jws<Claims> claims = null;
		try {
			if(jwt != null && !jwt.equals("")) {
				claims = Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(jwt);
				value = (String) claims.getBody().get(key);
			}
		} catch (Exception ex) {

		}
		
		return value;
	}
	
	@Override
	public boolean isUsable(String jwt) {
		try {			
			Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
		} catch (Exception ex) {

		}
		return true;
	}
}
