package com.wipro.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Changed from int to Integer
    
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String role;

    // No Args Constructor
    public User() {
        super();
    }
    
    // Args based Constructor
    public User(Integer id, String name, String email, String password, String role) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    // Args based Constructor where ID will be auto incremented
    public User(String name, String email, String password, String role) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    // getter and setters
    public Integer getId() { // Changed return type to Integer
        return id;
    }

    public void setId(Integer id) { // Changed parameter type to Integer
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ToString to Display User Details
    @Override
    public String toString() {
        return "Id=" + id + ", Name=" + name + ", Email=" + email + ", Password=" + password + ", Role=" + role;
    }
}