package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;
import com.example.demo.model.Class;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findById(Integer id);
	Optional<User> findByEmail(String email);
	Optional<User> findByName(String name);
	Optional<User> findByEmailAndPassword(String email, String password);
	// for success / failure
	Optional<User> findByEmailAndType(String email, String Type);
	List<User> findByClassesContains(Class c);
	List<User> findByTypeAndClassesContains(String type, Class c);
}
