package com.example.reactive.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "user")
public class User {
    @Id
    private String username;
    private String password;
    private Collection<String> roles;

    public User(String username, String password, Collection<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public UserDetails toUserDetails() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (String role : this.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
        }
        return new org.springframework.security.core.userdetails.User(this.getUsername(), this.getPassword(), grantedAuthorities);
    }
}
