package com.cinema.cinema.entities;

public class User {
    public Integer ID;
    public String username;
    public String password;
    public Boolean isAdmin;
    
    public User(Integer ID, String username, String password, Boolean isAdmin) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
