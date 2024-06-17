package com.stg.recruit.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stg.recruit.entity.dto.AddCandidateDto;
import com.stg.recruit.entity.dto.ScheduleInterviewDto;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.service.CandidateService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/candidates")
public class CandidateController {
	
	@Autowired
    private CandidateService candidateService;

    @PostMapping("/addProfileAndScheduleInterview")
    public ResponseEntity<?> addCandidateProfileAndScheduleInterview(
            @RequestPart AddCandidateDto candidateDto,
            @RequestPart("primarySkills") Set<String> primarySkill,
            @RequestPart("secondarySkills") Set<String> secondarySkill,
            @RequestPart("resume") MultipartFile file,
            @RequestPart List<ScheduleInterviewDto> interviewDtos) {

        try {
            long candidateId = candidateService.addCandidateProfileAndScheduleInterview(
                    candidateDto, primarySkill, secondarySkill, file, interviewDtos);
            return ResponseEntity.ok("Candidate profile added and interviews scheduled successfully. Candidate ID: " + candidateId);
        } catch (RecruitException | IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
