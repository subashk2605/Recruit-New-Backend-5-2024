package com.stg.recruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stg.recruit.entity.Otp;
import com.stg.recruit.entity.User;


@Repository
public interface OtpRepository  extends JpaRepository<Otp, Integer>{
	
	 Otp findByUser(User user);

}
