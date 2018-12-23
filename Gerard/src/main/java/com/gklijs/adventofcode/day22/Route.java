package com.gklijs.adventofcode.day22;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gklijs.adventofcode.utils.Direction;
import com.gklijs.adventofcode.utils.Pair;

class Route implements Comparable<Route> {

    private final int[][] map;
    private final int time;
    private final Equipment equipment;
    private final Pair<Integer, Integer> currentLocation;
    private final Pair<Integer, Integer> target;
    private Map<Pair<Pair<Integer, Integer>, Equipment>, Integer> past;

    Route(final int[][] map, final int time, final Equipment equipment, final Pair<Integer, Integer> currentLocation,
          final Pair<Integer, Integer> target, final Map<Pair<Pair<Integer, Integer>, Equipment>, Integer> past) {
        this.map = map;
        this.time = time;
        this.equipment = equipment;
        this.currentLocation = currentLocation;
        this.target = target;
        this.past = past;
    }

    private boolean validNext(Pair<Integer, Integer> nextLocation) {
        if (nextLocation.getFirst() < 0 || nextLocation.getSecond() < 0) {
            return false;
        }
        return nextLocation.getFirst() < map[0].length && nextLocation.getSecond() < map.length;
    }

    private boolean isFast(Route route) {
        Pair<Pair<Integer, Integer>, Equipment> tag = new Pair<>(route.currentLocation, route.equipment);
        if (past.containsKey(tag)) {
            int oldTime = past.get(tag);
            if (route.time < oldTime) {
                past.put(tag, route.time);
                return true;
            } else {
                return false;
            }
        } else {
            past.put(tag, route.time);
            return true;
        }
    }

    List<Route> nextRoutes() {
        List<Route> routes = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Pair<Integer, Integer> nextLocation = direction.nextCord(currentLocation);
            boolean nextIsTarget = nextLocation.equals(target);
            if (!validNext(nextLocation)) {
                continue;
            }
            int nextType = map[nextLocation.getSecond()][nextLocation.getFirst()];
            Route route;
            if (equipment.isValid(nextType)) {
                route = new Route(map, nextIsTarget && equipment != Equipment.TORCH ? time + 8 : time + 1, equipment, nextLocation, target, past);
            } else {
                Equipment nextEquipment = equipment.next(map[currentLocation.getSecond()][currentLocation.getFirst()]);
                route = new Route(map, nextIsTarget && nextEquipment != Equipment.TORCH ? time + 15 : time + 8, nextEquipment, nextLocation, target, past);
            }
            if (isFast(route)) {
                routes.add(route);
            }
        }
        return routes;
    }

    int getTime() {
        return time;
    }

    Pair<Integer, Integer> getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Route) {
            Route other = (Route) o;
            return currentLocation.equals(other.currentLocation) && time == other.time && equipment == other.equipment;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + currentLocation.hashCode();
        result = 31 * result + Integer.hashCode(time);
        result = 31 * result + equipment.hashCode();
        return result;
    }

    @Override
    public int compareTo(final Route o) {
        return time - o.time;
    }
}
