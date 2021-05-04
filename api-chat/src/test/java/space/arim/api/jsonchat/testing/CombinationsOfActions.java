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

package space.arim.api.jsonchat.testing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Helper for generating all the possible ways a group of elements can have certain
 * actions applied to them in certain orders
 *
 * @param <A> the element type
 */
public final class CombinationsOfActions<A> {

    private final A[] actions;

    /**
     * Creates from subject actions
     *
     * @param actions the actions
     */
    public CombinationsOfActions(A[] actions) {
        this.actions = actions.clone();
    }

    /**
     * Gets all possible combinations. <br>
     * <br>
     * The length of each combination is called the exponent because the total
     * amount of combinations generated will be the number of available actions
     * raised to the power of the length of each combination.
     *
     * @param exponent the length of each combination
     * @return all possible combinations
     */
    public Stream<A[]> getAllCombinations(int exponent) {
        if (exponent == 0) {
            throw new IllegalArgumentException("Cannot have 0-length combinations");
        }
        Iterator<A[]> iterator = new CombinationsOfActionsIterator<>(actions, exponent);
        int spliteratorCharacteristics = Spliterator.DISTINCT | Spliterator.IMMUTABLE
                | Spliterator.NONNULL | Spliterator.ORDERED;
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator, spliteratorCharacteristics), false);
    }

    public static <A extends Enum<A>> CombinationsOfActions<A> ofEnumElements(Class<A> enumClass) {
        return new CombinationsOfActions<>(enumClass.getEnumConstants());
    }

    /*
     *
     * Imperative and eager implementation.
     * Kept because although the lazy iterator version passes the same tests,
     * this implementation is far easier to follow logically.
     *
     */

    private void collectCombinations(final Collection<A[]> combinations,
                                     final A[] actionsSoFar, final int index) {
        for (int n = 0; n < actions.length; n++) {
            A[] actionsContinued = actionsSoFar.clone();
            actionsContinued[index] = actions[n];
            if (index == 0) {
                combinations.add(actionsContinued);
            } else {
                collectCombinations(combinations, actionsContinued, index - 1);
            }
        }
    }

    private Collection<A[]> getAllCombinationsEager(int exponent) {
        int testCount = (int) Math.pow(actions.length, exponent);

        List<A[]> combinations = new ArrayList<>(testCount);

        @SuppressWarnings("unchecked")
        A[] casted = (A[]) Array.newInstance(actions.getClass().getComponentType(), exponent);
        collectCombinations(combinations, casted, exponent - 1);
        assertEquals(testCount, combinations.size());

        return combinations;
    }

}
