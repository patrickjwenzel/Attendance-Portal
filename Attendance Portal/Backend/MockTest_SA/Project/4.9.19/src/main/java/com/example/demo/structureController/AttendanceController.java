package com.example.demo.structureController;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Attendance;
import com.example.demo.model.User;
import com.example.demo.model.Class;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.UserRepository;

@RestController
public class AttendanceController {
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	/*
	 * Need: date
	 */
	
//-------------- Get Mapping -------------------------------------------------------------------------------
	
	// get all attendance
	@GetMapping(path = "/attendance/get/all")
	public @ResponseBody List<Attendance> a1() {
		return (List<Attendance>) attendanceRepository.findAll();
	}
	
	// get attendance for user
	@GetMapping(path = "/attendance/get/id/{id}")
	public @ResponseBody Attendance a2(@PathVariable("id") Integer id) {
		return attendanceRepository.findById(id).get();
	}
	
	// get attendance for user
	@GetMapping(path = "/attendance/get/email/{email}")
	public @ResponseBody List<Attendance> a3(@PathVariable("email") String email) {
		return userRepository.findByEmail(email).get().attendance;
	}
	

//-------------- Post Mapping ------------------------------------------------------------------------------

	// create a new attendance for a user for a class for a date
	@PostMapping(path = "/class/post/create/{userId}/{classId}/{date}")
	public @ResponseBody Attendance a4(@PathVariable("userId") Integer userId, @PathVariable("classId") Integer classId, @PathVariable("date") String date) throws Exception {
		User user = userRepository.findById(userId).get();
		Class c = classRepository.findById(classId).get();
		if (user == null || c == null || date == null) throw new Exception("Invalid class creation!");
		Attendance a = new Attendance(user, c, date, false);
		user.attendance.add(a);
		userRepository.save(user);
		attendanceRepository.save(a);
		return a;
	}
	
	// remove a single attendance instance
	@PostMapping(path = "/attendance/post/delete/{id}")
	public @ResponseBody Attendance a5(@PathVariable("id") Integer id) {
		Attendance remove = attendanceRepository.findById(id).get();
		attendanceRepository.delete(remove);
		return remove;
	}
	
	// remove all attendance records
	@PostMapping(path = "/attendance/post/delete/all")
	public @ResponseBody String a6() {
		attendanceRepository.deleteAll();
		return "All Attendance Records Were Deleted!";
	}
	
	
	
//-------------- Variable Manipulation ---------------------------------------------------------------------
	
}