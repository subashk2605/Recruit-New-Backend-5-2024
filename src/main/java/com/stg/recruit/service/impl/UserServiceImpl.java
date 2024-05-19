package com.stg.recruit.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.Interview;
import com.stg.recruit.entity.STGEmployee;
import com.stg.recruit.entity.User;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.entity.dto.ScheduleInterview;
import com.stg.recruit.entity.enumuration.EApplicationStatus;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.repository.CandidateRepository;
import com.stg.recruit.repository.EmployeeRepository;
import com.stg.recruit.repository.InterviewRepository;
import com.stg.recruit.repository.UserRepository;
import com.stg.recruit.service.AuthService;
import com.stg.recruit.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private InterviewRepository interviewRepository;

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

	@Override
	public List<CustomUserDetails> getStgEmployees() throws RecruitException {
		List<STGEmployee> stgEmployees = getAllEmployee();

		List<CustomUserDetails> customUserDetailsList = stgEmployees.stream().map(this::convertToCustomUserDetails)
				.collect(Collectors.toList());
		return customUserDetailsList;
	}

	private CustomUserDetails convertToCustomUserDetails(STGEmployee stgEmployee) {
		
		CustomUserDetails customUserDetails = new CustomUserDetails();
		customUserDetails.setId(stgEmployee.getId());
		customUserDetails.setFullName(stgEmployee.getEmployeeFirstName()+" "+stgEmployee.getEmployeeLastName());
		customUserDetails.setFirstName(stgEmployee.getEmployeeFirstName());
		customUserDetails.setLastName(stgEmployee.getEmployeeLastName());
		customUserDetails.setEmail(stgEmployee.getEmail());
		customUserDetails.setDesignation(stgEmployee.getDesignation());
		return customUserDetails;
	}

	@Override
	public String saveCandidate(Candidate candidate) throws RecruitException {
	    if (!candidateRepository.existsById(candidate.getCandidateId())) {
	        candidate.setApplicationStatus(EApplicationStatus.SAVED);
	        candidate.setCreatedDate(LocalDateTime.now());
	    }
	    candidate.setUpdatedDate(LocalDateTime.now());
	    candidateRepository.save(candidate);
	    return "Saved";
	}


	@Override
	public String saveAndScheduleCandidate(ScheduleInterview scheduleInterview) throws RecruitException {
		Candidate candidate = scheduleInterview.getCandidate();
		Interview interview =  scheduleInterview.getInterview();
		if (candidate != null) {
			candidate.setApplicationStatus(EApplicationStatus.INTERVIEW_SCHEDULED);
			candidate.setUpdatedDate(LocalDateTime.now());
			interview.setCandidateInterviewRef(candidate);
			candidateRepository.save(candidate);
			interviewRepository.save(interview);
			return "Interview Scheduled";
		}else {
			throw new RecruitException();
		}
	}
	
	

}
