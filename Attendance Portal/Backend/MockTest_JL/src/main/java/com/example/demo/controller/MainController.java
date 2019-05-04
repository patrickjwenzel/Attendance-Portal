package com.example.demo.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Attendance;
import com.example.demo.model.Class;
import com.example.demo.model.Message;
import com.example.demo.model.School;
import com.example.demo.model.User;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.SchoolRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.DateHandeling;

@RestController
public class MainController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	@Autowired
	SchoolRepository schoolRepository;
	
	@Autowired
	MessageRepository messageRepository;
	
	// add basic users
	@PostMapping(path = "/reset")
	public @ResponseBody String resetDatabase() throws InterruptedException {
		User andrew = new User("aholman@iastate.edu", "ABCabc123!", "student");
		User bndrew = new User("bholman@iastate.edu", "ABCabc123!", "teacher");
		User patrick = new User("pwenzel@iastate.edu", "ABCabc123!", "teacher");
		Class comS = new Class("ComS");
		Thread.sleep(10);
		Class math = new Class("Math");
		Thread.sleep(10);
		Class sof = new Class("SoftE");
		Thread.sleep(10);
		Class span = new Class("Spanish");
		School school = new School("Iowa State");
		
		andrew.classes.add(comS);
		andrew.classes.add(math);
		andrew.classes.add(sof);
		andrew.classes.add(span);
		
		bndrew.classes.add(comS);
		bndrew.classes.add(math);
		bndrew.classes.add(sof);
		bndrew.classes.add(span);
		
		patrick.classes.add(comS);
		patrick.classes.add(math);
		patrick.classes.add(sof);
		patrick.classes.add(span);
		
		andrew.name = "Andrew";
		bndrew.name = "Andrew";
		patrick.name = "Patrick";
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		userRepository.save(andrew);
		userRepository.save(bndrew);
		userRepository.save(patrick);
		
		comS.cInfo = "MWF ! 5-6pm ! BuildingF ! 712";
		math.cInfo = "WF ! 1-2pm ! BuildingF ! 345";
		sof.cInfo = "MF ! 8-9am ! BuildingF ! 143";
		span.cInfo = "MWF ! 8-10am ! BuildingF ! 214";
		
		comS.rInfo = "TF ! 10-11am ! BuildingA ! 894";
		math.rInfo = "TR ! 4-5pm ! BuildingA ! 315";
		sof.rInfo = "TW ! 2-3pm ! BuildingA ! 315";
		span.rInfo = "TR ! 1-2pm ! BuildingA ! 151";
		
		comS.lInfo = "TWR ! 12-1pm ! BuildingL ! 802";
		math.lInfo = "R ! 3-4pm ! BuildingL ! 345";
		sof.lInfo = "TR ! 1-2pm ! BuildingL ! 1505";
		span.lInfo = "T ! 10-11am ! BuildingL ! 117";
		
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		
		comS.users.add(andrew);
		comS.users.add(bndrew);
		comS.users.add(patrick);
		math.users.add(andrew);
		math.users.add(bndrew);
		math.users.add(patrick);
		span.users.add(andrew);
		span.users.add(bndrew);
		span.users.add(patrick);
		sof.users.add(andrew);
		sof.users.add(bndrew);
		sof.users.add(patrick);
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		userRepository.save(andrew);
		userRepository.save(bndrew);
		userRepository.save(patrick);
		schoolRepository.save(school);
		
		math.school = school;
		comS.school = school;
		sof.school = school;
		span.school = school;
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		
		school.classes.add(math);
		school.classes.add(comS);
		school.classes.add(sof);
		school.classes.add(span);
		
		schoolRepository.save(school);
		
		String d1 = "Embedded C programming. Interrupt handling. Memory mapped I/O in the context of an application. Elementary embedded design flow/methodology. Timers, scheduling, resource allocation, optimization, and state based controllers.";
		String d2 = "Solution methods for ordinary differential equations. First order equations, linear equations, constant coefficient equations. Eigenvalue methods for systems of first order linear equations.";
		String d3 = "A practical introduction to methods for managing software development. Process models, requirements analysis, structured and object-oriented design, coding, testing, maintenance, cost and schedule estimation, metrics.";
		String d4 = "Introduction to the theory, methods, techniques, and problems of translation. Consideration of material from business, literature, and the social sciences. Taught in Spanish.";
		
		// subject to change
		int strLen = 35;
		comS.description = d1.substring(0, strLen); 
		math.description = d2.substring(0, strLen); 
		sof.description = d3.substring(0, strLen);
		span.description = d4.substring(0, strLen);
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		schoolRepository.save(school);
		userRepository.save(andrew);
		userRepository.save(bndrew);
		userRepository.save(patrick);
		
		messageRepository.save(new Message("failure"));
		messageRepository.save(new Message("success"));
		schoolRepository.save(new School("failure"));
		schoolRepository.save(new School("school"));
		userRepository.save(new User("failure", "ABCabc123!", "admin"));
		userRepository.save(new User("success", "ABCabc123!", "admin"));
		classRepository.save(new Class("failure"));
		classRepository.save(new Class("success"));
		attendanceRepository.save(new Attendance("failure", "failure"));
		attendanceRepository.save(new Attendance("success", "success"));
		
		return "complete";
	}
	
	// clears database
	@PostMapping(path = "/clear")
	public @ResponseBody String clearDatabase() {
		if (userRepository.findAll() != null) userRepository.deleteAll();
		if (classRepository.findAll() != null) classRepository.deleteAll();
		if (attendanceRepository.findAll() != null) attendanceRepository.deleteAll();
		if (schoolRepository.findAll() != null) schoolRepository.deleteAll();
		return "complete";
	}
	
	// randomize
	@PostMapping(path = "/random")
	public @ResponseBody String randomizeDatabase() {
		Attendance a, b, d;
		User user;
		String[] userEmails = {"fredr", "jeffu", "stanj", "keithy", "jakes", "nateh", "charliem", "kennedya", "rayj", "susanc"};
		String[] userNames = {"Fred", "Jeff", "Stan", "Keith", "Jake", "Nate", "Charlie", "Kennedy", "Ray", "Susan"};
		Instant date1 = DateHandeling.convert("02.01.2019");
		Instant date2 = DateHandeling.convert("02.03.2019");
		Instant date3 = DateHandeling.convert("02.05.2019");
		try {
			for (int i = 0; i < 10; i++) {
				String email = userEmails[i] + "@edu.com";
				String name = userNames[i];
				Class c = classRepository.findByName("Spanish");
				a = new Attendance(email, "Spanish", date1, true);
				b = new Attendance(email, "Spanish", date2, true);
				d = new Attendance(email, "Spanish", date3, true);
				attendanceRepository.save(a);
				attendanceRepository.save(b);
				attendanceRepository.save(d);
				user = new User(email, "a", "student");
				user.name = name;
				user.attendance.add(a);
				user.attendance.add(b);
				user.attendance.add(d);
				user.classes.add(c);
				userRepository.save(user);
				c.users.add(user);
				classRepository.save(c);
			}
		}
		catch (NullPointerException e) {
			return "failure";
		}
		return "complete";
	}
}
