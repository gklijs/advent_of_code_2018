package com.gklijs.adventofcode.day25;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Day25 {

    private Day25() {
        //prevent instantiation
    }

    public static Single<String> c(Flowable<String> input) {
        return input
            .reduce(new ArrayList<>(), Day25::reduce)
            .map(Day25::constellations)
            .map(x -> Integer.toString(x.size()));
    }

    public static Single<String> f(Flowable<String> input) {
        return input
            .last("bla")
            .map(x -> "Triggered the underflow");
    }

    private static List<int[]> reduce(List<int[]> result, String input) {
        result.add(toPoint(input));
        return result;
    }

    private static void handle(int[] item, List<Set<int[]>> result) {
        for (Set<int[]> items : result) {
            if (belongsTo(item, items)) {
                items.add(item);
                return;
            }
        }
        Set<int[]> extraSet = new HashSet<>();
        extraSet.add(item);
        result.add(extraSet);
    }

    private static boolean overlap(Set<int[]> first, Set<int[]> second) {
        for (int[] item : first) {
            if (belongsTo(item, second)) {
                return true;
            }
        }
        return false;
    }

    private static void combine(List<Set<int[]>> result) {
        List<Set<int[]>> remove = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                if (!remove.contains(result.get(j)) && overlap(result.get(i), result.get(j))) {
                    result.get(i).addAll(result.get(j));
                    remove.add(result.get(j));
                }
            }
        }
        result.removeAll(remove);
    }

    private static List<Set<int[]>> constellations(List<int[]> input) {
        List<Set<int[]>> result = new ArrayList<>();
        for (int[] item : input) {
            handle(item, result);
        }
        int count = 0;
        while (result.size() != count) {
            count = result.size();
            combine(result);
        }
        return result;
    }

    private static int[] toPoint(String input) {
        int[] result = new int[4];
        StringBuilder builder = new StringBuilder();
        int counter = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c) || c == '-') {
                builder.append(c);
            } else if (builder.length() != 0) {
                result[counter] = Integer.parseInt(builder.toString());
                builder.setLength(0);
                counter++;
            }
        }
        if (builder.length() != 0) {
            result[counter] = Integer.parseInt(builder.toString());
        }
        return result;
    }

    private static boolean belongsTo(int[] item, Set<int[]> items) {
        for (int[] otherItem : items) {
            int dist = Math.abs(item[0] - otherItem[0]) + Math.abs(item[1] - otherItem[1]) + Math.abs(item[2] - otherItem[2]) + Math.abs(item[3] - otherItem[3]);
            if (dist <= 3) {
                return true;
            }
        }
        return false;
    }
}
