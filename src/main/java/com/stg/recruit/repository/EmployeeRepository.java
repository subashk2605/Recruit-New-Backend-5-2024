package com.stg.recruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stg.recruit.entity.STGEmployee;

public interface EmployeeRepository extends JpaRepository<STGEmployee, Long> {

}
