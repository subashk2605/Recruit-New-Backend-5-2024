package com.stg.recruit.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stg.recruit.entity.enumuration.EApplicationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Candidate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long candidateId;

	@NotBlank(message = "FirstName is required")
	@Size(max = 40)
	private String firstName;

	@NotBlank(message = "LastName is required")
	@Size(max = 40)
	private String lastName;

//	@NotNull
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate dateOfBirth;

//	@NotNull(message = "Gender Should not be null")
	private String gender;

	@NotBlank(message = "Email is required")
	@Size(max = 40)
	private String email;

//	@Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
//	@NotBlank(message = "Phone No is required")
	private long phoneNo;

	@Digits(integer = 2, fraction = 0, message = "The field must contain only number")
	private int experienceInYear;

	@OneToMany(mappedBy = "candidateSkill", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<Skill> skills;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] resume;

	@Enumerated(EnumType.STRING)
	private EApplicationStatus applicationStatus;

	@OneToMany(mappedBy = "candidateInterviewRef", cascade = CascadeType.ALL)
	@JsonBackReference(value = "candidateInterview")
	private List<Interview> interviews;

	@ManyToOne
	@JoinColumn(name = "userCandidateId", referencedColumnName = "userId")
	@JsonManagedReference(value = "userCandidate")
	private User userCandidateRef;

	private LocalDate createdDate;

	private LocalDate updatedDate;

	private String panNumber;
}
