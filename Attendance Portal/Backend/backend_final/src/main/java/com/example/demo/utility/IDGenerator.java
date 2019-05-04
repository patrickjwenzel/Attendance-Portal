package com.example.demo.utility;

import java.util.Random;

public class IDGenerator {
	
	public static Integer classId() {
		Integer cId = 0;
		while (cId < 10000000) {
			cId = new Random(System.currentTimeMillis()).nextInt(100000000);
		}
		return cId;
	}
}
