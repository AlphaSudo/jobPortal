package com.fullmoon.jopportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    @Column(name = "email",unique = true)
    @NotEmpty
    private String email;
    @Column(name = "password")
    @NotEmpty
    private String password;
    // is_active Bit(1)
    @Column(name = "is_active", columnDefinition = "Bit(1)")
    private boolean active;
    // regestration date DateTime(6)
    @Column(name = "reg_date", columnDefinition = "DateTime(6)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String regDate;
//     @ManyToOne(cascade = CascadeType.ALL)
//     @JoinColumn(name = "user_type_id")
//     private UserType userType;
    @Column(name = "user_type")
     private String userType;
     @Column(name = "first_name")
     private String firstName;
     @Column(name = "last_name")
     private String lastName;
     private int profiled;
     //relationship type
        @OneToOne(mappedBy = "user")
        private JobSeeker jobSeeker;

    public User() {
    }

    public User(int id, String email, String password, boolean active, String regDate, String userType, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.active = active;
        this.regDate = regDate;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean getActive() {
        return active;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

//    public UserType getUserType() {
//        return userType;
//    }
//
//    public void setUserType(UserType userType) {
//        this.userType = userType;
//    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public int getProfiled() {
        return profiled;
    }

    public void setProfiled(int profiled) {
        this.profiled = profiled;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password=" + password + ", active=" + active + ", regDate=" + regDate + "]";
    }


}
