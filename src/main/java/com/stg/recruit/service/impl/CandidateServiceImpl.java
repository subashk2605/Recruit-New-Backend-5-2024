package com.stg.recruit.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
		candidate.setCreatedDate(LocalDate.now());
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
		for (ScheduleInterviewDto interviewDto : interviewDtos) {
			scheduleInterview(candidate.getCandidateId(), interviewDto);
		}

		return candidate.getCandidateId();
	}

	private Interview scheduleInterview(Long candidateId, ScheduleInterviewDto interviewDto) throws RecruitException {
		Optional<User> optionalInterviewer = userRepository.findById((long) interviewDto.getInterviewerId());
		Optional<Candidate> optionalCandidate = candidateRepository.findById(candidateId);

		if (optionalInterviewer.isPresent() && optionalCandidate.isPresent()) {
			Candidate candidate = optionalCandidate.get();
			if (candidate.getApplicationStatus() == EApplicationStatus.INTERVIEW_SCHEDULED
					|| candidate.getApplicationStatus() == EApplicationStatus.INTERVIEW_SCHEDULED) {

				User interviewer = optionalInterviewer.get();

				// Correctly parse and set the interview date and time
				Date interviewDate = Date
						.from(interviewDto.getInterviewdate().atStartOfDay(ZoneId.systemDefault()).toInstant());
				LocalTime interviewTime = interviewDto.getInterviewTime();

				Interview interview = new Interview();
				interview.setInterviewDate(interviewDate);
				interview.setInterviewTime(interviewTime);
				interview.setStage(interviewDto.getStage());
				interview.setCandidateInterviewRef(candidate);
				interview.setUserInterviewRef(interviewer);
				interview.setStatus(EInterviewStatus.PROGRESS);

				// Update candidate's application status only if it's appropriate
				if (candidate.getApplicationStatus() == EApplicationStatus.SAVED_AND_SCHEDULE_LATER) {
					candidate.setApplicationStatus(EApplicationStatus.SAVED_AND_SCHEDULE_LATER);
					candidateRepository.save(candidate);
				}

				interviewRepository.save(interview);

				return interview;
			} else {
				throw new RecruitException("Candidate application status is not valid for interview scheduling");
			}
		} else {
			throw new RecruitException("No interviewer or candidate found");
		}
	}

}
