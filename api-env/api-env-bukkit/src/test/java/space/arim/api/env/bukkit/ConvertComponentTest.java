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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import space.arim.api.env.bukkit.test.ComponentAssert;

import java.util.List;

public class ConvertComponentTest {

    private void assertConversion(Component component, BaseComponent bungeeEquivalent) {
        ComponentAssert.assertEqualComponents(
                new BaseComponent[] {bungeeEquivalent}, PlayerAudience.convertComponent(component));
    }

    @Test
    public void simpleText() {
        String text = "simple";
        TextComponent expectedComponent = new TextComponent(text);
        assertConversion(Component.text(text), expectedComponent);
    }

    @Test
    public void coloredText() {
        String text = "colored";
        TextComponent expectedComponent = new TextComponent(text);
        expectedComponent.setColor(ChatColor.DARK_PURPLE);
        assertConversion(Component.text(text, NamedTextColor.DARK_PURPLE), expectedComponent);
    }

    private void setDecoration(TextComponent textComponent, TextDecoration decoration) {
        switch (decoration) {
        case OBFUSCATED -> textComponent.setObfuscated(true);
        case BOLD -> textComponent.setBold(true);
        case STRIKETHROUGH -> textComponent.setStrikethrough(true);
        case UNDERLINED -> textComponent.setUnderlined(true);
        case ITALIC -> textComponent.setItalic(true);
        }
    }

    @ParameterizedTest
    @EnumSource
    public void styledText(TextDecoration decoration) {
        String text = "styled";
        TextComponent expectedComponent = new TextComponent(text);
        setDecoration(expectedComponent, decoration);
        assertConversion(Component.text(text, Style.style(decoration)), expectedComponent);
    }

    @ParameterizedTest
    @EnumSource
    public void coloredAndStyledText(TextDecoration decoration) {
        String text = "coloredAndStyled";
        TextComponent expectedComponent = new TextComponent(text);
        expectedComponent.setColor(ChatColor.DARK_PURPLE);
        setDecoration(expectedComponent, decoration);
        assertConversion(Component.text(text, Style.style(NamedTextColor.DARK_PURPLE, decoration)), expectedComponent);
    }

    @Test
    public void multipleComponents() {
        String textOne = "textOne";
        String textTwo = "textTwo";
        TextComponent expectedComponent = new TextComponent("");
        TextComponent expectedComponentOne = new TextComponent(textOne);
        TextComponent expectedComponentTwo = new TextComponent(textTwo);
        expectedComponentTwo.setColor(ChatColor.DARK_PURPLE);
        expectedComponent.setExtra(List.of(expectedComponentOne, expectedComponentTwo));
        assertConversion(
                net.kyori.adventure.text.TextComponent.ofChildren(
                        Component.text(textOne), Component.text(textTwo, NamedTextColor.DARK_PURPLE)),
                expectedComponent);
    }
    
    @Test
    public void multipleComponentsWithEvents() {
        String textOne = "textOne";
        String textTwo = "textTwo";
        String hoverText = "hover text";
        TextComponent expectedComponent = new TextComponent("");
        TextComponent expectedComponentOne = new TextComponent(textOne);
        TextComponent expectedComponentTwo = new TextComponent(textTwo);
        expectedComponentOne.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {new TextComponent(hoverText)}));
        expectedComponentTwo.setColor(ChatColor.DARK_PURPLE);
        expectedComponent.setExtra(List.of(expectedComponentOne, expectedComponentTwo));
        assertConversion(
                net.kyori.adventure.text.TextComponent.ofChildren(
                        Component.text().content(textOne).hoverEvent(HoverEvent.showText(Component.text(hoverText))).build(),
                        Component.text(textTwo, NamedTextColor.DARK_PURPLE)),
                expectedComponent);
    }
}
