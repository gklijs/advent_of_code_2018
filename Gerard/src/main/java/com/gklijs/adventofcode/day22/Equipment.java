package com.gklijs.adventofcode.day22;

import java.util.function.Function;

public enum Equipment {
    CLIMBING_GEAR(x -> x != 2),
    TORCH(x -> x != 1),
    NONE(x -> x != 0);

    private Function<Integer, Boolean> validation;

    Equipment(final Function<Integer, Boolean> validation) {
        this.validation = validation;
    }

    Equipment next(int target) {
        for (Equipment equipment : Equipment.values()) {
            if (this != equipment && equipment.isValid(target)) {
                return equipment;
            }
        }
        return null;
    }

    boolean isValid(int target) {
        return validation.apply(target);
    }
}
