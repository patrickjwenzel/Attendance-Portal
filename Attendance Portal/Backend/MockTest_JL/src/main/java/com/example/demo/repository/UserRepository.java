package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findById(Integer id);
	Optional<User> findByEmail(String email);
	Optional<User> findByName(String name);
	User findByEmailAndPassword(String email, String password);
	// for success / failure
	Optional<User> findByEmailAndType(String email, String Type);
}
