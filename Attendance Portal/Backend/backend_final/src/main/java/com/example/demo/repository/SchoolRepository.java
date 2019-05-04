package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.School;

@Repository
public interface SchoolRepository extends CrudRepository<School, Integer> {
	
	Optional<School> findById(Integer id);
	// for success / failure
	Optional<School> findBySName(String sName);
}
