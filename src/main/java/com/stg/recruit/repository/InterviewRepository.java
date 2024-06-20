package com.stg.recruit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.Interview;
import com.stg.recruit.entity.User;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

//	@Query(value = " SELECT * FROM interview WHERE candidate_interview_id = :candidateId;",nativeQuery = true)
//	public abstract List<Interview> allInterviewsByCandidateId (@Param("candidateId") Long candidateId);

	public abstract List<Interview> findAllInterviewBycandidateInterviewRef(Candidate candidate);
	
    List<Interview> findByUserInterviewRefAndInterviewDate(User interviewer,@org.springframework.data.jpa.repository.Temporal(TemporalType.DATE) Date interviewDate);

}
