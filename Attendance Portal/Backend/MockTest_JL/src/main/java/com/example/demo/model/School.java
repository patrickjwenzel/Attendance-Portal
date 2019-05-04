package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.core.style.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class School {

// ---------- Variables ----------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	public String sName;
	
	@OneToMany
	@JsonBackReference
	public List<Class> classes;
	
		

// ---------- Constructors ----------
	public School() {
		init();
	}
	
	public School(String sName) {
		init();
		this.sName = sName;
	}
	

	
// ---------- Custom Methods ----------
	private void init() {
		this.classes = new ArrayList<Class>();
	}
	
	
	
// ---------- To String ----------
	@Override
    public String toString() {
        return new ToStringCreator(this)
                .append("ID", this.id)
                .append("Name", this.sName)
                .append("Classes", this.classes)
                .toString();
	}
}
