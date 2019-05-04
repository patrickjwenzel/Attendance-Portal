package com.example.demo.structureController;

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
		return (List<School>) schoolRepository.findAll();
    }
	
	// get school
	@GetMapping(path = "/school/get/id/{id}")
    public @ResponseBody School s2(@PathVariable("id") Integer id) {
		return schoolRepository.findById(id).get();
    }
	
	// Warning name != unique
	// get school
	@GetMapping(path = "/school/get/name/{name}") 
	public @ResponseBody School s3(@PathVariable("name") String name){
		return schoolRepository.findBySName(name).get();
	}
    
	
	
//-------------- Post Mapping ------------------------------------------------------------------------------
	
	// create a school
	@PostMapping(path = "/school/post/create/{name}")
	public @ResponseBody School s4(@PathVariable("name") String name) {
		return schoolRepository.save(new School(name));
	}
	
	// delete a school
	@PostMapping(path = "/school/post/delete/{id}")
	public @ResponseBody School s5(@PathVariable("id") Integer id) {
		School remove = schoolRepository.findById(id).get();
		schoolRepository.delete(remove);
		return remove;
	}
	
	// delete all schools
	@PostMapping(path = "/school/post/delete/all")
	public @ResponseBody String s6() {
		schoolRepository.deleteAll();
		return "All Schools Were Deleted!";
	}
	
	
	
//-------------- Variable Manipulation ---------------------------------------------------------------------
	
}