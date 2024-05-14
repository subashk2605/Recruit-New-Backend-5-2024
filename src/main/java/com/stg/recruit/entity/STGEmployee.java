package com.stg.recruit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class STGEmployee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long employeeId;

	private String employeeFirstName;
	private String employeeLastName;

	private String designation;

	private String email;

}

