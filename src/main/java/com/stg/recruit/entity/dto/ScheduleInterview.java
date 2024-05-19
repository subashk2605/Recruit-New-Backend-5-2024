package com.stg.recruit.entity.dto;


import com.stg.recruit.entity.Candidate;
import com.stg.recruit.entity.Interview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInterview {

	private Candidate candidate;
	
	private Interview interview;

}
