package com.stg.recruit.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.stg.recruit.entity.dto.AddCandidateDto;
import com.stg.recruit.entity.dto.ScheduleInterviewDto;
import com.stg.recruit.exception.RecruitException;

public interface CandidateService {
	
	 public abstract long addCandidateProfileAndScheduleInterview(AddCandidateDto candidateDto, Set<String> primarySkill,
             Set<String> secondarySkill, MultipartFile file,
             List<ScheduleInterviewDto> interviewDtos) throws RecruitException, IOException;

}
