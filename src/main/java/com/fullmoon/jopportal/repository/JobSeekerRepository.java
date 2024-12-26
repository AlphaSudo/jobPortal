package com.fullmoon.jopportal.repository;

import com.fullmoon.jopportal.entity.JobSeeker;
import com.fullmoon.jopportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Integer> {
    JobSeeker findByFirstName(String firstName);

    JobSeeker findByUser(User authenticatedUser);
}
