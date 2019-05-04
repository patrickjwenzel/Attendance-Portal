package com.example.demo.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.core.style.ToStringCreator;

@Entity
public class Message {

// ---------- Variables ----------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public String msg;
	public Instant date;
	public String userEmail;
	public String className;
	
	/*
	@ManyToOne
	@JsonBackReference
	public User user;
	@ManyToOne
	@JsonBackReference
	public Class classes;
	*/
	
	
// ---------- Constructors ----------
	public Message() {
		init();
	}
	
	public Message(String msg) {
		init();
		this.msg = msg;
	}
	
	/*
	public Message(String msg, String date, User user) {
		init();
		this.msg = msg;
		this.date = date;
		this.user = user;
	}
	
	public Message(String msg, String date, User user, Class classes) {
		init();
		this.msg = msg;
		this.date = date;
		this.user = user;
		this.classes = classes;
	}
	*/
	
	// for failure / success
	public Message(String userEmail, String className) {
		init();
		this.userEmail = userEmail;
		this.className = className;
	}
	
	public Message(String msg, Instant date, String userEmail) {
		init();
		this.msg = msg;
		this.date = date;
		this.userEmail = userEmail;
	}
	
	public Message(String msg, Instant date, String userEmail, String className) {
		init();
		this.msg = msg;
		this.date = date;
		this.userEmail = userEmail;
		this.className = className;
	}
	

	
// ---------- Custom Methods ----------
	private void init() {
		
	}
	
	
	
// ---------- To String ----------
	@Override
    public String toString() {
        return new ToStringCreator(this)
        		.append("ID", this.id)
        		.append("Date", this.date)
        		//.append("Class", this.classes)
        		//.append("User", this.user)
        		.append("User", this.userEmail)
        		.append("Class", this.className)
        		.append("Message", this.msg)
        		.toString();
	}
}
