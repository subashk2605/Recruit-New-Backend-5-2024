package com.stg.recruit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stg.recruit.entity.STGEmployee;
import com.stg.recruit.entity.User;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.repository.EmployeeRepository;
import com.stg.recruit.repository.UserRepository;
import com.stg.recruit.service.AuthService;
import com.stg.recruit.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AuthService authService;

	@Override
	public List<CustomUserDetails> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(this::convertToCustomUserDetails).collect(Collectors.toList());
	}

	private CustomUserDetails convertToCustomUserDetails(User user) {
		return modelMapper.map(user, CustomUserDetails.class);
	}

	@Override
	public CustomUserDetails getUserById() {
		return authService.customUserPrincipal();
	}	
	
	@Override
	public List<STGEmployee> getAllEmployee() throws RecruitException {	
         return employeeRepository.findAll();
	
	}
	
	
	
	

}
