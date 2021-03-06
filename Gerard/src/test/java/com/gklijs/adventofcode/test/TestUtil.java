package com.gklijs.adventofcode.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.TestScheduler;
import static org.junit.Assert.assertEquals;

public class TestUtil {

    private TestUtil() {
        //prevent instantiation
    }

    public static <T> void testSingle(TestScheduler scheduler, String[] input, Function<Flowable<String>, Single<T>> function, T expected) {
        AtomicReference<T> result = new AtomicReference<>();
        try {
            function.apply((Observable.fromArray(input).toFlowable(BackpressureStrategy.BUFFER)))
                .doOnSuccess(result::set)
                .timeout(2, TimeUnit.SECONDS)
                .subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS);
        assertEquals(expected, result.get());
    }
}
