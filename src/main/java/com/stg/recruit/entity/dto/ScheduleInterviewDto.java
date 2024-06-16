package com.stg.recruit.entity.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stg.recruit.entity.enumuration.EApplicationStatus;

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
public class ScheduleInterviewDto {
	
	private int candidateId;
		
	private int interviewerId;
	
	@Temporal(TemporalType.DATE)
	private LocalDate interviewdate;
	
	@Temporal(TemporalType.TIME)
	private LocalTime interviewTime;
	
	private byte stage;
		
	
}
