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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static net.kyori.adventure.text.format.TextColor.color;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static space.arim.api.jsonchat.testing.TestUtil.randomString;

public class ComponentSerializationTest {

    private String colorToString(TextColor color) {
        StringBuilder builder = new StringBuilder();
        ComponentSerialization.appendColor(color.value(), builder);
        return builder.toString();
    }

    @ParameterizedTest
    @ArgumentsSource(NamedTextColorArgumentsProvider.class)
    public void namedColors(NamedTextColor textColor) {
        String message = randomString().replace("&", "");
        assertEquals(
                LegacyComponentSerializer.legacyAmpersand().serialize(Component.text(message, textColor)),
                colorToString(textColor) + message);
    }
    
    @Test
    public void rgbColors() {
        assertEquals("<#0acb08>", colorToString(color(0x0acb08)));
        assertEquals("<#aacbff>", colorToString(color(0xaacbff)));
    }

    @Test
    public void rgbColorsShort() {
        assertEquals("<#0cf>", colorToString(color(0x00ccff)));
        assertEquals("<#1cf>", colorToString(color(0x11ccff)));
    }

}
