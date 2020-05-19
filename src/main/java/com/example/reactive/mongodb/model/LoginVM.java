package com.example.reactive.mongodb.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginVM {
	@NotEmpty
	@Size(min = 1, max = 50)
	private String username;

	@NotEmpty
	private String password;

	public LoginVM() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
