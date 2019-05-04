package com.example.demo.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.example.demo.controller.structure.AttendanceController;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;

public class AttendanceControllerTest {

	@InjectMocks
	AttendanceController attendanceController;
	
	@Mock
	AttendanceRepository attendanceRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/*
	 * This tests the getAttendanceByUserEmailAndClassNameAndDate method in attendanceController.
	 * It also shares a name with the method to get these values from the database in attendanceRepository.
	 */
	
	@SuppressWarnings("unchecked")
	@Test
	public void getAttendanceByUserEmailAndClassNameAndDateTest() {
		Instant date = Calendar.getInstance().toInstant();
		when(((OngoingStubbing<Attendance>) attendanceRepository.findByUserEmailAndClassNameAndDate("jtloftus@iastate.edu", "test", date)).thenReturn(new Attendance("jtloftus@iastate.edu", "test", date, true)));
		Attendance a = attendanceController.getAttendanceByUserEmailAndClassNameAndDate("jtloftus@iastate.edu", "test", Calendar.getInstance().toInstant());
		
		assertEquals("jtloftus@iastate.edu", a.userEmail);
		assertEquals("test", a.className);
		assertEquals(true, a.present);
		assertEquals(date, a.date);
	}
}
