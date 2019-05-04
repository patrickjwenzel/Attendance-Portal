package com.example.demo.controller.structure;


import java.time.Instant;
import java.util.Calendar;
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
public class AttendanceController {
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	
	
//-------------- Get Mapping -------------------------------------------------------------------------------
	
	// get all attendance
	@GetMapping(path = "/attendance/get/all")
	public @ResponseBody List<Attendance> a1() {
		try {
			return (List<Attendance>) attendanceRepository.findAll();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get attendance for user
	@GetMapping(path = "/attendance/get/id/{id}")
	public @ResponseBody Attendance a2(@PathVariable("id") Integer id) {
		try {
			return attendanceRepository.findById(id).get();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// get attendance for user
	@GetMapping(path = "/attendance/get/email/{email}")
	public @ResponseBody List<Attendance> a3(@PathVariable("email") String email) {
		try {
			return attendanceRepository.findByUserEmail(email);
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	public Attendance getAttendanceByUserEmailAndClassNameAndDate(String userEmail, String className, Instant date) {
		return attendanceRepository.findByUserEmailAndClassNameAndDate(userEmail, className, date).get();
	}
	
	

//-------------- Post Mapping ------------------------------------------------------------------------------

	// create a new attendance for a user for a class for a date
	@PostMapping(path = "/class/post/create/{userId}/{classId}")
	public @ResponseBody Attendance a4(@PathVariable("userId") Integer userId, @PathVariable("classId") Integer classId) {
		try {
			User user = userRepository.findById(userId).get();
			Class c = classRepository.findById(classId).get();
			Instant date = DateHandeling.simplify(Calendar.getInstance());
			Attendance a = new Attendance(user.email, c.name, date, false);
			user.attendance.add(a);
			userRepository.save(user);
			attendanceRepository.save(a);
			return a;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// remove a single attendance instance
	@PostMapping(path = "/attendance/post/delete/{id}")
	public @ResponseBody Attendance a5(@PathVariable("id") Integer id) {
		try {
			Attendance remove = attendanceRepository.findById(id).get();
			attendanceRepository.delete(remove);
			return remove;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	// remove all attendance records
	@PostMapping(path = "/attendance/post/delete/all")
	public @ResponseBody String a6() {
		try {
			attendanceRepository.deleteAll();
			return "All Attendance Records Were Deleted!";
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	
	
//-------------- Variable Manipulation ---------------------------------------------------------------------
	
}