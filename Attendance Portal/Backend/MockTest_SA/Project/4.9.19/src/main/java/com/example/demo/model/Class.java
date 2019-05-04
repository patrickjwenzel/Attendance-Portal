package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.core.style.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Class {
	
// ---------- Variables ----------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public String name;
	public String description;
	public float lon;
	public float lat;
	public Boolean active;
	public Integer section;
	
	@OneToMany
	@JsonBackReference
	public List<Attendance> attendance;
	@ManyToOne
	public School school;
	@ManyToMany
	@JsonBackReference
	public List<User> users;
	@OneToMany
	@JsonBackReference
	public List<Message> messages;
	

// ---------- Constructors ----------
	public Class() {this.active = false; this.attendance = new ArrayList<Attendance>(); this.users = new ArrayList<User>(); this.messages = new ArrayList<Message>();}
	public Class(String name) {super(); this.active = false; this.name = name; this.attendance = new ArrayList<Attendance>(); this.users = new ArrayList<>(); this.messages = new ArrayList<Message>();}
	public Class(String name, User user) {super(); this.active = false; this.name = name; this.attendance = new ArrayList<Attendance>(); this.users = new ArrayList<User>(); this.users.add(user); this.messages = new ArrayList<Message>();}
	public Class(String name, Integer section) {super(); this.active = false; this.name = name; this.attendance = new ArrayList<Attendance>(); this.users = new ArrayList<>(); this.messages = new ArrayList<Message>(); this.section = section;}
	public Class(String name, Integer section, User user) {super(); this.active = false; this.name = name; this.attendance = new ArrayList<Attendance>(); this.users = new ArrayList<User>(); this.users.add(user); this.messages = new ArrayList<Message>(); this.section = section;}
	
	
	
// ---------- To String ----------
    @Override
    public String toString() {
    		return new ToStringCreator(this)
    				.append("ID", this.id)
    				.append("Name", this.name)
    				.append("Description", this.description)
    				.append("Section", this.section)
    				.append("Longitude", this.lon)
    				.append("Latitude", this.lat)
    				.append("Active", this.active)
    				.append("School", this.school)
    				.append("Users", this.users)
    				.append("Attendance", this.attendance)
    				.append("Messages", this.messages)
    				.toString();
    }
}
