package com.example.demo.controller.function;

import java.util.ArrayList;
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
public class AdminController {
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	UserRepository userRepository;
	
	// add class info
	@PostMapping(path = "/admin/post/classCInfo/{email}/{className}/{days}/{time}/{location}/{roomNum}")
	public @ResponseBody String editCInfo(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("days") String days, @PathVariable("time") String time, @PathVariable("location") String location, @PathVariable("roomNum") String roomNum) {
		try {
			Class c = classRepository.findByName(className).get();
			if (!userRepository.findByEmail(email).get().type.equals("admin")) return "failure";
			days = days.toUpperCase();
			String cInfo = days + " ! " + time + " ! " + location + " ! " + roomNum;
			c.cInfo = cInfo;
			classRepository.save(c);
			return cInfo;
		}
		catch (NullPointerException e) {
			return "failure";
		}
	}
	
	// add recitation info
	@PostMapping(path = "/admin/post/classRInfo/{email}/{className}/{days}/{time}/{location}/{roomNum}")
	public @ResponseBody String editRInfo(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("days") String days, @PathVariable("time") String time, @PathVariable("location") String location, @PathVariable("roomNum") String roomNum) {
		try {
			Class c = classRepository.findByName(className).get();
			if (!userRepository.findByEmail(email).get().type.equals("admin")) return "failure";
			days = days.toUpperCase();
			String rInfo = days + " ! " + time + " ! " + location + " ! " + roomNum;
			c.rInfo = rInfo;
			classRepository.save(c);
			return rInfo;
		}
		catch (NullPointerException e) {
			return "failure";
		}
	}
	
	// add lab info
	@PostMapping(path = "/admin/post/classLInfo/{email}/{className}/{days}/{time}/{location}/{roomNum}")
	public @ResponseBody String editLInfo(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("days") String days, @PathVariable("time") String time, @PathVariable("location") String location, @PathVariable("roomNum") String roomNum) {
		try {
			Class c = classRepository.findByName(className).get();
			if (!userRepository.findByEmail(email).get().type.equals("admin")) return "failure";
			days = days.toUpperCase();
			String lInfo = days + " ! " + time + " ! " + location + " ! " + roomNum;
			c.lInfo = lInfo;
			classRepository.save(c);
			return lInfo;
		}
		catch (NullPointerException e) {
			return "failure";
		}
	}
	
	// get students basic info
	@GetMapping(path = "/admin/get/basic/user")
	public @ResponseBody List<String> basicUser() {
		try {
			List<User> list = (List<User>) userRepository.findAll();
			List<String> out = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				if (user.type.equals("student")) out.add(user.name + "!" + user.email);
			}
			return out;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get teachers basic info
	@GetMapping(path = "/admin/get/basic/teacher")
	public @ResponseBody List<String> basicTeacher(){
		try {
			List<User> list = (List<User>) userRepository.findAll();
			List<String> out = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				if (user.type.equals("teacher")) out.add(user.name + "!" + user.email);
			}
			return out;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get class basic info
	@GetMapping(path = "/admin/get/basic/class")
	public @ResponseBody List<String> basicClass(){
		try {
			List<Class> list = (List<Class>) classRepository.findAll();
			List<String> out = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				Class c = list.get(i);
				if (!c.name.equals("success") && !c.name.equals("failure")) out.add(c.name);
			}
			return out;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// admin adds user to class
	@PostMapping(path = "/admin/add/user/class/{email}/{className}/{isTA}")
	public @ResponseBody Class addStudentClass(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("isTA") boolean isTA) {
		try {
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className).get();
			if (user.classes.contains(c) || user.ta.contains(c)) return classRepository.findByName("failure").get();
			if (isTA == true) user.ta.add(c);
			else user.classes.add(c);
			userRepository.save(user);
			return classRepository.findByName("success").get();
		}
		catch (Exception e) {
			return classRepository.findByName("failure").get();
		}
	}
}
