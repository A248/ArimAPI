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

package space.arim.api.jsonchat.adventure.internal;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import space.arim.api.jsonchat.testing.CombinationsOfActions;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IteratorBaseTest {

    private static final int[] ELEMENTS = new int[] {1, 10, 3, -4};

    @TestFactory
    public Stream<DynamicNode> simpleImpl() {
        Stream<ElementAction[]> combinations =
                CombinationsOfActions.ofEnumElements(ElementAction.class).getAllCombinations(ELEMENTS.length);
        return combinations.map((combination) -> {
            return DynamicTest.dynamicTest("Iteration for " + Arrays.toString(combination),
                    () -> iterate(combination));
        });
    }

    private void iterate(ElementAction[] combination) {
        IteratorBase<Integer> iter = new SimpleImpl(ELEMENTS);
        for (int n = 0; n < ELEMENTS.length; n++) {
            expect(ELEMENTS[n], iter, combination[n]);
        }
    }

    private void expect(int value, IteratorBase<Integer> iter, ElementAction action) {
        switch (action) {
        case CHECK_THEN_NEXT -> {
            assertTrue(iter.hasNext());
            assertEquals(value, iter.next());
        }
        case NEXT -> assertEquals(value, iter.next());
        default -> throw new UnsupportedOperationException("Unknown " + action);
        }
    }

    private enum ElementAction {
        CHECK_THEN_NEXT,
        NEXT
    }

    private static class SimpleImpl extends IteratorBase<Integer> {

        private final int[] elements;
        private int position;

        SimpleImpl(int[] elements) {
            this.elements = elements;
        }

        @Override
        public Integer getNext() {
            if (position == elements.length) {
                return null;
            }
            return elements[position++];
        }
    }
}
