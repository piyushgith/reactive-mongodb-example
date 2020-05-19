package com.example.reactive.mongodb.security.jwt;

public class JWTToken {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JWTToken(String token) {
		super();
		this.token = token;
	}

	public JWTToken() {
		super();
	}

}
