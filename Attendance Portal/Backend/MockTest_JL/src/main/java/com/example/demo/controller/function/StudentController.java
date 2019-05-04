package com.example.demo.controller.function;

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
import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.DateHandeling;

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
	
	// check in
	@PostMapping(path = "/student/post/attendance/{email}/{className}/{lat}/{lon}") 
	public @ResponseBody User checkIn(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("lat") float lat, @PathVariable("lon") float lon) {
		try {
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className);
			Instant date = DateHandeling.simplify(Calendar.getInstance());
			Attendance a = attendanceRepository.findByUserEmailAndClassNameAndDate(user.email, c.name, date);
			if (user.classes == null || !user.classes.contains(c) || !c.users.contains(user)) return userRepository.findByEmailAndType("failure", "Admin").get();
			if (c.active.equals("true") && Math.abs(lat - c.lat) < 1000 && Math.abs(lon - c.lon) < 1000) {
				user.attendance.remove(a);
				a.present = true;
				attendanceRepository.save(a);
				user.activeC.remove(c);
				user.attendance.add(a);
				userRepository.save(user);
				return userRepository.findByEmailAndType("success", "Admin").get();
			}
			return userRepository.findByEmailAndType("failure", "Admin").get();
		}
		catch (NullPointerException e) {
			return userRepository.findByEmailAndType("failure", "Admin").get();
		}
	}
	
	// check messages
	@GetMapping(path = "/student/get/msg/{email}")
	public @ResponseBody List<Message> checkMessages(@PathVariable("email") String email) {
		try {
			User user = userRepository.findByEmail(email).get();
			if (!user.type.equals("student")) return messageRepository.findByUserEmailAndClassName("failure", "failure");
			return user.messages;
		}
		catch (NullPointerException e) {
			return messageRepository.findByUserEmailAndClassName("failure", "failure");
		}
	}
	
	// delete message
	@PostMapping(path = "/student/post/delete/msg/{email}/{messages}")
	public @ResponseBody List<Message> deleteMessages(@PathVariable("email") String email, @PathVariable("messages") List<Message> messages){
		try {
			User user = userRepository.findByEmail(email).get();
			List<Message> msgs = messageRepository.findByUserEmail(email);
			user.messages = messages;
			msgs = messages;
			userRepository.save(user);
			messageRepository.saveAll(msgs);
			return messageRepository.findByUserEmailAndClassName("success", "success");
		}
		catch (NullPointerException e) {
			return messageRepository.findByUserEmailAndClassName("failure", "failure");
		}
	}
}
