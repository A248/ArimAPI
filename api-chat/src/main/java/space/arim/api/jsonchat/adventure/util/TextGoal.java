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

import java.util.Set;

/**
 * Parts of a component which may be targeted
 */
public enum TextGoal {
    /**
     * Text of textual components. {@code TextComponent.value()}
     */
    SIMPLE_TEXT,
    /**
     * The text within the components of a hover event's value.
     */
    HOVER_TEXT,
    /**
     * The click value
     */
    CLICK_VALUE,
    /**
     * The insertion value
     */
    INSERTION_VALUE;

    private static final Set<TextGoal> ALL_GOALS = Set.of(values());
    private static final Set<TextGoal> SIMPLE_TEXT_ONLY = Set.of(SIMPLE_TEXT);

    /**
     * An immutable set consisting of all text goals
     *
     * @return all goals
     */
    public static Set<TextGoal> allGoals() {
        return ALL_GOALS;
    }

    /**
     * An immutable set consisting of only {@code SIMPLE_TEXT}
     *
     * @return only {@code SIMPLE_TEXT}
     */
    public static Set<TextGoal> simpleTextOnly() {
        return SIMPLE_TEXT_ONLY;
    }
}
