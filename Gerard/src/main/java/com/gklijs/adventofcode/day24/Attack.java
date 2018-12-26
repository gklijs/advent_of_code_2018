package com.gklijs.adventofcode.day24;

public enum Attack {
    FIRE("fire"),
    COLD("cold"),
    SLASHING("slashing"),
    RADIATION("radiation"),
    BLUDGEONING("bludgeoning");

    private String value;

    Attack(final String value) {
        this.value = value;
    }

    static boolean isAttack(String v) {
        for (Attack attack : Attack.values()) {
            if (attack.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

    static Attack get(String v) {
        for (Attack attack : Attack.values()) {
            if (attack.value.equals(v)) {
                return attack;
            }
        }
        return null;
    }
}
