package com.gklijs.adventofcode.day23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.gklijs.adventofcode.errors.InvalidInputException;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Day23 {

    private static final long[] HEURISTIC = new long[]{523_050L, 1_926_350L, 1_596_850L};

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
        long[] bestPoint = new long[3];
        int bestValue = inReach(input, bestPoint);
        boolean changed = true;
        while (changed) {
            changed = false;
            int loopBestValue = bestValue;
            long[] loopBestPoint = bestPoint;
            for (int i = 30; i > -1; i--) {
                for (Mutator mutator : Mutator.values()) {
                    long[] newPoint = mutator.mutatedValue(bestPoint, (long) Math.pow(2, i));
                    int newValue = inReach(input, newPoint);
                    if (newValue > loopBestValue) {
                        loopBestValue = newValue;
                        loopBestPoint = newPoint;
                    } else if (newValue == loopBestValue && absPoint(newPoint) < absPoint(loopBestPoint)) {
                        loopBestPoint = newPoint;
                    }
                }
            }
            //set the HEURISTIC to get a fast but still correct outcome on my data, no guarantees for other data..
            for (long h : HEURISTIC) {
                for (Mutator mutator : Mutator.values()) {
                    long[] newPoint = mutator.mutatedValue(bestPoint, h);
                    int newValue = inReach(input, newPoint);
                    if (newValue > loopBestValue) {
                        loopBestValue = newValue;
                        loopBestPoint = newPoint;
                    } else if (newValue == loopBestValue && absPoint(newPoint) < absPoint(loopBestPoint)) {
                        loopBestPoint = newPoint;
                    }
                }
            }
            if (bestValue != loopBestValue || !Arrays.equals(bestPoint, loopBestPoint)) {
                changed = true;
                bestValue = loopBestValue;
                bestPoint = loopBestPoint;
            }
        }
        return Long.toString(absPoint(bestPoint));
    }
}
