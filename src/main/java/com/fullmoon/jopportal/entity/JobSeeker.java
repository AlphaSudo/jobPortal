package com.fullmoon.jopportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
@Table(name = "job_seeker_profile")
public class JobSeeker {
    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;
    private String city;
    private String country;
    private String firstName;
    private String lastName;
    @Transient
    private transient MultipartFile profilePicture;
    @Transient
    private transient MultipartFile resume;
    private String photo;
    private String resumePath;
    private String state;
    private String employmentType;
    private String workAuthorization;
    @OneToMany(targetEntity = Skills.class, cascade = CascadeType.ALL,mappedBy = "jobSeeker")
    private List<Skills> skills;
    private String thumbnail;
    private int finished;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startDate;

    public JobSeeker() {
    }

    public JobSeeker(String city, String country,  String firstName, String lastName, MultipartFile profilePicture, String state   , String employmentType, String workAuthorization, MultipartFile resume, List<Skills> skills, String photo, String resumePath,String thumbnail,int finished) {
        this.city = city;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.state = state;
        this.employmentType = employmentType;
        this.workAuthorization = workAuthorization;
        this.resume = resume;
        this.skills = skills;
        this.photo = photo;
        this.resumePath = resumePath;
        this.thumbnail=thumbnail;
        this.finished=finished;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public String getState() {
        return state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public MultipartFile getResume() {
        return resume;
    }

    public void setResume(MultipartFile resume) {
        this.resume = resume;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "JobSeeker{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", state='" + state + '\'' +
                ", employmentType='" + employmentType + '\'' +
                ", workAuthorization='" + workAuthorization + '\'' +
                ", resume='" + resume + '\'' +
                // Avoid calling toString on skills to prevent circular reference
                ", skills=" + (skills != null ? skills.size() : "null") +
                ", photo='" + photo + '\'' +
                ", resumePath='" + resumePath + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", finished='" + finished + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}
