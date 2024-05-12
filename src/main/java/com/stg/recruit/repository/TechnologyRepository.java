package com.stg.recruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stg.recruit.entity.Technology;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Integer> {

}
