package com.stg.recruit.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stg.recruit.entity.enumuration.EInterviewStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "interview")
public class Interview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long interviewId;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime interviewDateTime;

	@Enumerated(EnumType.STRING)
	private EInterviewStatus status;

	@Range(min = 1, max = 2)
	private byte stage;

	private String comment;

	@ManyToOne
	@JoinColumn(name = "candidateInterviewId", referencedColumnName = "candidateId")
	@JsonManagedReference(value = "candidateInterview")
	private Candidate candidateInterviewRef;

	@ManyToOne
	@JoinColumn(name = "userInterviewId", referencedColumnName = "userId")
	@JsonBackReference(value = "userInterview")
	private User userInterviewRef;

	@OneToMany(mappedBy = "interviewSkillRef", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "interviewSkill")
	private Set<Skill> skills;

}

