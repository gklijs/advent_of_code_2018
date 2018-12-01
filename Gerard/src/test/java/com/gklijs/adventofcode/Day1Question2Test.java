package com.gklijs.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import com.gklijs.adventofcode.test.TestSchedulerExtension;
import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class Day1Question2Test {

    @ExtendWith(TestSchedulerExtension.class)
    @Test
    void example1(TestScheduler scheduler) {
        var result = new ArrayList<Integer>();
        Day1Question2.firstDoubleFrequency(Observable.fromIterable(Arrays.asList("+1", "-1")))
            .doOnSuccess(result::add)
            .subscribe();
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS);
        assertIterableEquals(Collections.singletonList(0), result);
    }

    @ExtendWith(TestSchedulerExtension.class)
    @Test
    void example2(TestScheduler scheduler) {
        var result = new ArrayList<Integer>();
        Day1Question2.firstDoubleFrequency(Observable.fromIterable(Arrays.asList("+3", "+3", "+4", "-2", "-4")))
            .doOnSuccess(result::add)
            .subscribe();
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS);
        assertIterableEquals(Collections.singletonList(10), result);
    }

    @ExtendWith(TestSchedulerExtension.class)
    @Test
    void example3(TestScheduler scheduler) {
        var result = new ArrayList<Integer>();
        Day1Question2.firstDoubleFrequency(Observable.fromIterable(Arrays.asList("-6", "+3", "+8", "+5", "-6")))
            .doOnSuccess(result::add)
            .subscribe();
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS);
        assertIterableEquals(Collections.singletonList(5), result);
    }

    @ExtendWith(TestSchedulerExtension.class)
    @Test
    void example4(TestScheduler scheduler) {
        var result = new ArrayList<Integer>();
        Day1Question2.firstDoubleFrequency(Observable.fromIterable(Arrays.asList("+7", "+7", "-2", "-7", "-4")))
            .doOnSuccess(result::add)
            .subscribe();
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS);
        assertIterableEquals(Collections.singletonList(14), result);
    }
}