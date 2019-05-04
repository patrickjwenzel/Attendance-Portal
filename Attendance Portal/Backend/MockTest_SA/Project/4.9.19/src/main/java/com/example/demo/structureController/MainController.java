package com.example.demo.structureController;

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
import com.example.demo.repository.SchoolRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.MessageRepository;

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
	public @ResponseBody String resetDatabase() {
		User andrew = new User("aholman@iastate.edu", "ABCabc123!", "student");
		User patrick = new User("pwenzel@iastate.edu", "ABCabc123!", "teacher");
		Class comS = new Class("ComS");
		Class math = new Class("Math");
		Class sof = new Class("SoftE");
		Class span = new Class("Spanish");
		School school = new School("Iowa State");
		
		andrew.classes.add(comS);
		andrew.classes.add(math);
		andrew.classes.add(sof);
		andrew.classes.add(span);
		
		patrick.classes.add(comS);
		patrick.classes.add(math);
		patrick.classes.add(sof);
		patrick.classes.add(span);
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		
		userRepository.save(andrew);
		userRepository.save(patrick);
		
		andrew.name = "Andrew";
		patrick.name = "Patrick";
		
		comS.users.add(andrew);
		comS.users.add(patrick);
		math.users.add(andrew);
		math.users.add(patrick);
		span.users.add(andrew);
		span.users.add(patrick);
		sof.users.add(andrew);
		sof.users.add(patrick);
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		
		userRepository.save(andrew);
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
		
		userRepository.save(andrew);
		userRepository.save(patrick);
		
		school.classes.add(math);
		school.classes.add(comS);
		school.classes.add(sof);
		school.classes.add(span);
		
		schoolRepository.save(school);
		
		comS.description = "Embedded C programming. Interrupt handling. Memory mapped I/O in the context of an application. Elementary embedded design flow/methodology. Timers, scheduling, resource allocation, optimization, and state based controllers.";
		math.description = "Solution methods for ordinary differential equations. First order equations, linear equations, constant coefficient equations. Eigenvalue methods for systems of first order linear equations.";
		sof.description = "A practical introduction to methods for managing software development. Process models, requirements analysis, structured and object-oriented design, coding, testing, maintenance, cost and schedule estimation, metrics.";
		span.description = "Introduction to the theory, methods, techniques, and problems of translation. Consideration of material from business, literature, and the social sciences. Taught in Spanish.";
		
		classRepository.save(comS);
		classRepository.save(math);
		classRepository.save(sof);
		classRepository.save(span);
		
		schoolRepository.save(school);
		userRepository.save(andrew);
		userRepository.save(patrick);
		
		Message failureM = new Message("failure");
		Message successM = new Message("success");
		School failureS = new School("failure");
		School successS = new School("school");
		User failureU = new User("failure", "ABCabc123!", "admin");
		User successU = new User("success", "ABCabc123!", "admin");
		Class failureC = new Class("failure");
		Class successC = new Class("success");
		Attendance failureA = new Attendance("failure");
		Attendance successA = new Attendance("success");
		messageRepository.save(failureM);
		messageRepository.save(successM);
		schoolRepository.save(failureS);
		schoolRepository.save(successS);
		userRepository.save(failureU);
		userRepository.save(successU);
		classRepository.save(failureC);
		classRepository.save(successC);
		attendanceRepository.save(failureA);
		attendanceRepository.save(successA);
		
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
}
