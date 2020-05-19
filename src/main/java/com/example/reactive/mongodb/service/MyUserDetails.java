package com.example.reactive.mongodb.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
    private String username;
    private String password;
    private Collection<String> roles;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// return authorities derived from roles;
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (String role : this.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
		}
		// return grantedAuthorities;
		return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
	}

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
