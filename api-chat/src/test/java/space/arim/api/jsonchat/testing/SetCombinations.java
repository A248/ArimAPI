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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Discovers all combinations of elements in a variable-length set. <br>
 * <br>
 * Differs from {@code CombinationsOfActions} which generates combinations
 * fixed in length.
 *
 * @param <E> the element type
 */
public final class SetCombinations<E> {

    private final Set<E> fullSet;

    public SetCombinations(Set<E> fullSet) {
        this.fullSet = fullSet;
    }

    /**
     * Creates all combinations of the input set
     *
     * @return all combinations of elements, from the empty set, to the full set, and everywhere in between
     */
    public Stream<Set<E>> possibleSets() {
        List<E> list = new ArrayList<>(fullSet);
        int size = list.size();
        return CombinationsOfActions.ofEnumElements(ElementStatus.class).getAllCombinations(size)
                .map((elementStatuses) -> {
                    Set<E> possibleSet = new HashSet<>(size);
                    for (int n = 0; n < size; n++) {
                        if (elementStatuses[n] == ElementStatus.PRESENT) {
                            possibleSet.add(list.get(n));
                        }
                    }
                    return possibleSet;
                });
    }

    private enum ElementStatus {
        PRESENT,
        NOT_PRESENT
    }
}
