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

package space.arim.api.env.bukkit.test;

import net.md_5.bungee.api.chat.BaseComponent;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.opentest4j.AssertionFailedError;

import java.util.Arrays;
import java.util.Objects;

/**
 * BaseComponent does not implement equals and hashCode in the version of bungeechat
 * which is compiled against
 */
public final class BaseComponentMatcher implements ArgumentMatcher<BaseComponent> {

    private final BaseComponent expectedComponent;

    public BaseComponentMatcher(BaseComponent expectedComponent) {
        this.expectedComponent = Objects.requireNonNull(expectedComponent);
    }

    public static BaseComponent[] fixEquals(BaseComponent[] expectedComponents) {
        return Arrays.stream(expectedComponents).map(BaseComponentMatcher::new).map(Mockito::argThat).toArray(BaseComponent[]::new);
    }

    @Override
    public boolean matches(BaseComponent argument) {
        try {
            ComponentAssert.assertEqualComponents(expectedComponent, argument);
            return true;
        } catch (AssertionFailedError ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "BaseComponentMatcher{" +
                "expectedComponent=" + expectedComponent +
                '}';
    }
}
