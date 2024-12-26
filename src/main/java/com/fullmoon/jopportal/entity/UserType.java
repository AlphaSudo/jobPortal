//package com.fullmoon.jopportal.entity;
//
//import jakarta.persistence.*;
//
//import java.util.List;
//
//@Entity
//@Table(name = "users_type")
//public class UserType {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_type_id")
//    private int id;
//    @Column(name = "user_type")
//    private String userType;
//    @OneToMany(mappedBy = "userType", cascade = CascadeType.ALL)
//    private List<User> users;
//
//    public UserType() {
//    }
//
//    public UserType(int id, String userType, List<User> users) {
//        this.id = id;
//        this.userType = userType;
//        this.users = users;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getUserType() {
//        return userType;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setUserType(String userType) {
//        this.userType = userType;
//    }
//
//    public List<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<User> users) {
//        this.users = users;
//    }
//
//    @Override
//    public String toString() {
//        return "UserType{" +
//                "id=" + id +
//                ", userType='" + userType + '\'' +
//                '}';
//    }
//}
