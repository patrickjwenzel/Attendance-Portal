package com.example.demo.controller.structure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;


@RestController
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
//-------------- Get Mapping -------------------------------------------------------------------------------
	
	// get all users
	@GetMapping(path = "/user/get/all")
	public @ResponseBody List<User> u1() {
		try {
			List<User> list = (List<User>) userRepository.findAll();
			return list;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get user
	@GetMapping(path = "/user/get/id/{id}")
	public @ResponseBody User u2(@PathVariable("id") Integer id) {
		try {
			return userRepository.findById(id).get();
		}
		catch (NullPointerException e) {
			return null;
		}
	}

	// get user
	@GetMapping(path = "/user/get/name/{name}")
	public @ResponseBody User u3(@PathVariable("name") String name) {
		try {
			return userRepository.findByName(name).get();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get user
	@GetMapping(path = "/user/get/email/{email}")
	public @ResponseBody User u4(@PathVariable("email") String email) {
		try {	
			return userRepository.findByEmail(email).get();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	public User getUserByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}
	
	
	
//-------------- Post Mapping ------------------------------------------------------------------------------

	// create a user
	@PostMapping(path = "/user/post/create/user/{email}/{password}/{type}")
	public @ResponseBody User u5(@PathVariable("email") String email, @PathVariable("password") String password, @PathVariable("type") String type) {
		try {	
			if (userRepository.findByEmail(email).get() != null || email == null || password == null || type == null) return userRepository.findByEmail("failure").get();
			return userRepository.save(new User(email, password, type));
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// delete a user
	@PostMapping(path = "/user/post/delete/{id}")
	public @ResponseBody User u6(@PathVariable("id") Integer id) {
		try {	
			User remove = userRepository.findById(id).get();
			userRepository.delete(userRepository.findById(id).get());
			return remove;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// delete all users
	@PostMapping(path = "/user/post/delete/all")
	public @ResponseBody String u7() {
		try {	
			userRepository.deleteAll();
			return "All Users Were Deleted!";
		}
		catch (NullPointerException e) {
			return null;
		}
	}

	
	
//-------------- Variable Manipulation ---------------------------------------------------------------------
	
	// change type of user
	@PostMapping(path = "/user/post/change/type/{email}/{type}")
	public @ResponseBody User changeType(@PathVariable("email") String email, @PathVariable("type") String type) {
		try {	
			if (type.equals("student") || type.equals("admin") || type.equals("teacher") || type.equals("teacherAssistant")) {
				userRepository.findByEmail(email).get().type = type;}
			userRepository.save(userRepository.findByEmail(email).get());
			return userRepository.findByEmail(email).get();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
}