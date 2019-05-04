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
		try {
			Class c = classRepository.findByName(className).get();
			return userRepository.findByTypeAndClassesContains("student", c);
		}
		catch (NullPointerException e) {
			List<User> temp = new ArrayList<User>();
			temp.add(userRepository.findByEmailAndType("failure", "admin").get());
			return temp;
		}
	}
	
	// create a class
	@PostMapping(path = "/teacher/post/create/class/{email}/{className}")
	public @ResponseBody Class createClass(@PathVariable("email") String email, @PathVariable("className") String className) {
		try {
			if (!userRepository.findByEmail(email).isPresent() || userRepository.findByEmail(email).get().type != "teacher" || className == null) return classRepository.findByName("failure").get();
			return classRepository.save(new Class(className, userRepository.findByEmail(email).get()));
		}
		catch (NullPointerException e) {
			return classRepository.findByName("failure").get();
		}
	}
	
	// start a class
	@PostMapping(path = "/teacher/post/start/{email}/{className}/{lat}/{lon}")
	public @ResponseBody String startClass(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("lat") float lat, @PathVariable("lon") float lon) {
		try {
			Class c = classRepository.findByName(className).get();
			List<User> students = userRepository.findByTypeAndClassesContains("student", c);
			List<User> used = new ArrayList<User>();
			Attendance a;
			Message msg;
			User temp;
			Instant date = DateHandeling.simplify(Calendar.getInstance());
			String cname = c.name;
			if (!attendanceRepository.findByClassNameAndDate(className, date).isEmpty()) {
				return "failure";
			}
			for (int i = 0; i < students.size(); i++) {
				temp = students.get(i);
				if (!used.contains(temp)) {
					a = new Attendance(temp.email, cname, date, false);
					attendanceRepository.save(a);
					msg = new Message(cname + " has started", date, email, cname);
					messageRepository.save(msg);
					temp.messages.add(msg);
					temp.attendance.add(a);
					used.add(temp);
					userRepository.save(temp);
				}
			}
			c.lat = lat;
			c.lon = lon;
			c.active = "true";
			classRepository.save(c);
			return "success";
		}
		catch (Exception e) {
			return "error";
		}
	}
	
	// stop a class
	@PostMapping(path = "/teacher/post/stop/{email}/{className}")
	public @ResponseBody List<User> stopClass(@PathVariable("email") String email, @PathVariable("className") String className) {
		List<User> list = new ArrayList<User>();
		try {
			//User user = userRepository.findByEmail(email).get();
			Class c = classRepository.findByName(className).get();
			List<User> students = new ArrayList<User>(); 
			students.addAll(userRepository.findByTypeAndClassesContains("student", c));
			User temp;
			Instant date;
			Message msg;
			for (int i = 0; i < students.size(); i++) {
				temp = students.get(i);
				date = DateHandeling.simplify(Calendar.getInstance());
				msg = new Message(c.name + " has ended", date, email, c.name);
				messageRepository.save(msg);
				temp.messages.add(msg);
				userRepository.save(temp);
				if (attendanceRepository.findByUserEmailAndClassNameAndDateAndPresent(temp.email, className, date, true).isPresent()) {
					list.add(temp);
				}
			}
			c.lat = 0;
			c.lon = 0;
			c.active = "false";
			classRepository.save(c);
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			list.add(userRepository.findByEmailAndType("failure", "admin").get());
			return list;
		}
	}
	
	// change a students attendance
	@PostMapping(path = "/teacher/post/swap/{studentEmail}/{className}/{date}")
	public @ResponseBody Attendance changeAttendance(@PathVariable("studentEmail") String studentEmail, @PathVariable("className") String className, @PathVariable("date") String date) {
		try {
			User student = userRepository.findByEmail(studentEmail).get();
			Class c = classRepository.findByName(className).get();
			Instant dateA = DateHandeling.shallowConvert(date);
			Attendance a = attendanceRepository.findByUserEmailAndClassNameAndDate(student.email, c.name, dateA).get();
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
			Class c = classRepository.findByName(className).get();
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
		List<String> list = new ArrayList<String>();				// output
		List<Instant> dList = new ArrayList<Instant>();			// dates
		List<Integer> iList = new ArrayList<Integer>();			// number of students present
		List<Attendance> aList = new ArrayList<Attendance>();		// list of all attendance
		List<User> users = new ArrayList<User>();				// list of users in class
		int index, size = 0;
		Class c;
		try {
			aList.addAll(attendanceRepository.findByClassName(className));
			c = classRepository.findByName(className).get();
			users.addAll(userRepository.findByClassesContains(c));
			for (int i = 0; i < aList.size(); i++) {
				Instant date = aList.get(i).date;
				if (dList.size() == 0) {
					dList.add(date);
					if (aList.get(i).present == true) iList.add(1);
					else iList.add(0);
				}
				else if (!dList.contains(date)) {
					dList.add(date);
					if (aList.get(i).present == true) iList.add(1);
					else iList.add(0);
				}
				else if (dList.contains(date)) {
					index = dList.indexOf(aList.get(i).date);
					if (aList.get(i).present == true) iList.set(index, iList.get(index) + 1);
				}
			}
			for (int k = 0; k < users.size(); k++) {
				if (users.get(k).type.equals("student")) size++;
			}
			for (int j = 0; j < dList.size() && j < aList.size() && j < iList.size(); j++) {
				String temp = dList.get(j) + "";
				list.add("Date: " + temp + ", Students Present: "+ iList.get(j) + "/" + size);
			}
		}
		catch (Exception e) {
			list.clear();
			list.add("ERROR HERE" + e.getMessage());
			return list;
		}
		return list;
	}
}
