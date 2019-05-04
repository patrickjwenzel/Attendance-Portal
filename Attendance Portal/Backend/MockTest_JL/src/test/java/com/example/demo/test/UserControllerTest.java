package com.example.demo.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.example.demo.controller.structure.UserController;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

public class UserControllerTest {

	@InjectMocks
	UserController userController;
	
	@Mock
	UserRepository userRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/*
	 * This tests the getUserByEmailAndPassword method in the userController.
	 * It also shares a name with the method to get these values from the database in userRepository.
	 * This test is very important due to the fact that it is used whenever a user signs in.
	 */
	
	@SuppressWarnings("unchecked")
	@Test
	public void getUserByEmailAndPassword() {
		when(((OngoingStubbing<User>) userRepository.findByEmailAndPassword("jtloftus@iastate.edu", "test")).thenReturn(new User("jtloftus@iastate.edu", "test", "student")));
		User user = userController.getUserByEmailAndPassword("jtloftus@iastate.edu", "test");
		
		assertEquals("jtloftus@iastate.edu", user.email);
		assertEquals("test", user.password);
		assertEquals("student", user.type);
	}
}
