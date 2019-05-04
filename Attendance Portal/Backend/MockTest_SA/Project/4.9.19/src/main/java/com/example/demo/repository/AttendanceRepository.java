package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Attendance;
import com.example.demo.model.Class;
import com.example.demo.model.User;

@Repository
public interface AttendanceRepository extends CrudRepository<Attendance, Integer>{
	
	Optional<Attendance> findById(Integer id);
	Optional<Attendance> findByUserAndClasses(User user, Class classes);
	Optional<Attendance> findByUserAndClassesAndDate(User user, Class classes, String date);
	Optional<Attendance> findByDate(String date);
}
