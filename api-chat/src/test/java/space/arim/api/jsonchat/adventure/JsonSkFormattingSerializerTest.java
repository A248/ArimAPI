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

package space.arim.api.jsonchat.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static space.arim.api.jsonchat.testing.AdventureUtil.text;
import static space.arim.api.jsonchat.testing.AdventureUtil.toComponentList;

public class JsonSkFormattingSerializerTest {

    private final FormattingSerializer formattingSerializer = new JsonSkFormattingSerializer();

    private void assertRoundTrip(String text, Component...components) {
        List<Component> componentList = List.of(components);

        List<Component> readFormatting = toComponentList(formattingSerializer.readFormatting(text));
        verifyContractOfReadFormatting(readFormatting);
        assertEquals(componentList, readFormatting);

        assertEquals(text, formattingSerializer.writeFormatting(componentList).toString());
    }

    private static void verifyContractOfReadFormatting(List<Component> readFormatting) {
        for (Component verifyContract : readFormatting) {
            assertTrue(verifyContract.children().isEmpty());
            assertNull(verifyContract.hoverEvent());
            assertNull(verifyContract.clickEvent());
            assertNull(verifyContract.insertion());
        }
    }

    @Test
    public void simpleText() {
        assertRoundTrip("simple text", text("simple text"));
    }

    private static TextDecoration decorationFromLetter(String letter) {
        return switch (letter) {
            case "k" -> TextDecoration.OBFUSCATED;
            case "l" -> TextDecoration.BOLD;
            case "m" -> TextDecoration.STRIKETHROUGH;
            case "n" -> TextDecoration.UNDERLINED;
            case "o" -> TextDecoration.ITALIC;
            default -> throw new IllegalArgumentException();
        };
    }

    @ParameterizedTest
    @ValueSource(strings = {"k", "l", "m", "n", "o"})
    public void formattingCodes(String styleLetter) {
        assertRoundTrip("some &atext &ewith &c&" + styleLetter + "colors &bis annoying&f unless reset &7in fire",
                text("some "), text("text ", GREEN), text("with ", YELLOW),
                text("colors ", style(RED, decorationFromLetter(styleLetter))), text("is annoying", style(AQUA)),
                text(" unless reset "), text("in fire", GRAY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"k", "l", "m", "n", "o"})
    public void hexAndFormattingCodes(String styleLetter) {
        assertRoundTrip("some &atext <#cc5941>with &c&" + styleLetter + "formatting " +
                        "<#152aa6>is annoying&f unless reset &7in fire",
                text("some "), text("text ", GREEN), text("with ", color(0xcc5941)),
                text("formatting ", style(RED, decorationFromLetter(styleLetter))),
                text("is annoying", style(color(0x152aa6))), text(" unless reset "), text("in fire", GRAY));
    }

}
