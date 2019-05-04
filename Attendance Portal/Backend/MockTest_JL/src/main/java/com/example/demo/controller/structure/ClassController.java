package com.example.demo.controller.structure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Class;
import com.example.demo.repository.ClassRepository;

@RestController
public class ClassController {

	@Autowired
	ClassRepository classRepository;
	
	/*
	 * Need: name
	 */
	
//-------------- Get Mapping -------------------------------------------------------------------------------
	
	// get all classes
	@GetMapping(path = "/class/get/all")
	public @ResponseBody List<Class> c1() {
		try {	
			return (List<Class>) classRepository.findAll();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get a class
	@GetMapping(path = "/class/get/id/{id}")
	public @ResponseBody Class c2(@PathVariable("id") Integer id) {
		try {	
			return classRepository.findById(id).get();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get a class
	@GetMapping(path = "/class/get/name/{name}")
	public @ResponseBody Class getClassByName(@PathVariable("name") String name) {
		try {
			return classRepository.findByName(name);
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	
	
//-------------- Post Mapping ------------------------------------------------------------------------------
	
	// create new class
	@PostMapping(path = "/class/post/create/{name}")
	public @ResponseBody Class c4(@PathVariable("name") String name) {
		try {
			return classRepository.save(new Class(name));
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// delete a class
	@PostMapping(path = "/class/post/delete/{id}")
	public @ResponseBody Class c5(@PathVariable("classId") Integer id) {
		try {	
			Class remove = classRepository.findById(id).get();
			classRepository.delete(remove);
			return remove;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// delete all classes
	@PostMapping(path = "/class/post/delete/all")
	public @ResponseBody String c6() {
		try {	
			classRepository.deleteAll();
			return "All Classes Were Deleted!";
		}
		catch (NullPointerException e) {
			return null;
		}
	}

	
	
//-------------- Variable Manipulation ---------------------------------------------------------------------
	
}