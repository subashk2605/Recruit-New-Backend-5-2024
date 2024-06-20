package com.stg.recruit.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.Interview;
import com.stg.recruit.entity.STGEmployee;
import com.stg.recruit.entity.User;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.entity.dto.ScheduleInterview;
import com.stg.recruit.entity.enumuration.ERole;
import com.stg.recruit.entity.enumuration.EStatus;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.repository.UserRepository;
import com.stg.recruit.request.LoginRequest;
import com.stg.recruit.security.config.JwtUtils;


@Service
public interface UserService {

    public abstract List<CustomUserDetails> findAllUsers();
    
    public abstract CustomUserDetails getUserById();
    
	public abstract List<STGEmployee> getAllEmployee() throws RecruitException;
	
	public abstract List<CustomUserDetails> getStgEmployees() throws RecruitException;


    
}
    


