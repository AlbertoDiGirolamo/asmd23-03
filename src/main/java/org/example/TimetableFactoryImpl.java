package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class TimetableFactoryImpl implements TimetableFactory {

    @Override
    public Timetable empty() {
        return new TimetableImpl(new HashMap<>());
    }

    @Override
    public Timetable single(String activity, String day) {
        Map<Pair<String, String>, Integer> data = new HashMap<>();
        data.put(new Pair<>(activity, day), 1);
        return new TimetableImpl(data);
    }

    @Override
    public Timetable join(Timetable table1, Timetable table2) {
        Map<Pair<String, String>, Integer> newData = new HashMap<>(table1.getData());
        table2.getData().forEach((pair, hours) ->
                newData.merge(pair, hours, Integer::sum)
        );
        return new TimetableImpl(newData);
    }

    @Override
    public Timetable cut(Timetable table, BiFunction<String, String, Integer> bounds) {
        Map<Pair<String, String>, Integer> newData = new HashMap<>();
        table.getData().forEach((pair, hours) -> {
            String activity = pair.get1();
            String day = pair.get2();
            int limit = bounds.apply(activity, day);
            int newHours = Math.min(hours, limit);
            newData.put(pair, newHours);
        });
        return new TimetableImpl(newData);
    }

}
