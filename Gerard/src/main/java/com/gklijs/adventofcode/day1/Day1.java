package com.gklijs.adventofcode.day1;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.gklijs.adventofcode.Utils;
import com.gklijs.adventofcode.utils.Pair;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class Day1 {

    private Day1() {
        //prevent instantiation
    }

    public static Single<String> calculateFrequency(Flowable<String> frequencyChanges) {
        return frequencyChanges
            .map(Integer::valueOf)
            .reduce(0, Integer::sum)
            .map(Objects::toString);
    }

    public static Single<String> firstDoubleFrequency(Flowable<String> frequencyChanges) {
        return frequencyChanges
            .repeat()
            .map(Integer::valueOf)
            .scan(0, Integer::sum)
            .scan(new Pair<Set<Integer>, Integer>(new HashSet<>(), null), Utils::firstDuplicate)
            .takeUntil(pair -> pair.getSecond() != null)
            .lastOrError()
            .map(Pair::getSecond)
            .map(Objects::toString);
    }
}
