package com.sip.ams.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String birthday;
    private int phoneNumber;
    private int verification;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String name, String surname,String email, String password,String birthday,
                  int phoneNumber, int verification, Set<Role> roles) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.verification = verification;
        this.roles = roles;
    }

    public User() {}

    public void setVerification(int verification) {
        this.verification = verification;
    }

    public int getVerification() {
        return verification;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
