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
	public List<Message> messages;
	
	
	
// ---------- Constructors ----------
	public User() {this.classes = new ArrayList<Class>(); this.attendance = new ArrayList<Attendance>(); this.messages = new ArrayList<Message>();}
	public User(String email, String password, String type) {super(); this.email = email; this.password = password; this.type = type; this.classes = new ArrayList<Class>(); this.attendance = new ArrayList<Attendance>(); this.messages = new ArrayList<Message>();}
	public User(String email, String password, String name, String type, List<Class> classes, List<Attendance> attendance, List<Message> messages) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.type = type;
		this.classes = classes;
		this.attendance = attendance;
		this.messages = messages;
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
                .append("Messages", this.messages)
                .append("Classes", this.classes)
                .append("Attendance", this.attendance)
                .toString();
	}
}