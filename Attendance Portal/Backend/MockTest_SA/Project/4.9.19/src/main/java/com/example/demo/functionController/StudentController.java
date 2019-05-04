package com.example.demo.functionController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Attendance;
import com.example.demo.model.Class;
import com.example.demo.model.User;
import com.example.demo.model.Message;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.MessageRepository;
import java.lang.Math;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	@Autowired
	MessageRepository messageRepository;
	
	/*
	 * Need: changed check to return user true or user fail, sign in returns user or user fail, user creation url changed, hardcode in failure/successs user in maincont
	 * updated date to add zero to front 
	 * changed .get( checking for checkin and signin
	 * remove exception
	 * .isPresent( added to error checking
	 * 
	 * error non unique response
	 */
	
	// check in
	@PostMapping(path = "/student/post/attendance/{email}/{className}/{date}/{lat}/{lon}") 
	public @ResponseBody Optional<User> checkIn(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("date") String date, @PathVariable("lat") float lat, @PathVariable("lon") float lon) {
		if (userRepository.findByEmail(email).isPresent() == false || classRepository.findByName(className).isPresent() == false) return userRepository.findByEmailAndType("failure", "Admin");
		User user = userRepository.findByEmail(email).get();
		Class c = classRepository.findByName(className).get();
		if (user == null || user.classes == null || c == null || date == null) return userRepository.findByEmailAndType("failure", "Admin");
		if (date.charAt(1) == '.') {date = "0" + date;}
		if (attendanceRepository.findByUserAndClassesAndDate(user, c, date).isPresent() == false || !c.users.contains(user)) return userRepository.findByEmailAndType("failure", "Admin"); 
		Attendance a = attendanceRepository.findByUserAndClassesAndDate(user, c, date).get();
		userRepository.save(user);
		classRepository.save(c);
		attendanceRepository.save(a);
		if (c.active == true && Math.abs(lat - c.lat) < 1000 && Math.abs(lon - c.lon) < 1000) {
			a.present = true; 
			attendanceRepository.save(a); 
			userRepository.save(user); 
			return userRepository.findByEmail("success");}
		return userRepository.findByEmailAndType("failure", "Admin");
	}
	
	// check messages
	@GetMapping(path = "/student/get/msg/{email}")
	public @ResponseBody List<Message> checkMessages(@PathVariable("email") String email) throws Exception {
		User user = userRepository.findByEmail(email).get();
		if (user == null || !user.type.equals("student")) throw new Exception("Invalid user");
		return user.messages;
	}
	
	/*
	 * could have problem with order of save
	 */
	// delete message
	@PostMapping(path = "/student/post/delete/msg/{email}/{messages}")
	public @ResponseBody List<Message> deleteMessages(@PathVariable("email") String email, @PathVariable("messages") List<Message> messages){
		User user = userRepository.findByEmail(email).get();
		List<Message> msgs = messageRepository.findByUser(user);
		user.messages = messages;
		msgs = messages;
		userRepository.save(user);
		messageRepository.saveAll(msgs);
		return msgs;
	}
}
