package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.model.Class;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{

	Optional<Message> findById(Integer id);
	List<Message> findByUser(User user);
	List<Message> findByClasses(Class c);
	List<Message> findByDate(String date);
}
