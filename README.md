# Task 1: CODE GENERATION

## LLM: ChatGPT

### First request:
* For the first step i ask to ChatGPT this request:
>Using the following file .java : [TimetableImpl.java, TimetableFactory.java, Pair.java], complete the following code for to obtain all correct test: [Test.java]


* The answer is:
>To implement the TimetableFactory interface, you need to create a class that provides implementations for each of its methods.
>Below is the implementation for the TimetableFactory interface: [TimetableFactoryImpl.java]


> And here is the implementation for the Timetable interface: [TimetableImpl.java]

> These implementations should provide the required functionality for the tests in your Test class. You can uncomment the initFactory method in the Test class and use these implementations to run your tests.


* For the second step i ask to ChatGPT this request:
> how to fix the following error:
```
error: cannot find symbol
        Map<Pair<String, String>, Integer> newData = new HashMap<>(table1.getData());
                                                                         ^
  symbol:   method getData()
  location: variable table1 of type Timetable
```

* The answer is:
> To resolve the error cannot find symbol in your code, you need to ensure that the Timetable interface provides a method called getData() which returns the data stored in the timetable. Let's add this method to the Timetable interface:

```
package org.example;

import java.util.Map;
import java.util.Set;

public interface Timetable {
    
    Timetable addHour(String activity, String day);
    
    Set<String> activities();

    Set<String> days();

    int getSingleData(String activity, String day);

    int sums(Set<String> activities, Set<String> days);
    
    // Add this method to the interface
    Map<Pair<String, String>, Integer> getData();
}
```

> Now, implement this method in your TimetableImpl class:
```
@Override
public Map<Pair<String, String>, Integer> getData() {
    return data;
}
```

> With these changes, the getData() method will be available in your Timetable interface, and you can use it to retrieve the data stored in the timetable objects. This should resolve the compilation error you encountered.


#### With LLM ChatGPT and uploading all the codes with all specifics for obtain the solution, it response with a correct version with few requests

# Task 2: TESTING

## LLM: ChatGPT

* From an existing OOP exam I removed tests already implemented and I used a LLM (in this case ChatGPT) for generate tests.
Each test generated must be similar to the original test, so I asked to LLM to generate tests with particular characteristics.

> From the linked Files write the follows junit test. All tests must be correct:
```
        @org.junit.Before
	public void initFactory() {
		this.factory = new TimetableFactoryImpl();
	}

	@org.junit.Test
	public void testEmpty() {
                // Checks the behavior of an empty timetable and adding an hour of activity on a specific day.
	}

	@org.junit.Test
	public void testSingle() {
                // Tests creating a timetable with an hour of activity on a specific day and adds additional hours, verifying activity sums per day and total.
	}

	@org.junit.Test
	public void testJoin() {
		// Joins two different timetable tables and verifies that the hours are summed correctly.
	}

	@org.junit.Test
	public void testBounds() {
                // Tests the cut function of the factory, which should remove hours from the timetable based on a given condition, and verifies if the timetable is cut correctly.
	}
 ```
* LLM reply with each test correctly implemented, in fact all of them pass when you try to run.
```
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
```
* Is a good practice in this case ask to LLM to grow each tests ad add more controls about how system work, for example add more Assertion for check other scenarios with different input values.

> Modify testEmpty and for test more scenarios: check table.activities, table.days, table.getSingleData and table.sums
```
    @Test
    public void testEmpty() {
    // Create an empty timetable
    Timetable timetable = factory.empty();

    // Test if activities and days are empty
    assertTrue(timetable.activities().isEmpty());
    assertTrue(timetable.days().isEmpty());

    // Test getSingleData for a non-existing activity and day
    assertEquals(0, timetable.getSingleData("Study", "Monday"));

    // Test sums for empty activities and days
    assertEquals(0, timetable.sums(Set.of(), Set.of()));
}

```
> With these additions, we cover the following scenarios:
Ensuring activities and days are empty in an empty timetable.
Testing getSingleData() for a non-existing activity and day, which should return 0.
Testing sums() with empty sets of activities and days, which should also return 0.

> Modify testJoin and add test more scenarios
```
@Test
public void testJoin() {
	// Create two timetables with overlapping and non-overlapping activities
	Timetable timetable1 = factory.single("Study", "Monday").addHour("Study", "Monday").addHour("Study", "Tuesday");
	Timetable timetable2 = factory.single("Play", "Monday").addHour("Play", "Monday").addHour("Play", "Wednesday");

	// Join the two timetables
	Timetable joinedTimetable = factory.join(timetable1, timetable2);

	// Test if the joined timetable contains correct activity hours
	assertEquals(2, joinedTimetable.getSingleData("Study", "Monday"));
	assertEquals(1, joinedTimetable.getSingleData("Study", "Tuesday"));
	assertEquals(2, joinedTimetable.getSingleData("Play", "Monday"));
	assertEquals(1, joinedTimetable.getSingleData("Play", "Wednesday"));

	// Test activities and days after joining
	assertEquals(2, joinedTimetable.activities().size());
	assertEquals(3, joinedTimetable.days().size());

	// Test sums for both activities and days
	assertEquals(6, joinedTimetable.sums(joinedTimetable.activities(), joinedTimetable.days()));

	// Test joining with an empty timetable
	Timetable emptyTimetable = factory.empty();
	joinedTimetable = factory.join(joinedTimetable, emptyTimetable);
	assertEquals(6, joinedTimetable.sums(joinedTimetable.activities(), joinedTimetable.days()));
}

```
