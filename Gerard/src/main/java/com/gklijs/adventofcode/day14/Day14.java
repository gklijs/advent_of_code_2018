package com.gklijs.adventofcode.day14;

import com.gklijs.adventofcode.utils.Pair;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class Day14 {

    private Day14() {
        //prevent instantiation
    }

    public static Single<String> tenAfter(Flowable<String> input) {
        return input
            .lastOrError()
            .map(Integer::parseInt)
            .map(recipes -> new RecipeList(recipes + 10))
            .map(RecipeList::complete)
            .map(Day14::printLastTen);
    }

    public static Single<String> doTill(Flowable<String> input) {
        return input
            .lastOrError()
            .map(Day14::toIntArray)
            .map(sequence -> new Pair<>(new RecipeList(100000000), sequence))
            .map(pair -> pair.getFirst().complete(pair.getSecond()))
            .map(Object::toString);
    }

    private static String printLastTen(int[] scores) {
        StringBuilder builder = new StringBuilder();
        for (int i = scores.length - 10; i < scores.length; i++) {
            builder.append(scores[i]);
        }
        return builder.toString();
    }

    private static int[] toIntArray(String input) {
        char[] chars = input.toCharArray();
        int[] ints = new int[chars.length];
        for (int x = 0; x < chars.length; x++) {
            ints[x] = Character.getNumericValue(chars[x]);
        }
        return ints;
    }
}
