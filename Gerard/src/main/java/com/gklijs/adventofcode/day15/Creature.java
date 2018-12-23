package com.gklijs.adventofcode.day15;

import com.gklijs.adventofcode.utils.Pair;

abstract class Creature implements Comparable<Creature> {

    private Pair<Integer, Integer> cord;
    private int stepsTaken;
    private int hitpoints;

    Creature(int x, int y) {
        cord = new Pair<>(x, y);
        hitpoints = 200;
    }

    @Override
    public int compareTo(final Creature o) {
        if (stepsTaken != o.stepsTaken) {
            return stepsTaken - o.stepsTaken;
        }
        if (!cord.getSecond().equals(o.cord.getSecond())) {
            return cord.getSecond() - o.cord.getSecond();
        }
        return cord.getFirst() - o.cord.getFirst();
    }

    Pair<Integer, Integer> getCord() {
        return cord;
    }

    int takeHit(int attack) {
        hitpoints -= attack;
        return hitpoints;
    }

    Pair<Integer, Integer> move(Move move) {
        cord = move.nextCord(cord);
        stepsTaken++;
        return cord;
    }

    int getStepsTaken() {
        return stepsTaken;
    }

    int getHitpoints() {
        return hitpoints;
    }

    abstract char getTarget();

    abstract int getPower();
}
