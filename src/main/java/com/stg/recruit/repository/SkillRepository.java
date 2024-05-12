package com.stg.recruit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

	public List<Skill> findAllByCandidateSkill(Candidate candidate);

}
