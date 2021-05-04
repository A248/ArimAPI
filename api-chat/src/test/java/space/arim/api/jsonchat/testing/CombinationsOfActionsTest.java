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

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static space.arim.api.jsonchat.testing.CombinationsOfActionsTest.Action.A1;
import static space.arim.api.jsonchat.testing.CombinationsOfActionsTest.Action.A2;
import static space.arim.api.jsonchat.testing.CombinationsOfActionsTest.Action.A3;

public class CombinationsOfActionsTest {

    @Test
    public void expectCombinations() {
        Iterator<Action[]> combinations =
                CombinationsOfActions.ofEnumElements(Action.class).getAllCombinations(2).iterator();

        assertCombinations(combinations);
    }

    private void assertCombinations(Iterator<Action[]> combinations) {
        /*combinations.forEachRemaining((combination) -> {
            System.out.println("Combination " + java.util.Arrays.toString(combination));
        });*/
        assertArrayEquals(new Action[] {A1, A1}, combinations.next());
        assertArrayEquals(new Action[] {A2, A1}, combinations.next());
        assertArrayEquals(new Action[] {A3, A1}, combinations.next());
        assertArrayEquals(new Action[] {A1, A2}, combinations.next());
        assertArrayEquals(new Action[] {A2, A2}, combinations.next());
        assertArrayEquals(new Action[] {A3, A2}, combinations.next());
        assertArrayEquals(new Action[] {A1, A3}, combinations.next());
        assertArrayEquals(new Action[] {A2, A3}, combinations.next());
        assertArrayEquals(new Action[] {A3, A3}, combinations.next());
    }

    @Test
    public void expectCombinationsIterator() {
        Iterator<Action[]> combinations = new CombinationsOfActionsIterator<>(Action.class.getEnumConstants(), 2);
        assertCombinations(combinations);
    }

    enum Action {
        A1,
        A2,
        A3
    }

    @Test
    public void zeroLengthCombinations() {
        CombinationsOfActions<Boolean> combinationsOfActions = new CombinationsOfActions<>(new Boolean[] {true, false});
        //noinspection ResultOfMethodCallIgnored
        assertThrows(IllegalArgumentException.class, () -> combinationsOfActions.getAllCombinations(0).toList());
    }
}
