package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.core.style.ToStringCreator;

@Entity
public class User {

// ---------- Variables ----------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public String email;
	public String password;
	public String name;
	public String type;
	
	@ManyToMany
	public List<Class> classes;
	@OneToMany
	public List<Attendance> attendance;
	@OneToMany
	//@JsonBackReference
	public List<Message> messages;
	@OneToMany
	public List<Class> activeC;
	
	
	
// ---------- Constructors ----------
	public User() {
		init();
	}
	
	public User(String email, String password, String type) {
		init();
		this.email = email; 
		this.password = password;
		this.type = type;
	}
	
	
	
// ---------- Custom Methods ----------
	private void init() {
		this.classes = new ArrayList<Class>();
		this.attendance = new ArrayList<Attendance>();
		this.messages = new ArrayList<Message>();
		this.activeC = new ArrayList<Class>();
	}
	
	
	
// ---------- To String ----------
	@Override
    public String toString() {
        return new ToStringCreator(this)
                .append("ID", this.id)
                .append("Email", this.email)
                .append("Password", this.password)
                .append("Name", this.name)
                .append("Type", this.type)
                .append("Active Classes", this.activeC)
                .append("Messages", this.messages)
                .append("Classes", this.classes)
                .append("Attendance", this.attendance)
                .toString();
	}
}