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
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeekingIteratorTest {

    private static final Integer[] ELEMENTS = new Integer[] {1, 3, null, -5, 14};

    @TestFactory
    public Stream<DynamicNode> testAll() {
        return Stream.concat(
                testImpl(CombinationsOfActions.ofEnumElements(ElementAction.class).getAllCombinations(ELEMENTS.length),
                        ListIteratorPeekingIterator::new),
                testImpl(CombinationsOfActions.ofEnumElements(ElementAction.class).getAllCombinations(ELEMENTS.length),
                        AddedFunctionalityPeekingIterator::new));
    }

    private Stream<DynamicNode> testImpl(Stream<ElementAction[]> combinations,
                                         Function<Integer[], PeekingIterator<Integer>> impl) {
        Stream<DynamicNode> iterationTests = combinations.map((combination) -> {
            return DynamicTest.dynamicTest("Iteration for " + Arrays.toString(combination),
                    () -> new Container(impl).iterate(combination));
        });
        Stream<DynamicNode> nseTests = Stream.of(
                ElementAction.CHECK_THEN_NEXT, ElementAction.NEXT, ElementAction.CHECK_THEN_PEEK, ElementAction.PEEK)
                .map((how) -> {
                    return DynamicTest.dynamicTest("expectNoSuchElement", () -> new Container(impl).expectNoSuchElement(how));
                });
        return Stream.concat(nseTests, iterationTests);
    }

    private enum ElementAction {
        CHECK_THEN_NEXT,
        CHECK_THEN_PEEK,
        CHECK_THEN_PEEK_TWICE,
        NEXT,
        PEEK,
        PEEK_TWICE
    }

    private static class Container {

        private final PeekingIterator<Integer> iter;

        Container(Function<Integer[], PeekingIterator<Integer>> impl) {
            iter = impl.apply(ELEMENTS);
        }

        private void consumeAll() {
            for (Integer element : ELEMENTS) {
                assertEquals(element, iter.next(), "Consumed wrong element");
            }
        }

        void expectNoSuchElement(ElementAction how) {
            consumeAll();
            switch (how) {
            case CHECK_THEN_NEXT -> {
                assertFalse(iter.hasNext());
                assertThrows(NoSuchElementException.class, iter::next);
            }
            case NEXT -> assertThrows(NoSuchElementException.class, iter::next);
            case CHECK_THEN_PEEK -> {
                assertFalse(iter.hasNext());
                assertThrows(NoSuchElementException.class, iter::peek);
            }
            case PEEK -> assertThrows(NoSuchElementException.class, iter::peek);
            default -> throw new IllegalArgumentException();
            }
            switch (how) {
            case CHECK_THEN_NEXT, NEXT -> assertThrows(NoSuchElementException.class, iter::peek);
            case CHECK_THEN_PEEK, PEEK -> assertThrows(NoSuchElementException.class, iter::next);
            }
        }

        void iterate(ElementAction[] how) {
            for (int n = 0; n < ELEMENTS.length; n++) {
                expect(ELEMENTS[n], how[n]);
            }
        }

        private void expect(Integer value, ElementAction how) {
            switch (how) {
            case CHECK_THEN_NEXT -> {
                assertTrue(iter.hasNext());
                assertEquals(value, iter.next());
            }
            case CHECK_THEN_PEEK -> {
                assertTrue(iter.hasNext());
                assertEquals(value, iter.peek());
                assertEquals(value, iter.next(), "Element should still be visible after peek");
            }
            case CHECK_THEN_PEEK_TWICE -> {
                assertTrue(iter.hasNext());
                assertEquals(value, iter.peek());
                assertEquals(value, iter.peek(), "Repeated peek");
                assertEquals(value, iter.next(), "Element should still be visible after two peeks");
            }
            case NEXT -> assertEquals(value, iter.next());
            case PEEK -> {
                assertEquals(value, iter.peek());
                assertEquals(value, iter.next(), "Element should still be visible after peek");
            }
            case PEEK_TWICE -> {
                assertEquals(value, iter.peek());
                assertEquals(value, iter.peek(), "Repeated peek");
                assertEquals(value, iter.next(), "Element should still be visible after two peeks");
            }
            }
        }
    }


}
