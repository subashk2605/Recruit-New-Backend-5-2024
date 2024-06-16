package com.stg.recruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stg.recruit.entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	public abstract Optional<Candidate> findById(Long candidateId);

	@Query(value = "SELECT * FROM Candidate c WHERE c.email = :email OR c.phone_no = :phoneNo", nativeQuery = true)
	Candidate findByEmailOrPhoneNo(@Param("email") String email, @Param("phoneNo") Long phoneNo);

	@Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END "
			+ "FROM Candidate  WHERE email = :email OR phoneNo = :phoneNo")
	public abstract boolean existByEmailOrPhoneNo(String email, long phoneNo);

	public abstract boolean existsByEmailOrPhoneNo(String email, long phoneNo);

}
