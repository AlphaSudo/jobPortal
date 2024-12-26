package com.fullmoon.jopportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "recruiter_profile")
public class Recruiter {
    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    private String city;
    @Column(name = "company", nullable = false)
    private String companyName;
    private String country;
    private String firstName;
    private String lastName;
    @Column(name = "profile_photo" ,columnDefinition = "VARCHAR(64) DEFAULT 'default.jpg'", nullable = true)
    private String profilePic;
    private String state;


    @Transient
    private transient MultipartFile profilePicture;

    private int finished;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startDate;

    public Recruiter() {
    }

    public Recruiter(User user, String city, String companyName, String country, String firstName, String lastName, String profilePic, String state) {
        this.user = user;
        this.city = city;
        this.companyName = companyName;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePic = profilePic;
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public String getCity() {
        return city;
    }

    public String getCompanyName() {
        return companyName;
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

    public String getProfilePic() {
        return profilePic;
    }

    public String getState() {
        return state;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
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
        return "Recruiter [user=" + user + ", city=" + city + ", companyName=" + companyName + ", country=" + country + ", firstName=" + firstName + ", lastName=" + lastName + ", profilePicture=" + profilePicture + ", state=" + state + "]";
    }


}
