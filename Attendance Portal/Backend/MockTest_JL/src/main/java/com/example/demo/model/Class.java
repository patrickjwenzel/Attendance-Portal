package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.core.style.ToStringCreator;

import com.example.demo.utility.IDGenerator;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Class {
	
// ---------- Variables ----------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public Integer classId; // make it auto gen 8 digit number on creation of class
	public String name;
	public String description;
	public float lon;
	public float lat;
	public String active;
	//public Boolean active;
	public String cInfo;	// "{days} / {time {interval like 12-1}} / {location} / {room number}"
	public String rInfo; // "{recitation days} / {recitation time} / {recitation location} / {recitation room number}"
	public String lInfo; // "{lab days} / {lab time} / {lab location} / {lab room number}"
	/*
	@OneToMany
	@JsonBackReference
	public List<Attendance> attendance;
	*/
	@ManyToOne
	public School school;
	@ManyToMany
	@JsonBackReference
	public List<User> users;
	/*
	@OneToMany
	@JsonBackReference
	public List<Message> messages;
	*/
	

// ---------- Constructors ----------
	public Class() {
		init();
	}
	
	public Class(String name) {
		init();
		this.name = name;
	}
	
	public Class(String name, User user) {
		init();
		this.name = name;
		this.users.add(user);
	}
	
	
	
// ---------- Custom Methods ----------
	private void init() {
		this.cInfo = " ";
		this.rInfo = " ";
		this.lInfo = " ";
		this.classId = IDGenerator.classId();
		this.active = "false";
		//this.attendance = new ArrayList<Attendance>();
		this.users = new ArrayList<User>();
		//this.messages = new ArrayList<Message>();
	}
	
	
	
// ---------- To String ----------
    @Override
    public String toString() {
    		return new ToStringCreator(this)
    				.append("ID", this.id)
    				.append("Class ID", this.classId)
    				.append("Name", this.name)
    				.append("Description", this.description)
    				.append("Longitude", this.lon)
    				.append("Latitude", this.lat)
    				.append("Active", this.active)
    				.append("Info", this.cInfo)
    				.append("Info", this.rInfo)
    				.append("Info", this.lInfo)
    				.append("School", this.school)
    				.append("Users", this.users)
    				//.append("Attendance", this.attendance)
    				//.append("Messages", this.messages)
    				.toString();
    }
}
