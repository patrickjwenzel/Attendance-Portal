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
public class TeacherController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	@Autowired
	MessageRepository messageRepository;
	
	// get students in class
	@GetMapping(path = "/teacher/get/students/{className}")
	public @ResponseBody List<User> getStudents(@PathVariable("className") String className) {
		List<User> students = new ArrayList<User>();
		try {
			Class c = classRepository.findByName(className);
			for (int i = 0; i < c.users.size(); i++) {
				if (c.users.get(i).type.equals("student")) students.add(c.users.get(i));
			}
			return students;
		}
		catch (NullPointerException e) {
			students.add(userRepository.findByEmailAndType("failure", "admin").get());
			return students;
		}
	}
	
	// create a class
	@PostMapping(path = "/teacher/post/create/class/{email}/{className}")
	public @ResponseBody Class createClass(@PathVariable("email") String email, @PathVariable("className") String className) {
		try {
			if (!userRepository.findByEmail(email).isPresent() || userRepository.findByEmail(email).get().type != "teacher" || className == null) return classRepository.findByName("failure");
			return classRepository.save(new Class(className, userRepository.findByEmail(email).get()));
		}
		catch (NullPointerException e) {
			return classRepository.findByName("failure");
		}
	}
	
	// start a class
	@PostMapping(path = "/teacher/post/start/{email}/{className}/{lat}/{lon}")
	public @ResponseBody String startClass(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("lat") float lat, @PathVariable("lon") float lon) {
		try {
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className);
			if (!user.type.equals("teacher") || !user.classes.contains(c) || c.active.equals("true")) return "failure to start";
			List<User> students = c.users;
			for (int i = 0; i < students.size(); i++) {
				User temp = students.get(i);
				if (temp.type.equals("student")) {
					Instant date = DateHandeling.simplify(Calendar.getInstance());
					Attendance a = new Attendance(temp.email, c.name, date, false);
					attendanceRepository.save(a);
					Message msg = new Message(c.name + " has started", date, user.email, c.name);
					messageRepository.save(msg);
					temp.activeC.add(c);
					temp.attendance.add(a);
					temp.messages.add(msg);
					userRepository.save(temp);
				}
			}
			c.lat = lat;
			c.lon = lon;
			c.active = "true";
			classRepository.save(c);
			return "Class " + c.name + " has been started!";
		}
		catch (NullPointerException e) {
			return "general failure"; 
		}
	}
	
	// stop a class
	@PostMapping(path = "/teacher/post/stop/{email}/{className}")
	public @ResponseBody List<User> stopClass(@PathVariable("email") String email, @PathVariable("className") String className) {
		List<User> list = new ArrayList<User>();
		try {
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className);
			if (!user.type.equals("teacher") || !user.classes.contains(c) || c.active.equals("false")) {
				list.add(userRepository.findByEmailAndType("failure", "admin").get());
				return list;
			}
			List<User> students = c.users;
			for (int i = 0; i < students.size(); i++) {
				User temp = students.get(i);
				if (temp.type.equals("student")) {
					Instant date = DateHandeling.simplify(Calendar.getInstance());
					Message msg = new Message(c.name + " has ended", date, user.email, c.name);
					messageRepository.save(msg);
					temp.messages.add(msg);
					temp.activeC.remove(c);
					userRepository.save(temp);
					if (temp.attendance.get(temp.attendance.size() - 1).present == true) {
						list.add(temp);
					}
				}
			}
			c.lat = 0;
			c.lon = 0;
			c.active = "false";
			classRepository.save(c);
			return list;
		}
		catch (NullPointerException e) {
			list.add(userRepository.findByEmailAndType("failure", "admin").get());
			return list;
		}
	}
	
	// change a students attendance
	@PostMapping(path = "/teacher/post/swap/{studentEmail}/{className}/{date}")
	public @ResponseBody Attendance changeAttendance(@PathVariable("studentEmail") String studentEmail, @PathVariable("className") String className, @PathVariable("date") String date) {
		try {
			User student = userRepository.findByEmail(studentEmail).get();
			//User teacher = userRepository.findByEmail(teacherEmail).get();
			Class c = classRepository.findByName(className);
			//Instant dateA = DateHandeling.convert(date);
			Instant dateA = DateHandeling.shallowConvert(date);
			Attendance a = attendanceRepository.findByUserEmailAndClassNameAndDate(student.email, c.name, dateA);
			student.attendance.remove(a);
			if (a.present == false) a.present = true;
			else a.present = false;
			attendanceRepository.save(a);
			student.attendance.add(a);
			userRepository.save(student);
			return a;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	// add and change class description
	@PostMapping(path = "/teacher/post/des/add/{email}/{className}/{description}")
	public @ResponseBody String addDescription(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("description") String description) {
		try {
			User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className);
			if (user == null || !user.type.equals("teacher") || c == null || !c.users.contains(user)) return "failure";
			if (description.length() > 50) description = description.substring(0, 49);
			description = description.replace("_", " ");
			c.description = description;
			classRepository.save(c);
			return description;
		}
		catch (NullPointerException e) {
			return "failure";
		}
	}
	
	// andrews general info
	@GetMapping(path = "/teacher/get/aInfo/{className}")
	public @ResponseBody List<String> getAInfo(@PathVariable("className") String className){
		List<String> list = new ArrayList<String>();
		List<Instant> dList = new ArrayList<Instant>();
		List<Integer> iList = new ArrayList<Integer>();
		List<Attendance> aList = new ArrayList<Attendance>();
		int actual, index;
		Class c;
		try {
			aList = attendanceRepository.findByClassName(className);
			c = classRepository.findByName(className);
			actual = c.users.size();
			if (actual != 0) actual--;
			for (int i = 0; i < aList.size(); i++) {
				if (dList.contains(aList.get(i).date) && aList.get(i).present) {
					index = dList.indexOf(aList.get(i).date);
					iList.add(index, iList.get(index) + 1);
				}
				else {
					dList.add(aList.get(i).date);
					iList.add(1);
				}
			}
			for (int j = 0; j < dList.size() && j < aList.size() && j < iList.size(); j++) {
				String temp = dList.get(j) + "";
				list.add("Date: " + temp + ", Students Present: "+ iList.get(j) + "/" + actual);
			}
		}
		catch (NullPointerException e) {
			return list;
		}
		return list;
	}
}
