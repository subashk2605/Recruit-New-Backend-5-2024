package com.stg.recruit.entity.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stg.recruit.entity.enumuration.EApplicationStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddCandidateDto {

	private long candidateId;

	private String firstName;

	private String lastName;

	@Temporal(TemporalType.DATE)
//	@JsonFormat(pattern = "dd-mm-yyyy")
	private LocalDate dateOfBirth;

	private String gender;

	private String email;

	private long phoneNo;

	private int experienceInYear;

	@Temporal(TemporalType.DATE)
//	@JsonFormat(pattern = "dd-mm-yyyy")
	private LocalDate createdDate;

	@Temporal(TemporalType.DATE)
//	@JsonFormat(pattern = "dd-mm-yyyy")
	private LocalDate updatedDate;

	private String panNumber;

	@Enumerated(EnumType.ORDINAL)
	private EApplicationStatus applicationStatus;

}
