package com.stg.recruit.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stg.recruit.entity.enumuration.ESkillType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Skill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long skillId;
	@Column(length = 50)
	private String skillName;

	@DecimalMax(value = "5.0", message = "rating range 0.1 to 5.0")
	private BigDecimal rating;

	private ESkillType skillType;

	@ManyToOne
	@JoinColumn(name = "interviewSkillId", referencedColumnName = "interviewId")
	@JsonBackReference(value = "interviewSkill")
	private Interview interviewSkillRef;

	@ManyToOne
	@JsonBackReference
	private Candidate candidateSkill;

}
