package com.example.demo.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.example.demo.controller.structure.ClassController;
import com.example.demo.model.Class;
import com.example.demo.repository.ClassRepository;

public class ClassControllerTest {

	@InjectMocks
	ClassController classController;
	
	@Mock
	ClassRepository classRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/*
	 * This tests the getClassByName method in the classController.
	 * It also shares a name with the method to get these values from the database in classRepository.
	 */
	
	@SuppressWarnings("unchecked")
	@Test
	public void getClassByName() {
		when(((OngoingStubbing<Class>) classRepository.findByName("test")).thenReturn(new Class("test")));
		Class c = classController.getClassByName("test");
		
		assertEquals("test", c.name);
		assertEquals(false, c.active);
		assertEquals(true, c.classId > 10000);
	}
}
