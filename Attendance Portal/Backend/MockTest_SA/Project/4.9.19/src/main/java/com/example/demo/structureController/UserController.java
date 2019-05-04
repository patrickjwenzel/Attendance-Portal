package com.example.demo.structureController;

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
	
	/*
	 * Need: name, email, password editablility
	 */
	
//-------------- Get Mapping -------------------------------------------------------------------------------
	
    @GetMapping(path = "/user/hello")
    public String helloWorld() {
        return "hello world";
    }

        
	// get all users
	@GetMapping(path = "/user/get/all")
	public @ResponseBody List<User> u1() {
		return (List<User>) userRepository.findAll();
	}
	
	// get user
	@GetMapping(path = "/user/get/id/{id}")
	public @ResponseBody User u2(@PathVariable("id") Integer id) {
		return userRepository.findById(id).get();
	}
	
	// Warning name != unique
	// get user
	@GetMapping(path = "/user/get/name/{name}")
	public @ResponseBody User u3(@PathVariable("name") String name) {
		return userRepository.findByName(name).get();
	}
	
	// get user
	@GetMapping(path = "/user/get/email/{email}")
	public @ResponseBody User u4(@PathVariable("email") String email) {
		return userRepository.findByEmail(email).get();
	}
	
	
	
//-------------- Post Mapping ------------------------------------------------------------------------------

	// create a user
	@PostMapping(path = "/user/post/create/user/{email}/{password}/{type}")
	public @ResponseBody User u5(@PathVariable("email") String email, @PathVariable("password") String password, @PathVariable("type") String type) {
		if (userRepository.findByEmail(email).get() != null || email == null || password == null || type == null) return userRepository.findByEmail("failure").get();
		return userRepository.save(new User(email, password, type));
	}
	
	// delete a user
	@PostMapping(path = "/user/post/delete/{id}")
	public @ResponseBody User u6(@PathVariable("id") Integer id) {
		User remove = userRepository.findById(id).get();
		userRepository.delete(userRepository.findById(id).get());
		return remove;
	}
	
	// delete all users
	@PostMapping(path = "/user/post/delete/all")
	public @ResponseBody String u7() {
		userRepository.deleteAll();
		return "All Users Were Deleted!";
	}

	
	
//-------------- Variable Manipulation ---------------------------------------------------------------------
	
	// change type of user
	@PostMapping(path = "/user/post/change/type/{email}/{type}")
	public @ResponseBody User changeType(@PathVariable("email") String email, @PathVariable("type") String type) {
		if (type.equals("student") || type.equals("admin") || type.equals("teacher") || type.equals("teacherAssistant")) {
			userRepository.findByEmail(email).get().type = type;}
		userRepository.save(userRepository.findByEmail(email).get());
		return userRepository.findByEmail(email).get();
	}
}