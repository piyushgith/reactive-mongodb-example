package com.example.reactive.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Size;

@Document(collection = "user")
public class User {
	@Id
	private String id;

	@Indexed(unique = true)
	private String login;

	//@Size(min = 60, max = 60)
	private String password;

	//@JsonIgnore
	private Set<Authority> authorities = new HashSet<>();

	public User() {
		super();
	}

	public User(String login,String password, Set<Authority> authorities) {
		super();
		this.login = login;
		this.password = password;
		this.authorities = authorities;
	}

	public User(String id, String login,String password, Set<Authority> authorities) {
		super();
		this.id = id;
		this.login = login;
		this.password = password;
		this.authorities = authorities;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "User [getId()=" + getId() + ", getLogin()=" + getLogin() + ", getPassword()=" + getPassword()
				+ ", getAuthorities()=" + getAuthorities() + "]";
	}

}
