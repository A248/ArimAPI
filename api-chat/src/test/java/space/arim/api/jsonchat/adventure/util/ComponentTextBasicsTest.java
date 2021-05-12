/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
 *
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */

package space.arim.api.jsonchat.adventure.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import space.arim.api.jsonchat.testing.CombinationsOfActions;
import space.arim.api.jsonchat.testing.SetCombinations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static space.arim.api.jsonchat.adventure.util.TextGoal.CLICK_VALUE;
import static space.arim.api.jsonchat.adventure.util.TextGoal.HOVER_TEXT;
import static space.arim.api.jsonchat.adventure.util.TextGoal.INSERTION_VALUE;
import static space.arim.api.jsonchat.adventure.util.TextGoal.SIMPLE_TEXT;
import static space.arim.api.jsonchat.testing.TestUtil.randomString;

public class ComponentTextBasicsTest {

    @Test
    public void badConstruction() {
        assertThrows(NullPointerException.class, () -> ComponentText.create(null));
        assertThrows(NullPointerException.class,
                () -> ComponentText.create(Component.empty(), (Set<TextGoal>) null));
        assertThrows(NullPointerException.class,
                () -> ComponentText.create(Component.empty(), Collections.singleton(null)));
    }

    private Stream<Set<TextGoal>> possibleTextGoals() {
        return new SetCombinations<>(TextGoal.allGoals()).possibleSets();
    }

    @TestFactory
    public Stream<DynamicNode> badArguments() {
        return possibleTextGoals().flatMap((goals) -> {
            return Stream.of(Component.empty(), Component.text("text"), Component.text("text", NamedTextColor.BLUE))
                    .map((component) -> {
                        return DynamicTest.dynamicTest("Using component " + component + " and goals " + goals,
                                () -> runBadArguments(component, goals));
                    });
        });
    }

    private void runBadArguments(Component component, Set<TextGoal> goals) {
        ComponentText componentText = ComponentText.create(component, goals);
        assertThrows(NullPointerException.class, () -> componentText.contains(null));
        assertThrows(NullPointerException.class, () -> componentText.replaceText("a", null));
        assertThrows(NullPointerException.class, () -> componentText.replaceText((CharSequence) null, "b"));
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> componentText.replaceText(Pattern.compile("a"), null));
        assertThrows(NullPointerException.class, () -> componentText.replaceText((Pattern) null, "b"));
    }

    @TestFactory
    public Stream<DynamicNode> sumLength() {
        String content = randomString();
        String hoverValue = randomString();
        String clickValue = randomString();
        String insertion = randomString();
        Component component = Component.text().content(content)
                .hoverEvent(HoverEvent.showText(Component.text(hoverValue)))
                .clickEvent(ClickEvent.openUrl(clickValue))
                .insertion(insertion)
                .build();
        return possibleTextGoals().map((goals) -> {
            return DynamicTest.dynamicTest("Using goals " + goals, () -> {
                int expectedSum = 0;
                if (goals.contains(SIMPLE_TEXT)) {
                    expectedSum += content.length();
                }
                if (goals.contains(HOVER_TEXT)) {
                    expectedSum += hoverValue.length();
                }
                if (goals.contains(CLICK_VALUE)) {
                    expectedSum += clickValue.length();
                }
                if (goals.contains(INSERTION_VALUE)) {
                    expectedSum += insertion.length();
                }
                assertEquals(expectedSum, ComponentText.create(component, goals).sumLength());
            });
        });
    }

    /**
     * Returns a random string. If {@code containing}, the generated string includes
     * the given {@code value}, if not, it does not
     * @param value the value to contain or not
     * @param containing whether to contain the value
     * @return a random string containing the value or not
     */
    private static String randomStringContainingOrNot(String value, boolean containing) {
        if (containing) {
            return randomString() + value + randomString();
        }
        String result;
        do {
            result = randomString();
        } while (result.contains(value));
        return result;
    }

    /** A runnable test for {@code contains} */
    record TestData(String target, Set<GoalAndContains> goalAndContainsCombinations) {
        Set<TextGoal> goals() {
            return goalAndContainsCombinations().stream().map(GoalAndContains::goal).collect(Collectors.toUnmodifiableSet());
        }
    }
    record GoalAndContains(TextGoal goal, boolean contains) {}
    @TestFactory
    public Stream<DynamicNode> contains() {
        Stream<TestData> allTestData = possibleTextGoals().flatMap((goals) -> {
            if (goals.isEmpty()) {
                return Stream.empty();
            }
            return new CombinationsOfActions<>(new Boolean[] {true, false}).getAllCombinations(goals.size())
                    .map((contains) -> {
                        // Pair each goal with whether it is contained in this test
                        Set<GoalAndContains> goalAndContainsCombinations = new HashSet<>();
                        int n = 0;
                        for (TextGoal goal : goals) {
                            goalAndContainsCombinations.add(new GoalAndContains(goal, contains[n]));
                            n++;
                        }
                        return new TestData(randomString(), goalAndContainsCombinations);
                    });
        });
        return allTestData.map((testData) -> {
            return DynamicTest.dynamicTest("With test data " + testData, () -> runContains(testData));
        });
    }

    private void runContains(TestData testData) {
        TextComponent.Builder builder = Component.text();
        boolean expectedAnyContains = false;
        String target = testData.target();
        for (GoalAndContains goalAndContains : testData.goalAndContainsCombinations()) {
            boolean contains = goalAndContains.contains();
            if (contains) {
                expectedAnyContains = true;
            }
            String value = randomStringContainingOrNot(target, contains);
            var goal = goalAndContains.goal;
            switch (goal) {
            case SIMPLE_TEXT -> builder.content(value);
            case HOVER_TEXT -> builder.hoverEvent(HoverEvent.showText(Component.text(value)));
            case CLICK_VALUE -> builder.clickEvent(ClickEvent.runCommand(value));
            case INSERTION_VALUE -> builder.insertion(value);
            default -> throw new IllegalArgumentException("Unknown goal " + goal);
            }
        }
        assertEquals(expectedAnyContains, ComponentText.create(builder.build(), testData.goals()).contains(target));
    }
}
