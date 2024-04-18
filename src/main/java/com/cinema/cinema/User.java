package com.cinema.cinema;

public class User {
    Integer ID;
    String username;
    String password;
    Boolean isAdmin;
    
    public User(Integer ID, String username, String password, Boolean isAdmin) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
