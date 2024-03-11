package org.example;
import static org.junit.Assert.*;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestGeneratedByChatGPT {

	private TimetableFactoryImpl factory;

	@org.junit.Before
	public void initFactory() {
		this.factory = new TimetableFactoryImpl();
	}

	@org.junit.Test
	public void testEmpty() {
		Timetable timetable = factory.empty();
		assertTrue(timetable.activities().isEmpty());
		assertTrue(timetable.days().isEmpty());
	}

	@org.junit.Test
	public void testSingle() {
		Timetable timetable = factory.single("Study", "Monday");
		assertEquals(1, timetable.getSingleData("Study", "Monday"));

		timetable = timetable.addHour("Study", "Monday");
		assertEquals(2, timetable.getSingleData("Study", "Monday"));

		Set<String> days = timetable.days();
		assertEquals(1, days.size());
		assertTrue(days.contains("Monday"));

		Set<String> activities = timetable.activities();
		assertEquals(1, activities.size());
		assertTrue(activities.contains("Study"));

		assertEquals(2, timetable.sums(activities, days));
	}

	@org.junit.Test
	public void testJoin() {
		Timetable timetable1 = factory.single("Study", "Monday").addHour("Study", "Monday");
		Timetable timetable2 = factory.single("Play", "Monday").addHour("Play", "Monday");

		Timetable joinedTimetable = factory.join(timetable1, timetable2);
		assertEquals(2, joinedTimetable.getSingleData("Study", "Monday"));
		assertEquals(2, joinedTimetable.getSingleData("Play", "Monday"));
	}

	@org.junit.Test
	public void testBounds() {
		Timetable timetable = factory.single("Study", "Monday").addHour("Study", "Monday");
		timetable = factory.cut(timetable, (activity, day) -> {
			if (activity.equals("Study") && day.equals("Monday")) {
				return 1; // Limiting to 1 hour for Monday Study
			}
			return 0; // No other limits
		});

		assertEquals(1, timetable.getSingleData("Study", "Monday"));
		assertEquals(0, timetable.getSingleData("Study", "Tuesday")); // No hours for Tuesday
	}
}