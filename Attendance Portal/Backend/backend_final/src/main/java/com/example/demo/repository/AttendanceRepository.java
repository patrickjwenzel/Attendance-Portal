package com.example.demo.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Attendance;

@Repository
public interface AttendanceRepository extends CrudRepository<Attendance, Integer>{
	
	Optional<Attendance> findById(Integer id);
	Optional<Attendance> findByUserEmailAndClassNameAndDate(String email, String className, Instant date);
	Optional<Attendance> findByUserEmailAndClassNameAndDateAndPresent(String email, String className, Instant date, boolean present);
	List<Attendance> findByDate(Instant date);
	List<Attendance> findByUserEmail(String email);
	List<Attendance> findByClassName(String className);
	List<Attendance> findByClassNameAndDate(String className, Instant date);
	// for success / failure
	List<Attendance> findByUserEmailAndClassName(String email, String className);
}
