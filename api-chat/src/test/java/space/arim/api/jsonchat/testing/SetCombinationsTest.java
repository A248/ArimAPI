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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetCombinationsTest {

    @Test
    public void integerCombination() {
        Set<Integer> elements = Set.of(1, 2, 3);
        Set<Set<Integer>> combinations = Set.of(
                Set.of(), Set.of(1), Set.of(2), Set.of(3),
                Set.of(1, 2), Set.of(2, 3), Set.of(1, 3),
                Set.of(1, 2, 3));
        assertEquals(
                combinations,
                Set.copyOf(new SetCombinations<>(elements).possibleSets().toList()));
    }
}
