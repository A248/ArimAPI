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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static space.arim.api.jsonchat.testing.AdventureUtil.randomSimpleComponent;
import static space.arim.api.jsonchat.testing.TestUtil.assertIteratorsEqual;
import static space.arim.api.jsonchat.testing.TestUtil.mapList;

public class ComponentTextIteratorTest {

    private void assertMatches(List<String> texts, Component mainComponent) {
        assertIteratorsEqual(
                texts.iterator(),
                ComponentText.create(mainComponent).iter());
    }

    @Test
    public void simpleComponent() {
        var component = randomSimpleComponent();
        assertMatches(List.of(component.content()), component);
    }

    @Test
    public void someExtras() {
        TextComponent[] extras = new TextComponent[] {
                randomSimpleComponent(), randomSimpleComponent()};
        TextComponent main = Component.text().append(extras).build();

        assertMatches(List.of(main.content(), extras[0].content(), extras[1].content()), main);
    }

    @Test
    public void nestingPlusPlus() {
        TextComponent[] deepExtrasOne = new TextComponent[] {
                randomSimpleComponent(), randomSimpleComponent()};
        TextComponent[] deepExtrasTwo = new TextComponent[] {
                randomSimpleComponent(), randomSimpleComponent(), randomSimpleComponent()};
        TextComponent extraOne = randomSimpleComponent().children(List.of(deepExtrasOne));
        TextComponent extraTwo = randomSimpleComponent().children(List.of(deepExtrasTwo));
        TextComponent[] extras = new TextComponent[] {extraOne, extraTwo};
        TextComponent main = randomSimpleComponent().children(List.of(extras));

        assertMatches(
                mapList(List.of(main, extraOne, deepExtrasOne[0], deepExtrasOne[1],
                        extraTwo, deepExtrasTwo[0], deepExtrasTwo[1], deepExtrasTwo[2]),
                        TextComponent::content),
                main);
    }

    @Test
    public void nestingWithInheritance() {
        TextComponent[] deepExtrasOne = new TextComponent[] {
                randomSimpleComponent(), randomSimpleComponent()};
        ClickEvent deepExtrasTwoOverridingClick = ClickEvent.suggestCommand("/suggestion");
        TextComponent[] deepExtrasTwo = new TextComponent[] {
                randomSimpleComponent().clickEvent(deepExtrasTwoOverridingClick),
                randomSimpleComponent().clickEvent(deepExtrasTwoOverridingClick),
                randomSimpleComponent().clickEvent(deepExtrasTwoOverridingClick)};

        HoverEvent<?> inheritedHover = HoverEvent.showText(Component.text("hover"));
        TextComponent extraOne = randomSimpleComponent().children(List.of(deepExtrasOne)).hoverEvent(inheritedHover);

        // This ClickEvent is not inherited because child components override it
        ClickEvent uninheritedClick = ClickEvent.runCommand("/say hello");
        TextComponent extraTwo = randomSimpleComponent().children(List.of(deepExtrasTwo)).clickEvent(uninheritedClick);

        TextComponent[] extras = new TextComponent[] {extraOne, extraTwo};
        String insertion = "insert me";
        TextComponent main = randomSimpleComponent().children(List.of(extras)).insertion(insertion);

        assertMatches(
                List.of(main.content(), insertion,
                        extraOne.content(), "hover", insertion,
                        deepExtrasOne[0].content(), "hover", insertion,
                        deepExtrasOne[1].content(), "hover", insertion,
                        extraTwo.content(), "/say hello", insertion,
                        deepExtrasTwo[0].content(), "/suggestion", insertion,
                        deepExtrasTwo[1].content(), "/suggestion", insertion,
                        deepExtrasTwo[2].content(), "/suggestion", insertion),
                main);
    }

}
