package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.core.style.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Attendance {

// ---------- Variables ----------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public String date;
	public Boolean present;
	@ManyToOne
	public Class classes;
	@ManyToOne
	@JsonBackReference
	public User user;
	
	
	
// ---------- Constructors ----------
	public Attendance() {this.present = false;}
	public Attendance(String date) {this.date = date;}
	public Attendance(User user, Class classes, String date, Boolean present) {
		super();
		this.user = user;
		this.date = date;
		this.classes = classes;
		this.present = present;}

	
	
// ---------- To String ----------
	@Override
    public String toString() {
        return new ToStringCreator(this)
                .append("ID", this.id)
                .append("Date", this.date)
                .append("User", this.user)
                .append("Class", this.classes)
                .append("Present", this.present)
                .toString();
	}	
}
