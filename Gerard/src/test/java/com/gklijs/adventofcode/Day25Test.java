package com.gklijs.adventofcode;

import com.gklijs.adventofcode.day24.Day24;
import com.gklijs.adventofcode.day25.Day25;
import com.gklijs.adventofcode.test.TestSchedulerExtension;
import io.reactivex.schedulers.TestScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static com.gklijs.adventofcode.test.TestUtil.testSingle;

class Day25Test {

    String[] data = new String[]{
        "-1,2,2,0",
        "0,0,2,-2",
        "0,0,0,-2",
        "-1,2,0,0",
        "-2,-2,-2,2",
        "3,0,2,-1",
        "-1,3,2,2",
        "-1,0,-1,0",
        "0,2,1,-2",
        "3,0,0,0"
    };

    @ExtendWith(TestSchedulerExtension.class)
    @Test
    void example1(TestScheduler scheduler) {
        testSingle(scheduler, data, Day25::c, "4");
    }
}