package com.gklijs.adventofcode.day23;

enum Mutator {
    ONE(1, 0, 0),
    TWO(-1, 0, 0),
    THREE(0, 1, 0),
    FOUR(0, -1, 0),
    FIVE(0, 0, 1),
    SIX(0, 0, -1),
    SEVEN(1, 1, 0),
    EIGHT(1, -1, 0),
    NINE(1, 0, 1),
    TEN(1, 0, -1),
    ELEVEN(-1, 1, 0),
    TWELVE(-1, -1, 0),
    THIRTEEN(-1, 0, 1),
    FORTEEN(-1, 0, -1),
    FIFTEEN(0, 1, 1),
    SIXTEEN(0, 1, -1),
    SEVENTEEN(0, -1, 1),
    EIGHTEEN(0, -1, -1),
    NINETEEN(1, 1, 1),
    TWENTY(1, 1, -1),
    TWENTYONE(1, -1, 1),
    TWENTYTWO(1, -1, -1),
    TWENTYTHREE(-1, 1, 1),
    TWENTYFOUR(-1, 1, -1),
    TWENTYFIVE(-1, -1, 1),
    TWENTYSIX(-1, -1, -1);

    private int x;
    private int y;
    private int z;

    Mutator(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    long[] mutatedValue(long[] ori, long step) {
        long[] result = ori.clone();
        result[0] = result[0] + x * step;
        result[1] = result[1] + y * step;
        result[2] = result[2] + z * step;
        return result;
    }
}
