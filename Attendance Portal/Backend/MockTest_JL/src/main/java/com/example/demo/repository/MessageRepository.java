package com.example.demo.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{

	Optional<Message> findById(Integer id);
	List<Message> findByUserEmail(String userEmail);
	List<Message> findByClassName(String className);
	List<Message> findByDate(Instant date);
	// for failure / success
	List<Message> findByUserEmailAndClassName(String string, String string2);
}
