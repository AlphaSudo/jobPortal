package com.fullmoon.jopportal.repository;

import com.fullmoon.jopportal.entity.Recruiter;
import com.fullmoon.jopportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
    Recruiter findByCompanyName(String companyName);
    Recruiter findByUser(User authenticatedUser);
}
