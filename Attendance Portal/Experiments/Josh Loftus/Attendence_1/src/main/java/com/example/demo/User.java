package com.example.demo;

public class User {
	private Long id;
	private String Username;
	private String Password;

	public User() {
		super();
	}

	public User(Long given_Id, String given_Username, String given_Password) {
		id = given_Id;
		Username = given_Username;
	}

	public User(String given_Username, String given_Password) {
		Username = given_Username;
		Password = given_Password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String changed_Username) {
		this.Username = changed_Username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String changed_Password) {
		Password = changed_Password;
	}

}
