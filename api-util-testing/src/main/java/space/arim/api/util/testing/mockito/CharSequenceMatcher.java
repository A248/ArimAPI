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

package space.arim.api.util.testing.mockito;

import org.mockito.ArgumentMatcher;

/**
 * Mockito matcher to evaluate equality of char sequences using {@code toString}
 *
 */
public final class CharSequenceMatcher implements ArgumentMatcher<CharSequence> {

    private final CharSequence expectedValue;

    /**
     * Creates from an expected value
     *
     * @param expectedValue the expected value, which may be null
     */
    public CharSequenceMatcher(CharSequence expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean matches(CharSequence argument) {
        if (expectedValue == null) {
            return argument == null;
        }
        return expectedValue.toString().equals(argument.toString());
    }

    @Override
    public String toString() {
        return "CharSequenceMatcher{" + expectedValue + '}';
    }
}
