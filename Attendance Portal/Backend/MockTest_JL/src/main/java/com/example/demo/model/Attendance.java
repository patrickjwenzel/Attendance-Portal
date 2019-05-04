package com.example.demo.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.core.style.ToStringCreator;

@Entity
public class Attendance {

// ---------- Variables ----------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public Instant date;
	public String userEmail;
	public String className;
	public Boolean present;
	
	
	
// ---------- Constructors ----------
	public Attendance() {
		init();
	}
	
	public Attendance(Instant date) {
		init();
		this.date = date;
	}
	
	// for failure / success
	public Attendance(String userEmail, String className) {
		this.userEmail = userEmail;
		this.className = className;
	}
	
	public Attendance(String email, String className, Instant date, Boolean present) {
		init();
		this.userEmail = email;
		this.date = date;
		this.className = className;
		this.present = present;
	}
	

	
// ---------- Custom Methods ----------
	private void init() {
		this.present = false;
	}
	
	
	
// ---------- To String ----------
	@Override
    public String toString() {
        return new ToStringCreator(this)
                .append("ID", this.id)
                .append("Date", this.date)
                .append("User", this.userEmail)
                .append("Class", this.className)
                .append("Present", this.present)
                .toString();
	}	
}
