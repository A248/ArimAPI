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

package space.arim.api.env.legacypaper;

import com.destroystokyo.paper.Title;
import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.Objects;

/**
 * com.destroystokyo.paper.Title does not implement equals and hashCode,
 * so method argument verification with mockito has to use this as a workaround
 */
public final class TitleMatcher implements ArgumentMatcher<Title> {

    private final Title expectedTitle;

    public TitleMatcher(Title expectedTitle) {
        this.expectedTitle = Objects.requireNonNull(expectedTitle);
    }

    @Override
    public boolean matches(Title argument) {
        return Arrays.equals(argument.getTitle(), expectedTitle.getTitle())
                && Arrays.equals(argument.getSubtitle(), expectedTitle.getSubtitle())
                && argument.getFadeIn() == expectedTitle.getFadeIn()
                && argument.getStay() == expectedTitle.getStay()
                && argument.getFadeOut() == expectedTitle.getFadeOut();
    }
}
