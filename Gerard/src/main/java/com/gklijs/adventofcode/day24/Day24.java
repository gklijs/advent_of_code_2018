package com.gklijs.adventofcode.day24;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.Single;

public class Day24 {

    private Day24() {
        //prevent instantiation
    }

    public static Single<String> fight(Observable<String> input) {
        return input
            .reduce(new ArrayList<>(), Day24::reduce)
            .map(Day24::fight)
            .map(Day24::getTotalUnits);
    }

    public static Single<String> fightEnahnced(Observable<String> input) {
        return input
            .reduce(new ArrayList<>(), Day24::reduce)
            .map(Day24::fightEnhanced)
            .map(Day24::getTotalUnits);
    }

    private static String getTotalUnits(List<List<Group>> input) {
        for (List<Group> groups : input) {
            if (!groups.isEmpty()) {
                return Integer.toString(groups.stream().mapToInt(Group::getUnits).sum());
            }
        }
        return "0";
    }

    private static List<List<Group>> reduce(List<List<Group>> result, String input) {
        if (!input.isEmpty()) {
            if (Character.isDigit(input.charAt(0))) {
                result.get(result.size() - 1).add(toGroup(input));
            } else {
                result.add(new ArrayList<>());
            }
        }
        return result;
    }

    private static void pickTarget(Group attacker, Set<Group> pickedTargets, List<Group> optionalTargets) {
        for (Group optionalTarget : optionalTargets) {
            if (!pickedTargets.contains(optionalTarget) && optionalTarget.getWeaknesses().contains(attacker.getDamageType())) {
                pickedTargets.add(optionalTarget);
                attacker.setTarget(optionalTarget);
                return;
            }
        }
        for (Group optionalTarget : optionalTargets) {
            if (!pickedTargets.contains(optionalTarget) && !optionalTarget.getImmunities().contains(attacker.getDamageType())) {
                pickedTargets.add(optionalTarget);
                attacker.setTarget(optionalTarget);
                return;
            }
        }
    }

    private static List<List<Group>> fight(List<List<Group>> input) {
        List<Group> immuneSystem = input.get(0);
        List<Group> infection = input.get(1);
        int unitsAlive = immuneSystem.stream().mapToInt(Group::getUnits).sum() + infection.stream().mapToInt(Group::getUnits).sum();
        while (!immuneSystem.isEmpty() && !infection.isEmpty()) {
            Collections.sort(immuneSystem);
            Collections.sort(infection);
            Set<Group> pickedTargets = new HashSet<>();
            for (Group group : immuneSystem) {
                pickTarget(group, pickedTargets, infection);
            }
            for (Group group : infection) {
                pickTarget(group, pickedTargets, immuneSystem);
            }
            List<Group> attackers = new ArrayList<>(immuneSystem);
            attackers.addAll(infection);
            attackers.sort(Comparator.comparingInt(Group::getInitiative).reversed());
            for (Group attacker : attackers) {
                attacker.attack();
            }
            immuneSystem = immuneSystem.stream().filter(group -> group.getUnits() > 0).collect(Collectors.toList());
            infection = infection.stream().filter(group -> group.getUnits() > 0).collect(Collectors.toList());
            int newAlive = immuneSystem.stream().mapToInt(Group::getUnits).sum() + infection.stream().mapToInt(Group::getUnits).sum();
            if (newAlive == unitsAlive) {
                immuneSystem = new ArrayList<>();
                infection = new ArrayList<>();
                break;
            } else {
                unitsAlive = newAlive;
            }
        }
        input.clear();
        input.add(immuneSystem);
        input.add(infection);
        return input;
    }

    private static List<List<Group>> getEnhanced(List<List<Group>> input, int additionalDamage) {
        List<Group> immuneSystem = new ArrayList<>();
        List<Group> infection = new ArrayList<>();
        for (Group group : input.get(0)) {
            immuneSystem.add(new Group(group, additionalDamage));
        }
        for (Group group : input.get(1)) {
            infection.add(new Group(group, 0));
        }
        List<List<Group>> result = new ArrayList<>();
        result.add(immuneSystem);
        result.add(infection);
        return result;
    }

    private static List<List<Group>> fightEnhanced(List<List<Group>> input) {
        List<List<Group>> result = fight(getEnhanced(input, 0));
        int counter = 0;
        while (result.get(0).isEmpty()) {
            counter++;
            result = fight(getEnhanced(input, counter));
        }
        return result;
    }

    private static Group toGroup(String input) {
        StringBuilder builder = new StringBuilder();
        boolean betweenBrackets = false;
        int counter = 0;
        int hitPoints = 0;
        int units = 0;
        int damage = 0;
        boolean weakness = true;
        List<Attack> weaknesses = new ArrayList<>();
        List<Attack> immunities = new ArrayList<>();
        Attack damageType = null;
        for (char c : input.toCharArray()) {
            if (c == '(') {
                betweenBrackets = true;
                builder.setLength(0);
            } else if (betweenBrackets) {
                if (Character.isAlphabetic(c)) {
                    builder.append(c);
                } else if (c == ' ' || c == ')') {
                    if ("immune".equals(builder.toString())) {
                        weakness = false;
                    }
                    if ("weak".equals(builder.toString())) {
                        weakness = true;
                    }
                    if (Attack.isAttack(builder.toString())) {
                        if (weakness) {
                            weaknesses.add(Attack.get(builder.toString()));
                        } else {
                            immunities.add(Attack.get(builder.toString()));
                        }
                    }
                    builder.setLength(0);
                    if (c == ')') {
                        betweenBrackets = false;
                    }
                }
            } else {
                if (Character.isDigit(c) || (Character.isAlphabetic(c) && counter == 3)) {
                    builder.append(c);
                } else if (c == ' ' && counter == 3 && builder.length() > 0 && damageType == null) {
                    damageType = Attack.get(builder.toString());
                    builder.setLength(0);
                } else if (c == ' ' && builder.length() > 0) {
                    switch (counter) {
                        case 0:
                            units = Integer.parseInt(builder.toString());
                            break;
                        case 1:
                            hitPoints = Integer.parseInt(builder.toString());
                            break;
                        case 2:
                            damage = Integer.parseInt(builder.toString());
                            break;
                        default:
                            break;
                    }
                    builder.setLength(0);
                    counter++;
                }
            }
        }
        int initiative = Integer.parseInt(builder.toString());
        return new Group(units, hitPoints, damage, damageType, initiative, weaknesses, immunities);
    }
}
