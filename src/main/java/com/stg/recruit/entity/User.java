package com.stg.recruit.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stg.recruit.entity.enumuration.ERole;
import com.stg.recruit.entity.enumuration.EStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
//@Table(name = "user_table")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@NotBlank(message = "FirstName is required")
	private String firstName;
	@NotBlank(message = "LastName is required")
	private String lastName;

	private String email;
	private String password;
	private String designation;
	private ERole role;
	private boolean accountLockStatus = false;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Otp otpDetails;

	@OneToMany(mappedBy = "userCandidateRef", cascade = CascadeType.ALL)
	@JsonBackReference(value = "userCandidate")
	private List<Candidate> candidates;

	@OneToMany(mappedBy = "userInterviewRef", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "userInterview")
	private List<Interview> interviews;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(getRole());
	}

}
