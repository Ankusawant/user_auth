package com.usermanagement.dto;

import java.util.List;

public class LoginResponse {
	private Long id;
	private List<String> roles;
	private String token;

	public LoginResponse(Long id, List<String> roles, String token) {
		this.id = id;
		this.roles = roles;
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}