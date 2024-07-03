package com.api.main.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.api.main.entity.UserEntity;

import lombok.RequiredArgsConstructor;

import java.util.Map;


public interface JwtService {

	String create(Map<String, Object> paramClaim);
	
	String get(String key);

	boolean isUsable(String jwt);

	String getToken();


}

