package com.gklijs.adventofcode.day22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.gklijs.adventofcode.utils.Pair;
import com.gklijs.adventofcode.utils.Triple;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class Day22 {

    private Day22() {
        //prevent instantiation
    }

    public static Single<String> riskOfArea(Flowable<String> input) {
        return input
            .reduce(new Triple<>(null, null, null), Day22::reduce)
            .map(i -> map(i, 0))
            .map(Day22::total);
    }

    public static Single<String> rescue(Flowable<String> input) {
        return input
            .reduce(new Triple<>(null, null, null), Day22::reduce)
            .map(i -> new Pair<>(map(i, (i.getSecond() + i.getThird()) / 10), i))
            .map(Day22::rescue);
    }

    private static Triple<Integer, Integer, Integer> reduce(Triple<Integer, Integer, Integer> result, String input) {
        StringBuilder builder = new StringBuilder();
        if (input.startsWith("depth:")) {
            for (char c : input.toCharArray()) {
                if (Character.isDigit(c)) {
                    builder.append(c);
                }
            }
            result.setFirst(Integer.parseInt(builder.toString()));
        } else if (input.startsWith("target")) {
            for (char c : input.toCharArray()) {
                if (Character.isDigit(c)) {
                    builder.append(c);
                } else if (c == ',') {
                    result.setSecond(Integer.parseInt(builder.toString()));
                    builder.setLength(0);
                }
            }
            result.setThird(Integer.parseInt(builder.toString()));
        }
        return result;
    }

    private static Optional<Integer> safeGet(int[] pastGeoIndex, int index) {
        if (index < 0 || index >= pastGeoIndex.length) {
            return Optional.empty();
        } else {
            return Optional.of(pastGeoIndex[index]);
        }
    }

    private static int getGeoIndex(int x, int y, Optional<Integer> past1, Optional<Integer> past2) {
        return past1.map(integer1 -> past2.map(integer -> integer1 * integer).orElseGet(() -> x * 16807)).orElseGet(() -> y * 48271);
    }

    private static int[][] map(Triple<Integer, Integer, Integer> input, int additionalSpace) {
        int[][] map = new int[input.getThird() + 1 + additionalSpace][input.getSecond() + 1 + additionalSpace];
        int[] pastErosionLevel = new int[input.getSecond() + 1 + additionalSpace];
        for (int i = 0; i < input.getSecond() + input.getThird() + 1 + (2 * additionalSpace); i++) {
            int[] newErosionLevel = pastErosionLevel.clone();
            for (int x = 0; x <= input.getSecond() + additionalSpace; x++) {
                int y = i - x;
                if (y < 0 || y > input.getThird() + additionalSpace) {
                    continue;
                }
                int geoIndex = getGeoIndex(x, y, safeGet(pastErosionLevel, x - 1), y == 0 ? Optional.empty() : safeGet(pastErosionLevel, x));
                if (x == input.getSecond() && y == input.getThird()) {
                    geoIndex = 0;
                }
                int erosionLevel = (geoIndex + input.getFirst()) % 20183;
                newErosionLevel[x] = erosionLevel;
                map[y][x] = erosionLevel % 3;
            }
            pastErosionLevel = newErosionLevel;
        }
        return map;
    }

    private static String total(int[][] map) {
        int counter = 0;
        for (int[] row : map) {
            counter += Arrays.stream(row).sum();
        }
        return Integer.toString(counter);
    }

    private static String rescue(Pair<int[][], Triple<Integer, Integer, Integer>> input) {
        Pair<Integer, Integer> target = new Pair<>(input.getSecond().getSecond(), input.getSecond().getThird());
        List<Route> routes = new ArrayList<>();
        Map<Pair<Pair<Integer, Integer>, Equipment>, Integer> past = new HashMap<>();
        past.put(new Pair<>(new Pair<>(0, 0), Equipment.TORCH), 0);
        routes.add(new Route(input.getFirst(), 0, Equipment.TORCH, new Pair<>(0, 0), target, past));
        while (!routes.get(0).getCurrentLocation().equals(target)) {
            Route route = routes.get(0);
            routes.remove(route);
            routes.addAll(route.nextRoutes());
            Collections.sort(routes);
        }
        return Integer.toString(routes.get(0).getTime());
    }
}
