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

package space.arim.api.env.bukkit;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;

/**
 * BaseComponent does not implement equals and hashCode in the version of bungeechat
 * which is compiled against
 */
public final class BaseComponentMatcher implements ArgumentMatcher<BaseComponent> {

    private final BaseComponent expectedComponent;

    public BaseComponentMatcher(BaseComponent expectedComponent) {
        this.expectedComponent = Objects.requireNonNull(expectedComponent);
    }

    public static BaseComponent fixEqualsHashCode(BaseComponent expectedComponent) {
        return argThat(new BaseComponentMatcher(expectedComponent));
    }

    public static BaseComponent[] fixEqualsHashCode(BaseComponent[] expectedComponents) {
        return Arrays.stream(expectedComponents).map(BaseComponentMatcher::fixEqualsHashCode).toArray(BaseComponent[]::new);
    }

    @Override
    public boolean matches(BaseComponent argument) {
        if (expectedComponent instanceof TextComponent textComponent) {
            if (!(argument instanceof TextComponent textArgument)
                    || !Objects.equals(textComponent.getText(), textArgument.getText())) {
                return false;
            }
        }
        return Objects.equals(expectedComponent.isObfuscatedRaw(), argument.isObfuscatedRaw())
                && Objects.equals(expectedComponent.isBoldRaw(), argument.isBoldRaw())
                && Objects.equals(expectedComponent.isStrikethroughRaw(), argument.isStrikethroughRaw())
                && Objects.equals(expectedComponent.isUnderlinedRaw(), argument.isUnderlinedRaw())
                && Objects.equals(expectedComponent.isItalicRaw(), argument.isItalicRaw())
                && Objects.equals(expectedComponent.getClickEvent(), argument.getClickEvent())
                && hoverEventsEqual(expectedComponent.getHoverEvent(), argument.getHoverEvent())
                && Objects.equals(expectedComponent.getInsertion(), argument.getInsertion());
    }

    private static boolean hoverEventsEqual(HoverEvent hoverEvent1, HoverEvent hoverEvent2) {
        if (hoverEvent1 == null) {
            return hoverEvent2 == null;
        }
        if (hoverEvent1.getAction() != hoverEvent2.getAction()) {
            return false;
        }
        BaseComponent[] value1Components = hoverEvent1.getValue();
        BaseComponent[] value2Components = hoverEvent2.getValue();
        if (value1Components.length != value2Components.length) {
            return false;
        }
        for (int n = 0; n < value1Components.length; n++) {
            BaseComponent value1 = value1Components[n];
            BaseComponent value2 = value2Components[n];
            if (value1 == null) {
                if (value2 != null) {
                    return false;
                }
            } else {
                if (value2 == null) {
                    return false;
                }
                if (!new BaseComponentMatcher(value1).matches(value2)) {
                    return false;
                }
            }
        }
        return true;
    }
}
