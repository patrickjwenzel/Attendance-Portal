package com.example.demo.functionController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	/*
	 * Need: 
	 */
	
	// create a class
	@PostMapping(path = "/teacher/post/create/class/{email}/{className}")
	public @ResponseBody Class createClass(@PathVariable("email") String email, @PathVariable("className") String className) {
		if (!userRepository.findByEmail(email).isPresent() || userRepository.findByEmail(email).get().type != "teacher" || className == null) return classRepository.findByName("failure").get();
		return classRepository.save(new Class(className, userRepository.findByEmail(email).get()));
	}
	
	// create a class complex
		@PostMapping(path = "/teacher/post/create/class/{email}/{className}/{section}")
		public @ResponseBody Class createClass(@PathVariable("email") String email, @PathVariable("className") String className, @PathVariable("section") Integer section) {
			if (!userRepository.findByEmail(email).isPresent() || email == null || className == null || section == null || userRepository.findByEmail(email).get().type != "teacher") return classRepository.findByName("failure").get();
			return classRepository.save(new Class(className, section, userRepository.findByEmail(email).get()));
		}
	
	// start a class
	@PostMapping(path = "/teacher/post/start/{email}/{classId}/{lat}/{lon}/{date}")
	public @ResponseBody String startClass(@PathVariable("email") String email, @PathVariable("classId") Integer classId, @PathVariable("lat") float lat, @PathVariable("lon") float lon, @PathVariable("date") String date) {
		if (date == null || email == null || classId == null|| !classRepository.findById(classId).isPresent() || !userRepository.findByEmail(email).isPresent() || classRepository.findById(classId).get().users == null || !userRepository.findByEmail(email).get().type.equals("teacher") || 
				!userRepository.findByEmail(email).get().classes.contains(classRepository.findById(classId).get()) || classRepository.findById(classId).get().active == true) return "failure to start";
		User user = userRepository.findByEmail(email).get();
		Class c = classRepository.findById(classId).get();
		if (date.charAt(1) == '.') {date = "0" + date;}
		List<User> students = c.users;
		for (int i = 0; i < students.size(); i++) {
			User temp = students.get(i);
			if (temp.type.equals("student")) {
				classRepository.save(c);
				Attendance a = new Attendance(temp, c, date, false);
				classRepository.save(c);
				Message msg = new Message(c.name + " has started", date, user, c);
				classRepository.save(c);
				messageRepository.save(msg);
				attendanceRepository.save(a);
				userRepository.save(temp);
				temp.attendance.add(a);
				messageRepository.save(msg);
				attendanceRepository.save(a);
				userRepository.save(temp);
				temp.messages.add(msg);
				messageRepository.save(msg);
				attendanceRepository.save(a);
				userRepository.save(temp);
				msg.user = temp;
				messageRepository.save(msg);
				attendanceRepository.save(a);
				userRepository.save(temp);
			}
		}
		c.lat = lat;
		c.lon = lon;
		c.active = true;
		classRepository.save(c);
		return "Class " + c.name + " has been started!";
	}
	
	/*
	 * possible error in save order on below and next
	 */
	// stop a class
	@PostMapping(path = "/teacher/post/stop/{email}/{classId}/{date}")
	public @ResponseBody String stopClass(@PathVariable("email") String email, @PathVariable("classId") Integer classId, @PathVariable("date") String date) {
		if (date.charAt(1) == '.') {date = "0" + date;}
		if (date == null || email == null || classId == null|| !classRepository.findById(classId).isPresent() || !userRepository.findByEmail(email).isPresent() || classRepository.findById(classId).get().users == null || !userRepository.findByEmail(email).get().type.equals("teacher") || 
				!userRepository.findByEmail(email).get().classes.contains(classRepository.findById(classId).get()) || classRepository.findById(classId).get().active == true) return "failure to start";
		User user = userRepository.findByEmail(email).get();
		Class c = classRepository.findById(classId).get();
		List<User> students = c.users;
		for (int i = 0; i < students.size(); i++) {
			User temp = students.get(i);
			if (temp.type.equals("student")) {
				classRepository.save(c);
				Message msg = new Message(c.name + " has ended", date, user, c);
				classRepository.save(c);
				messageRepository.save(msg);
				userRepository.save(temp);
				messageRepository.save(msg);
				userRepository.save(temp);
				
				temp.messages.add(msg);
				
				messageRepository.save(msg);
				userRepository.save(temp);
				
				msg.user = temp;
				
				messageRepository.save(msg);
				userRepository.save(temp);
			}
		}
		c.lat = 0;
		c.lon = 0;
		c.active = false;
		classRepository.save(c);
		return "Class " + c.name + " has been stopped!";
	}
	
	// change a students attendance
	@PostMapping(path = "/teacher/post/swap/{teacherEmail}/{studentEmail}/{classId}/{date}")
	public @ResponseBody Attendance changeAttendance(@PathVariable("teacherEmail") String teacherEmail, @PathVariable("studentEmail") String studentEmail, @PathVariable("classId") Integer classId, @PathVariable("date") String date) {
		if (teacherEmail == null || studentEmail == null || classId == null || date == null || userRepository.findByEmail(teacherEmail).get().type.equals("teacher") || !userRepository.findByEmail(studentEmail).get().type.equals("student") || !userRepository.findByEmail(teacherEmail).get().classes.contains(classRepository.findById(classId).get()) || 
				!userRepository.findByEmail(studentEmail).get().classes.contains(classRepository.findById(classId).get())) return attendanceRepository.findByDate("failure").get();
		User student = userRepository.findByEmail(studentEmail).get();
		Class c = classRepository.findById(classId).get();
		Attendance a = attendanceRepository.findByUserAndClassesAndDate(student, c, date).get();
		if (a.present == false) a.present = true;
		else a.present = false;
		attendanceRepository.save(a);
		userRepository.save(student);
		classRepository.save(c);
		return a;
	}
	
	// add and change class description
	@PostMapping(path = "/teacher/post/des/add/{email}/{classId}/{description}")
	public @ResponseBody String addDescription(@PathVariable("email") String email, @PathVariable("classId") Integer classId, @PathVariable("description") String description) throws Exception {
		User user = userRepository.findByEmail(email).get();
		Class c = classRepository.findById(classId).get();
		if (user == null || !user.type.equals("teacher") || c == null || !c.users.contains(user) || description.length() > 229) throw new Exception("Invalid command!");
		c.description = description;
		classRepository.save(c);
		return description;
	}
}
