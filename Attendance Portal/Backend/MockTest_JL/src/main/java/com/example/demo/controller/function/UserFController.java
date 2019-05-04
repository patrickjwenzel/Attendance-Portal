package com.example.demo.controller.function;

import java.util.ArrayList;
import java.util.List;

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
import com.example.demo.utility.DateHandeling;

@RestController
public class UserFController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	
		
//-------------- Get Mapping -------------------------------------------------------------------------------
	
	// get all users in a class
	@GetMapping(path = "/user/get/class/users/{classId}")
	public @ResponseBody List<User> getUsersInClass(@PathVariable("classId") Integer classId) {
		try {
			return classRepository.findById(classId).get().users;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get attendance records for a student one class
	@GetMapping(path = "/user/get/attendance/{email}/{className}")
	public @ResponseBody List<Attendance> getAttendance(@PathVariable("email") String email, @PathVariable("className") String className){
		try {	
			List<Attendance> specA = new ArrayList<Attendance>();
			List<Attendance> allA = userRepository.findByEmail(email).get().attendance;
			for (int n = 0; n < allA.size(); n++) {
				if(allA.get(n).className.equals(className)) specA.add(allA.get(n));
			}
			return specA;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// user sign in
	@GetMapping(path = "/user/get/signin/{email}/{password}")
	public @ResponseBody User getSignIn(@PathVariable("email") String email, @PathVariable("password") String password) {
		try {
			User user = userRepository.findByEmailAndPassword(email, password);
			if (user == null) return userRepository.findByEmailAndType("failure", "Admin").get(); 
			return user;
		}
		catch (NullPointerException e) {
			return userRepository.findByEmailAndType("failure", "Admin").get();
		}
	}
	
	// get class info
	@GetMapping (path = "/user/get/cInfo/{className}")
	public @ResponseBody String getCInfo(@PathVariable("className") String className) {
		try {
			Class c = classRepository.findByName(className);
			return c.cInfo;
		}
		catch (NullPointerException e) {
			return "failure";
		}
	}
	
	// get recitation info
	@GetMapping (path = "/user/get/rInfo/{className}")
	public @ResponseBody String getRInfo(@PathVariable("className") String className) {
		try {
			Class c = classRepository.findByName(className);
			return c.rInfo;
		}
		catch (NullPointerException e) {
			return "failure";
		}
	}
	
	// get lab info
	@GetMapping (path = "/user/get/lInfo/{className}")
	public @ResponseBody String getLInfo(@PathVariable("className") String className) {
		try {
			Class c = classRepository.findByName(className);
			return c.lInfo;
		}
		catch (NullPointerException e) {
			return "failure";
		}
	}
	
	// get all info
	@GetMapping (path = "/user/get/allInfo/{className}")
	public @ResponseBody List<String> getAllInfo(@PathVariable("className") String className) {
		List<String> info = new ArrayList<String>();
		try {
			Class c = classRepository.findByName(className);
			info.add(c.cInfo);
			info.add(c.rInfo);
			info.add(c.lInfo);
			return info;
		}
		catch (NullPointerException e) {
			info.add("failure");
			return info;
		}
	}
	
	// get attendance for user
	@GetMapping(path = "/user/get/attendance/class/{email}/{className}")
	public @ResponseBody List<Attendance> a7(@PathVariable("email") String email, @PathVariable("className") String className){
		List<Attendance> list = attendanceRepository.findByUserEmailAndClassName(email, className);
		List<Attendance> out = new ArrayList<Attendance>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).present) {
				out.add(list.get(i));
			}
		}
		return out;
	}
	
	// get attendance for a class
	@GetMapping(path = "/user/get/class/attendance/{className}")
	public @ResponseBody List<Attendance> a8(@PathVariable("className") String className){
		List<Attendance> list = attendanceRepository.findByClassName(className);
		List<Attendance> out = new ArrayList<Attendance>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).present) {
				out.add(list.get(i));
			}
		}
		return out;
	}

	// get attendance for a class
	@GetMapping(path = "/user/get/class/attendance/date/{className}/{date}")
	public @ResponseBody List<Attendance> a9(@PathVariable("className") String className, @PathVariable("date") String date){
		List<Attendance> list = attendanceRepository.findByClassNameAndDate(className, DateHandeling.shallowConvert(date));
		List<Attendance> out = new ArrayList<Attendance>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).present) {
				out.add(list.get(i));
			}
		}
		return out;
	}
	
	

//-------------- Post Mapping ------------------------------------------------------------------------------
	
	// add a class
	@PostMapping(path = "/user/post/class/add/{email}/{classId}")
	public @ResponseBody Class addClass(@PathVariable("email") String email, @PathVariable("classId") Integer classId){
		try {
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findById(classId).get();
			if (user.classes.contains(c)) return classRepository.findByName("failure");
			user.classes.add(c);
			c.users.add(user);
			userRepository.save(user); 
			classRepository.save(c);
			return classRepository.findByName("success");
		}
		catch (NullPointerException e) {
			return classRepository.findByName("failure");
		}
	}
	
	// remove a class
	@PostMapping(path = "/user/post/class/remove/{email}/{classId}")
	public @ResponseBody Class RemovedClass(@PathVariable("email") String email, @PathVariable("classId") Integer classId){
		try {	
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findById(classId).get();
			if (!user.classes.contains(c)) return classRepository.findByName("failure");
			user.classes.remove(c);
			c.users.remove(user);
			classRepository.save(c);
			userRepository.save(user);
			return classRepository.findByName("success");
		}
		catch (NullPointerException e) {
			return classRepository.findByName("failure");
		}
	}
	
	// add a class
	@PostMapping(path = "/user/post/class/add/name/{email}/{className}")
	public @ResponseBody Class addClass(@PathVariable("email") String email, @PathVariable("className") String className){
		try {	
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className);
			if (user.classes.contains(c)) return classRepository.findByName("failure");
			user.classes.add(c);
			c.users.add(user);
			userRepository.save(user); 
			classRepository.save(c);
			return c;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
		
	// remove a class
	@PostMapping(path = "/user/post/class/remove/name/{email}/{className}")
	public @ResponseBody Class RemovedClass(@PathVariable("email") String email, @PathVariable("className") String className){
		try {	
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className);
			if (!user.classes.contains(c)) return classRepository.findByName("failure");
			user.classes.remove(c);
			c.users.remove(user);
			classRepository.save(c);
			userRepository.save(user);
			return classRepository.findByName("success");
		}
		catch (NullPointerException e) {
			return classRepository.findByName("failure");
		}
	}
	
	// change password
	@PostMapping(path = "/user/reset/password/{email}/{oldPass}/{newPass}")
	public @ResponseBody String changePass(@PathVariable("email") String email, @PathVariable("oldPass") String oldPass, @PathVariable("newPass") String newPass) {
		try {
			User user = userRepository.findByEmailAndPassword(email, oldPass);
			user.password = newPass;
			userRepository.save(user);
		}
		catch (NullPointerException e) {
			return "Invalid email or password";
		}
		return "Success";
	}
}
