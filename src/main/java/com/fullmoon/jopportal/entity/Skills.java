package com.fullmoon.jopportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String experienceLevel;
    private String yearsOfExperience;
    @ManyToOne( targetEntity = JobSeeker.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_seeker_id")
    private JobSeeker jobSeeker;

    public Skills() {
    }

    public Skills(String name, String experienceLevel, String yearsOfExperience, JobSeeker jobSeeker) {
        this.name = name;
        this.experienceLevel = experienceLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.jobSeeker = jobSeeker;
    }

    public String getName() {
        return name;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "skills{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", experienceLevel='" + experienceLevel + '\'' +
                ", yearsOfExperience='" + yearsOfExperience + '\'' +
                // Avoid calling toString on jobSeeker to prevent circular reference
                ", jobSeekerId=" + (jobSeeker != null ? jobSeeker.getId() : "null") +
                '}';
    }
}
