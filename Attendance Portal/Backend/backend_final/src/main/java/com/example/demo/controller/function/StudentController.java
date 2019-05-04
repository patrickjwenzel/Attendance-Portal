package com.example.demo.controller.function;

import java.time.Instant;
import java.util.ArrayList;
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
	public @ResponseBody String checkIn(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("lat") float lat, @PathVariable("lon") float lon) {
		try {
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className).get();
			Instant date = DateHandeling.simplify(Calendar.getInstance());
			Attendance a = attendanceRepository.findByUserEmailAndClassNameAndDate(user.email, c.name, date).get();
			//&& Math.abs(lat - c.lat) < 1000 && Math.abs(lon - c.lon) < 1000
			if (c.active.equals("true")) {
				user.attendance.remove(a);
				a.present = true;
				attendanceRepository.save(a);
				//user.activeC.remove(c);
				user.attendance.add(a);
				userRepository.save(user);
				return "true";
			}
			return "false";
		}
		catch (NullPointerException e) {
			return "failure with error";
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
	
	// is a class active
	@GetMapping(path = "/student/get/activeC/{className}")
	public @ResponseBody String isActive(@PathVariable("className") String className) {
		Class c;
		try {
			c = classRepository.findByName(className).get();
			if (c.active.equals("true")) return "true";
			else return "false";
		}
		catch (NullPointerException e) {
			return "invalid";
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
	
	// find active classes
	@GetMapping(path = "/student/get/classes/active/{email}")
	public @ResponseBody List<Class> activeClasses(@PathVariable("email") String email) {
		try {
			List<Class> classes = userRepository.findByEmail(email).get().classes;
			List<Class> active = new ArrayList<Class>();
			Class c;
			int i;
			Instant date = DateHandeling.simplify(Calendar.getInstance());
			for (i = 0; i < classes.size(); i++) {
				c = classes.get(i);
				if (c.active.equals("true") && !attendanceRepository.findByUserEmailAndClassNameAndDateAndPresent(email, c.name, date, true).isPresent()) active.add(c);
			}
			return active;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
}
