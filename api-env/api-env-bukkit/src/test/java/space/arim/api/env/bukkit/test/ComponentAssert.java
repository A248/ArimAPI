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
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public final class ComponentAssert {

    private ComponentAssert() {}

    public static void assertEqualComponents(BaseComponent[] expectedComponents, BaseComponent[] actualComponents) {
        assertEqualComponents(List.of(expectedComponents), List.of(actualComponents));
    }

    private static void assertEqualComponents(List<BaseComponent> expectedComponents, List<BaseComponent> actualComponents) {
        assertEquals(expectedComponents.size(), actualComponents.size(), "Sizes should be equal");
        for (int n = 0; n < expectedComponents.size(); n++) {
            assertEqualComponents(expectedComponents.get(n), actualComponents.get(n));
        }
    }

    public static void assertEqualComponents(BaseComponent expectedComponent, BaseComponent actualComponent) {
        String prefix = "Expected " + expectedComponent + " but was " + actualComponent + ". Reason: ";
        if (expectedComponent instanceof TextComponent expectedTextComponent) {
            if (actualComponent instanceof TextComponent actualTextComponent) {
                assertEquals(expectedTextComponent.getText(), actualTextComponent.getText(), prefix + "Text of components");
            } else {
                fail(prefix + "Actual component is not a text component");
            }
        } else if (actualComponent instanceof TextComponent) {
            fail(prefix + "Expected component is not a text component");
        }
        assertEquals(expectedComponent.getColorRaw(), actualComponent.getColorRaw(), prefix + "Color");

        assertEquals(expectedComponent.isObfuscatedRaw(), actualComponent.isObfuscatedRaw(), prefix + "Obfuscated");
        assertEquals(expectedComponent.isBoldRaw(), actualComponent.isBoldRaw(), prefix + "Bold");
        assertEquals(expectedComponent.isStrikethroughRaw(), actualComponent.isStrikethroughRaw(), prefix + "Strikethrough");
        assertEquals(expectedComponent.isUnderlinedRaw(), actualComponent.isUnderlinedRaw(), prefix + "Underlined");
        assertEquals(expectedComponent.isItalicRaw(), actualComponent.isItalicRaw(), prefix + "Italic");

        assertEquals(expectedComponent.getClickEvent(), actualComponent.getClickEvent(), prefix + "Click event");
        assertEqualHoverEvents(expectedComponent.getHoverEvent(), actualComponent.getHoverEvent());
        assertEquals(expectedComponent.getInsertion(), actualComponent.getInsertion(), prefix + "Insertion");
    }

    private static void assertEqualHoverEvents(HoverEvent expectedEvent, HoverEvent actualEvent) {
        if (expectedEvent == null) {
            assertNull(actualEvent);
            return;
        } else if (actualEvent == null) {
            fail(expectedEvent + " is non-null");
            return;
        }
        assertEquals(expectedEvent.getAction(), actualEvent.getAction());
        assertEqualComponents(expectedEvent.getValue(), actualEvent.getValue());
    }
}
