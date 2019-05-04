package com.example.demo.controller.structure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.School;
import com.example.demo.repository.SchoolRepository;

@RestController
public class SchoolController {

	@Autowired
	SchoolRepository schoolRepository;

	/*
	 * Need: name
	 */
	
//-------------- Get Mapping -------------------------------------------------------------------------------
	
	// get all schools
	@GetMapping(path = "/school/get/all")
    public @ResponseBody List<School> s1() {
		try {	
			return (List<School>) schoolRepository.findAll();
		}
		catch (NullPointerException e) {
			return null;
		}
    }
	
	// get school
	@GetMapping(path = "/school/get/id/{id}")
    public @ResponseBody School s2(@PathVariable("id") Integer id) {
		try {	
			return schoolRepository.findById(id).get();
		}
		catch (NullPointerException e) {
			return null;
		}
    }
	
	// get school
	@GetMapping(path = "/school/get/name/{name}") 
	public @ResponseBody School s3(@PathVariable("name") String name){
		try {	
			return schoolRepository.findBySName(name).get();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
    
	
	
//-------------- Post Mapping ------------------------------------------------------------------------------
	
	// create a school
	@PostMapping(path = "/school/post/create/{name}")
	public @ResponseBody School s4(@PathVariable("name") String name) {
		try {	
			return schoolRepository.save(new School(name));
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// delete a school
	@PostMapping(path = "/school/post/delete/{id}")
	public @ResponseBody School s5(@PathVariable("id") Integer id) {
		try {
			School remove = schoolRepository.findById(id).get();
			schoolRepository.delete(remove);
			return remove;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// delete all schools
	@PostMapping(path = "/school/post/delete/all")
	public @ResponseBody String s6() {
		try {	
			schoolRepository.deleteAll();
			return "All Schools Were Deleted!";
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	
	
//-------------- Variable Manipulation ---------------------------------------------------------------------
	
}