package com.stg.recruit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.Interview;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

//	@Query(value = " SELECT * FROM interview WHERE candidate_interview_id = :candidateId;",nativeQuery = true)
//	public abstract List<Interview> allInterviewsByCandidateId (@Param("candidateId") Long candidateId);

	public abstract List<Interview> findAllInterviewBycandidateInterviewRef(Candidate candidate);
}
