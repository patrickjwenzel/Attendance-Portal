package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Class;

@Repository
public interface ClassRepository extends CrudRepository<Class, Integer>{
	
	Optional<Class> findById(Integer id);
	// For success / failure
	Optional<Class> findByName(String name);
}
