package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.core.style.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public String msg;
	public String date;
	@ManyToOne
	@JsonBackReference
	public User user;
	@ManyToOne
	@JsonBackReference
	public Class classes;
	
	public Message() {}
	public Message(String msg) {this.msg = msg;}
	public Message(String msg, String date, User user) {this.msg = msg; this.date = date; this.user = user;}
	public Message(String msg, String date, User user, Class classes) {this.msg = msg; this.date = date; this.user = user; this.classes = classes;}
	
	@Override
    public String toString() {
        return new ToStringCreator(this)
        		.append("ID", this.id)
        		.append("Date", this.date)
        		.append("Class", this.classes)
        		.append("User", this.user)
        		.append("Message", this.msg)
        		.toString();
	}
}
