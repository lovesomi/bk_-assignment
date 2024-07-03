package com.api.main.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.api.DataNotFoundException;
import com.api.main.UserDto;
import com.api.main.UserLoginDto;
import com.api.main.entity.UserEntity;
import com.api.main.service.impl.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final ModelMapper mapper;

	public void signUp(UserDto userDto) {
		
		UserEntity q = mapper.map(userDto, UserEntity.class);
		
		this.userRepository.save(q);
	}
	
	public UserEntity getUserId(UserDto userDto) {

		Optional<UserEntity> q = this.userRepository.findByUserId(userDto.getUserId());

		if (q.isPresent()) {
			return q.get();
		} else {
			return null;
		}
	
	}
	
	public UserEntity login(UserLoginDto userDto) {

		Optional<UserEntity> q = this.userRepository.findByUserIdAndPassword(userDto.getUserId(), userDto.getPassword());

		if (q.isPresent()) {
			return q.get();
		} else {
			throw new DataNotFoundException("login user not found");
		}
	
	}

}
