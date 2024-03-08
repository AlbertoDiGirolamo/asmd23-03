package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TimetableImpl implements Timetable {

    private final Map<Pair<String, String>, Integer> data;

    public TimetableImpl(Map<Pair<String, String>, Integer> data) {
        this.data = new HashMap<>(data);
    }

    @Override
    public Timetable addHour(String activity, String day) {
        Map<Pair<String, String>, Integer> newData = new HashMap<>(data);
        Pair<String, String> key = new Pair<>(activity, day);
        newData.put(key, newData.getOrDefault(key, 0) + 1);
        return new TimetableImpl(newData);
    }

    @Override
    public Set<String> activities() {
        Set<String> activities = new HashSet<>();
        for (Pair<String, String> pair : data.keySet()) {
            activities.add(pair.get1());
        }
        return activities;
    }

    @Override
    public Set<String> days() {
        Set<String> days = new HashSet<>();
        for (Pair<String, String> pair : data.keySet()) {
            days.add(pair.get2());
        }
        return days;
    }

    @Override
    public int getSingleData(String activity, String day) {
        return data.getOrDefault(new Pair<>(activity, day), 0);
    }

    @Override
    public int sums(Set<String> activities, Set<String> days) {
        int sum = 0;
        for (String activity : activities) {
            for (String day : days) {
                sum += data.getOrDefault(new Pair<>(activity, day), 0);
            }
        }
        return sum;
    }

    public Map<Pair<String, String>, Integer> getData() {
        return data;
    }

}
