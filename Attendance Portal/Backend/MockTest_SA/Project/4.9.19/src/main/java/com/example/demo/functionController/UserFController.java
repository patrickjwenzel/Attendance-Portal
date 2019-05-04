package com.example.demo.functionController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Attendance;
import com.example.demo.model.Class;
import com.example.demo.model.User;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.UserRepository;

@RestController
public class UserFController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	/*
	 * Need: 
	 */
		
//-------------- Get Mapping -------------------------------------------------------------------------------
	
	// get all users in a class
	@GetMapping(path = "/user/get/class/users/{classId}")
	public @ResponseBody List<User> getUsersInClass(@PathVariable("classId") Integer classId) {
		return classRepository.findById(classId).get().users;
	}
	
	// get attendance records for a student one class
	@GetMapping(path = "/user/get/attendance/{email}/{className}")
	public @ResponseBody List<Attendance> getAttendance(@PathVariable("email") String email, @PathVariable("className") String className){
		List<Attendance> specA = new ArrayList<Attendance>();
		List<Attendance> allA = userRepository.findByEmail(email).get().attendance;
		for (int n = 0; n < allA.size(); n++) {
			if(allA.get(n).classes.name.equals(className)) specA.add(allA.get(n));
		}
		return specA;
	}
	
	// user sign in
	@GetMapping(path = "/user/get/signin/{email}/{password}")
	public @ResponseBody Optional<User> getSignIn(@PathVariable("email") String email, @PathVariable("password") String password) {
		if (userRepository.findByEmailAndPassword(email, password).isPresent() == false) return userRepository.findByEmailAndType("failure", "Admin"); 
		Optional<User> user = userRepository.findByEmailAndPassword(email, password);
		return user;
	}
	

//-------------- Post Mapping ------------------------------------------------------------------------------
	
	// add a class
	@PostMapping(path = "/user/post/class/add/{email}/{classId}")
	public @ResponseBody List<Class> addClass(@PathVariable("email") String email, @PathVariable("classId") Integer classId){
		User user = userRepository.findByEmail(email).get();
		Class c = classRepository.findById(classId).get();
		user.classes.add(c);
		c.users.add(user);
		userRepository.save(user); 
		classRepository.save(c);
		return user.classes;
	}
	
	// remove a class
	@PostMapping(path = "/user/post/class/remove/{email}/{classId}")
	public @ResponseBody List<Class> RemovedClass(@PathVariable("email") String email, @PathVariable("classId") Integer classId){
		User user = userRepository.findByEmail(email).get();
		Class c = classRepository.findById(classId).get();
		user.classes.remove(c);
		c.users.remove(user);
		classRepository.save(c);
		userRepository.save(user);
		return user.classes;
	}
	
	// add a class
		@PostMapping(path = "/user/post/class/add/name/{email}/{className}")
		public @ResponseBody List<Class> addClass(@PathVariable("email") String email, @PathVariable("className") String className){
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className).get();
			user.classes.add(c);
			c.users.add(user);
			userRepository.save(user); 
			classRepository.save(c);
			return user.classes;
		}
		
		// remove a class
		@PostMapping(path = "/user/post/class/remove/name/{email}/{className}")
		public @ResponseBody List<Class> RemovedClass(@PathVariable("email") String email, @PathVariable("className") String className){
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className).get();
			user.classes.remove(c);
			c.users.remove(user);
			classRepository.save(c);
			userRepository.save(user);
			return user.classes;
		}
}
