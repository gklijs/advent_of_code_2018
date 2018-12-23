package com.gklijs.adventofcode.day23;

enum Corner {
    A(1, 0, 0),
    B(-1, 0, 0),
    C(0, 1, 0),
    D(0, -1, 0),
    E(0, 0, 1),
    F(0, 0, -1);

    private int xIs;
    private int yIs;
    private int zIs;

    Corner(final int xIs, final int yIs, final int zIs) {
        this.xIs = xIs;
        this.yIs = yIs;
        this.zIs = zIs;
    }

    long[] point(long[] drone, long add) {
        long[] r = new long[3];
        r[0] = drone[0] + xIs * add;
        r[1] = drone[1] + yIs * add;
        r[2] = drone[2] + zIs * add;
        return r;
    }
}