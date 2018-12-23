package com.gklijs.adventofcode.day23;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.gklijs.adventofcode.errors.InvalidInputException;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Day23 {

    private static final long H_M = 80_000_000L;
    private static final long M = 900_001L;

    private Day23() {
        //prevent instantiation
    }

    public static Single<String> dronesInReach(Observable<String> input) {
        return input
            .map(Day23::toDrone)
            .collect((Callable<ArrayList<long[]>>) ArrayList::new, List::add)
            .map(Day23::inReach);
    }

    public static Single<String> mostDrones(Observable<String> input) {
        return input
            .map(Day23::toDrone)
            .collect((Callable<ArrayList<long[]>>) ArrayList::new, List::add)
            .map(Day23::most);
    }

    private static long[] toDrone(String input) {
        long[] result = new long[4];
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
            result[3] = Integer.parseInt(builder.toString());
            return result;
        }
        throw new InvalidInputException("Could not get drone from: " + input);
    }

    private static String inReach(List<long[]> input) {
        long[] max = input.get(0);
        for (long[] drone : input) {
            if (drone[3] > max[3]) {
                max = drone;
            }
        }
        int counter = 0;
        for (long[] drone : input) {
            if (Math.abs(drone[0] - max[0]) + Math.abs(drone[1] - max[1]) + Math.abs(drone[2] - max[2]) <= max[3]) {
                counter++;
            }
        }
        return Integer.toString(counter);
    }

    private static int inReach(List<long[]> input, long[] point) {
        int counter = 0;
        for (long[] drone : input) {
            if (Math.abs(drone[0] - point[0]) + Math.abs(drone[1] - point[1]) + Math.abs(drone[2] - point[2]) <= drone[3]) {
                counter++;
            }
        }
        return counter;
    }

    private static long absPoint(long[] point) {
        return Math.abs(point[0]) + Math.abs(point[1]) + Math.abs(point[2]);
    }

    private static String most(List<long[]> input) {
        int bestValue = 0;
        long[] bestPoint = new long[3];
        for (long[] drone : input) {
            for (Corner corner : Corner.values()) {
                long[] newPoint = corner.point(drone, drone[3]);
                int newValue = inReach(input, newPoint);
                if (newValue > bestValue) {
                    bestValue = newValue;
                    bestPoint = newPoint;
                }
            }
        }
/*        for (long x = -1 * H_M; x < H_M; x += M) {
            for (long y = -1 * H_M; y < H_M; y += M) {
                for (long z = -1 * H_M; z < H_M; z += M) {
                    long[] newPoint = new long[]{x, y, z};
                    int newValue = inReach(input, newPoint);
                    if (newValue > bestValue) {
                        System.out.println("Best value is: " + bestValue);
                        bestValue = newValue;
                        bestPoint = newPoint;
                    }
                }
            }
        }*/
        //68945158
        System.out.println("Best value is: " + bestValue);
        for (int i = 30; i > -1; i--) {
            for (Corner corner : Corner.values()) {
                long[] newPoint = corner.point(bestPoint, (int) Math.pow(2, i));
                int newValue = inReach(input, newPoint);
                if (newValue > bestValue) {
                    bestValue = newValue;
                    bestPoint = newPoint;
                    System.out.println("Best value is now : " + bestValue);
                } else if (newValue == bestValue && absPoint(newPoint) < absPoint(bestPoint)) {
                    bestPoint = newPoint;
                }
            }
        }

        return Long.toString(absPoint(bestPoint));
    }
}
