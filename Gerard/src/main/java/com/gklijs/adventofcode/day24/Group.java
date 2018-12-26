package com.gklijs.adventofcode.day24;

import java.util.List;

public class Group implements Comparable<Group> {

    private int units;
    private final int hitPoints;
    private final int damage;
    private final Attack damageType;
    private final int initiative;
    private final List<Attack> weaknesses;
    private final List<Attack> immunities;
    private Group target = null;

    Group(final int units, final int hitPoints, final int damage, final Attack damageType, final int initiative, final List<Attack> weaknesses, final List<Attack> immunities) {
        this.units = units;
        this.hitPoints = hitPoints;
        this.damage = damage;
        this.damageType = damageType;
        this.initiative = initiative;
        this.weaknesses = weaknesses;
        this.immunities = immunities;
    }

    Group(Group group, int additionalDamage) {
        this.units = group.units;
        this.hitPoints = group.hitPoints;
        this.damage = group.damage + additionalDamage;
        this.damageType = group.damageType;
        this.initiative = group.initiative;
        this.weaknesses = group.weaknesses;
        this.immunities = group.immunities;
    }

    private int effectivePower() {
        if (units <= 0) {
            return 0;
        }
        return units * damage;
    }

    @Override
    public int compareTo(final Group o) {
        if (effectivePower() != o.effectivePower()) {
            return o.effectivePower() - effectivePower();
        }
        return o.initiative - initiative;
    }

    void setTarget(final Group target) {
        this.target = target;
    }

    void attack() {
        if (target != null) {
            int d = effectivePower();
            if (target.weaknesses.contains(damageType)) {
                d = 2 * d;
            }
            if (target.immunities.contains(damageType)) {
                d = 0;
            }
            target.takeDamage(d);
            target = null;
        }
    }

    private void takeDamage(int damage) {
        units = units - damage / hitPoints;
    }

    Attack getDamageType() {
        return damageType;
    }

    List<Attack> getWeaknesses() {
        return weaknesses;
    }

    List<Attack> getImmunities() {
        return immunities;
    }

    int getInitiative() {
        return initiative;
    }

    int getUnits() {
        return units;
    }
}
