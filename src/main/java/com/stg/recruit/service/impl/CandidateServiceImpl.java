package com.stg.recruit.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.Interview;
import com.stg.recruit.entity.Skill;
import com.stg.recruit.entity.User;
import com.stg.recruit.entity.dto.AddCandidateDto;
import com.stg.recruit.entity.dto.CustomUserDetails;
import com.stg.recruit.entity.dto.ScheduleInterviewDto;
import com.stg.recruit.entity.enumuration.EApplicationStatus;
import com.stg.recruit.entity.enumuration.EInterviewStatus;
import com.stg.recruit.entity.enumuration.ESkillType;
import com.stg.recruit.exception.RecruitException;
import com.stg.recruit.repository.CandidateRepository;
import com.stg.recruit.repository.InterviewRepository;
import com.stg.recruit.repository.SkillRepository;
import com.stg.recruit.repository.UserRepository;
import com.stg.recruit.service.CandidateService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class CandidateServiceImpl implements CandidateService {

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private AuthServiceImpl authenticationService;

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Validator validator;

	@Autowired
	private ModelMapper mapper;

	@Override
	public long addCandidateProfileAndScheduleInterview(AddCandidateDto candidateDto, Set<String> primarySkill,
			Set<String> secondarySkill, MultipartFile file, List<ScheduleInterviewDto> interviewDtos)
			throws RecruitException, IOException {

		CustomUserDetails userDetails = authenticationService.customUserPrincipal();

		Long userId = userDetails.getId();
		User user = userRepository.findById(userId).orElseThrow(() -> new RecruitException("Recruiter not found"));

		if (candidateRepository.existsByEmailOrPhoneNo(candidateDto.getEmail(), candidateDto.getPhoneNo())) {
			throw new RecruitException("We already have this candidate");
		}

		if ((primarySkill.size() <= 0 || secondarySkill.size() <= 0 || file.isEmpty())) {
			throw new RecruitException("Incomplete form cannot be submitted");
		}

		if (candidateDto.getPhoneNo() < 6000000000L || candidateDto.getPhoneNo() > 9999999999L) {
			throw new RecruitException("Invalid phone number");
		}

		Candidate candidate = new Candidate();
		candidate.setFirstName(candidateDto.getFirstName());
		candidate.setLastName(candidateDto.getLastName());
		candidate.setGender(candidateDto.getGender());
		candidate.setDateOfBirth(candidateDto.getDateOfBirth());
		candidate.setPhoneNo(candidateDto.getPhoneNo());
		candidate.setEmail(candidateDto.getEmail());
		candidate.setExperienceInYear(candidateDto.getExperienceInYear());
		candidate.setPanNumber(candidateDto.getPanNumber().toUpperCase());
		candidate.setCreatedDate(LocalDateTime.now());
		candidate.setApplicationStatus(candidateDto.getApplicationStatus());
		candidate.setUserCandidateRef(user);

		if (!file.isEmpty()) {
			if (!file.getContentType().equals("application/pdf")) {
				throw new RecruitException("File type not supported, only PDF is allowed");
			}
			byte[] pdfContent = file.getBytes();
			candidate.setResume(pdfContent);
		}

		candidateRepository.save(candidate);

		primarySkill.forEach(skillName -> {
			Skill skill = new Skill();
			skill.setSkillName(skillName);
			skill.setSkillType(ESkillType.PRIMARY);
			skill.setCandidateSkill(candidate);
			skillRepository.save(skill);
		});

		secondarySkill.forEach(skillName -> {
			Skill skill = new Skill();
			skill.setSkillName(skillName);
			skill.setSkillType(ESkillType.SECONDARY);
			skill.setCandidateSkill(candidate);
			skillRepository.save(skill);
		});

		// Schedule interviews for both stages
			scheduleInterview(candidate.getCandidateId(), interviewDtos);
		

		return candidate.getCandidateId();
	}

	private void scheduleInterview(Long candidateId, List<ScheduleInterviewDto> scheduleInterviewDtoList) throws RecruitException {
	    Optional<Candidate> optionalCandidate = candidateRepository.findById(candidateId);

	    if (optionalCandidate.isPresent()) {
	        Candidate candidate = optionalCandidate.get();

	        // Ensure candidate's application status allows scheduling
	        if (candidate.getApplicationStatus() != EApplicationStatus.INTERVIEW_SCHEDULED &&
	            candidate.getApplicationStatus() != EApplicationStatus.SAVED_AND_SCHEDULE_LATER) {
	            throw new RecruitException("Candidate application status is not valid for interview scheduling");
	        }

	        List<Interview> interviewsToSave = new ArrayList<>();

	        for (ScheduleInterviewDto scheduleInterviewDto : scheduleInterviewDtoList) {
	            Optional<User> optionalInterviewer = userRepository.findById((long) scheduleInterviewDto.getInterviewerId());

	            if (optionalInterviewer.isPresent()) {
	                User interviewer = optionalInterviewer.get();

	                // Correctly parse and set the interview date and time
	                LocalDate interviewDate = scheduleInterviewDto.getInterviewdate();
	                LocalTime interviewTime = scheduleInterviewDto.getInterviewTime();

	                // Check if the interviewer is available during the proposed time
	                List<Interview> interviewerSchedule = interviewRepository.findByUserInterviewRefAndInterviewDate(interviewer, Date.from(interviewDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	                boolean isAvailable = interviewerSchedule.stream()
	                        .noneMatch(interview -> {
	                            LocalTime existingInterviewStartTime = interview.getInterviewTime();
	                            LocalTime existingInterviewEndTime = existingInterviewStartTime.plusMinutes(30); // Assuming each interview is 30 minutes
	                            LocalTime proposedStartTime = interviewTime;
	                            LocalTime proposedEndTime = proposedStartTime.plusMinutes(45); // Assuming each interview is 45 minutes

	                            // Check for overlap
	                            return !(proposedEndTime.compareTo(existingInterviewStartTime) <= 0 || proposedStartTime.compareTo(existingInterviewEndTime) >= 0);
	                        });

	                if (!isAvailable) {
	                    throw new RecruitException("Interviewer is not available for the scheduled time.");
	                }

	                // Create the Interview entity and add to list to save later
	                Interview interview = new Interview();
	                interview.setInterviewDate(Date.from(interviewDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	                interview.setInterviewTime(interviewTime);
	                interview.setStage(scheduleInterviewDto.getStage());
	                interview.setCandidateInterviewRef(candidate);
	                interview.setUserInterviewRef(interviewer);
	                interview.setStatus(EInterviewStatus.PROGRESS);

	                interviewsToSave.add(interview);

	                // Update candidate's application status if necessary
	                if (candidate.getApplicationStatus() == EApplicationStatus.SAVED_AND_SCHEDULE_LATER) {
	                    candidate.setApplicationStatus(EApplicationStatus.INTERVIEW_SCHEDULED);
	                    // No need to save candidate in loop, save once after loop
	                }
	            } else {
	                throw new RecruitException("Interviewer with ID " + scheduleInterviewDto.getInterviewerId() + " not found");
	            }
	        }

	        // Save all interviews after loop completes
	        interviewRepository.saveAll(interviewsToSave);

	        // Save candidate only once after all interviews are scheduled
	        candidateRepository.save(candidate);
	    } else {
	        throw new RecruitException("Candidate with ID " + candidateId + " not found");
	    }
	}


}
