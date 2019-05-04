package com.example.demo.utility;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateHandeling {

	public static Instant convert(String date) {
		Calendar cal = Calendar.getInstance();
		List<Integer> list = new ArrayList<Integer>();
		Integer month = 0, day = 0, year = 0;
		for (int i = 0; i < date.length(); i++) {
			if (date.charAt(i) == '.') list.add(i);
		}
		try {
			month = Integer.parseInt(date.substring(0, list.get(0)));
			day = Integer.parseInt(date.substring(list.get(0) + 1, list.get(1)));
			year = Integer.parseInt(date.substring(list.get(1) + 1)) - 1;
		}
		catch (Exception e) {
			System.out.println("Error in DateHandeling.convert");
		}
		cal.clear();
		cal.set(year, month, day);
		return cal.toInstant();
	}
	
	public static Instant shallowConvert(String date) {
		Calendar cal = Calendar.getInstance();
		List<Integer> list = new ArrayList<Integer>();
		Integer month = 0, day = 0, year = 0;
		for (int i = 0; i < date.length(); i++) {
			if (date.charAt(i) == '-') list.add(i);
		}
		try {
			year = Integer.parseInt(date.substring(0, list.get(0)));
			month = Integer.parseInt(date.substring(list.get(0) + 1, list.get(1))) - 1;
			day = Integer.parseInt(date.substring(list.get(1) + 1, date.length() - 10));
		}
		catch (Exception e) {
			System.out.println("Error in DateHandeling.shallowConvert");
		}
		cal.clear();
		cal.set(year, month, day);
		return cal.toInstant();
	}
	
	@SuppressWarnings("static-access")
	public static Instant simplify(Calendar date) {
		Integer month = date.get(date.MONTH);
		Integer day = date.get(date.DATE);
		Integer year = date.get(date.YEAR);
		date.clear();
		date.set(year, month, day);
		return date.toInstant();
	}
}
