package com.gklijs.adventofcode;

import com.gklijs.adventofcode.day22.Day22;
import com.gklijs.adventofcode.day23.Day23;
import com.gklijs.adventofcode.test.TestSchedulerExtension;
import io.reactivex.schedulers.TestScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static com.gklijs.adventofcode.test.TestUtil.testSingle;

class Day23Test {

    String[] data = new String[]{
        "pos=<0,0,0>, r=4",
        "pos=<1,0,0>, r=1",
        "pos=<4,0,0>, r=3",
        "pos=<0,2,0>, r=1",
        "pos=<0,5,0>, r=3",
        "pos=<0,0,3>, r=1",
        "pos=<1,1,1>, r=1",
        "pos=<1,1,2>, r=1",
        "pos=<1,3,1>, r=1"
    };

    @ExtendWith(TestSchedulerExtension.class)
    @Test
    void example1(TestScheduler scheduler) {
        testSingle(scheduler, data, Day23::dronesInReach, "7");
    }

    String[] data2 = new String[]{
        "pos=<10,12,12>, r=2",
        "pos=<12,14,12>, r=2",
        "pos=<16,12,12>, r=4",
        "pos=<14,14,14>, r=6",
        "pos=<50,50,50>, r=200",
        "pos=<10,10,10>, r=5"
    };

    @ExtendWith(TestSchedulerExtension.class)
    @Test
    void example2(TestScheduler scheduler) {
        testSingle(scheduler, data2, Day23::mostDrones, "36");
    }
}