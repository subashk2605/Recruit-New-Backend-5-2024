package com.stg.recruit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.entity.dto.ScheduleInterview;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	 @GetMapping("/stg")
	    public ResponseEntity<List<CustomUserDetails>> getStgEmployees() {
	        try {
	            List<CustomUserDetails> stgEmployees = userService.getStgEmployees();
	            return new ResponseEntity<>(stgEmployees, HttpStatus.OK);
	        } catch (RecruitException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	 

}
