package com.example.reactive.mongodb.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection = "authority")
public class Authority implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(max = 50)
	@Id
	private String name;

	public Authority() {
		super();
	}

	public Authority(@NotNull @Size(max = 50) String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Authority authority = (Authority) o;

		return !(name != null ? !name.equals(authority.name) : authority.name != null);
	}

	@Override
	public String getAuthority() {
		return name;
	}
}
