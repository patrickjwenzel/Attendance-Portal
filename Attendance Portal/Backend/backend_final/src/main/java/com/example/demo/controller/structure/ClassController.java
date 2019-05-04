package com.example.demo.controller.structure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Class;
import com.example.demo.model.User;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.UserRepository;

@RestController
public class ClassController {

	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	UserRepository userRepository;
	
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
			return classRepository.findByName(name).get();
		}
		catch (Exception e) {
			return classRepository.findByName("failure").get();
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
	
	// create new class extra
	@PostMapping(path = "/class/post/create/extra/{name}/{description}/{teacherEmail}/{cInfo}/{lInfo}/{rInfo}")
	public @ResponseBody Class c7(@PathVariable("name") String name, @PathVariable("description") String des, @PathVariable("teacherEmail") String email, @PathVariable("cInfo") String cInfo, @PathVariable("lInfo") String lInfo, @PathVariable("rInfo") String rInfo) {
		try {
			User teacher = userRepository.findByEmail(email).get();
			des = des.replaceAll("_", " ");
			cInfo = cInfo.replaceAll("_", " ");
			lInfo = lInfo.replaceAll("_", " ");
			rInfo = rInfo.replaceAll("_", " ");
			Class c = new Class(name, des, cInfo, lInfo, rInfo);
			classRepository.save(c);
			teacher.classes.add(c);
			userRepository.save(teacher);
			return classRepository.findByName("success").get();
		}
		catch(Exception e) {
			return classRepository.findByName("failure").get();
		}
	}
	
	// delete a class
	@PostMapping(path = "/class/post/delete/{className}")
	public @ResponseBody Class c5(@PathVariable("className") String name) {
		try {	
			Class remove = classRepository.findByName(name).get();
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