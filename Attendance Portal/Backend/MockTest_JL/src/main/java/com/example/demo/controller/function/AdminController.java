package com.example.demo.controller.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Class;
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
			Class c = classRepository.findByName(className);
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
			Class c = classRepository.findByName(className);
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
			Class c = classRepository.findByName(className);
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
}
