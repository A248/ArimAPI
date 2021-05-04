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
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static space.arim.api.jsonchat.testing.AdventureUtil.randomSimpleComponent;
import static space.arim.api.jsonchat.testing.TestUtil.assertIteratorsEqual;
import static space.arim.api.jsonchat.testing.TestUtil.mapList;

public class ComponentIteratorTest {

    private void assertMatches(List<Component> componentsIterated, Component mainComponent) {
        assertIteratorsEqual(componentsIterated.iterator(), new ComponentIterator(mainComponent));
    }

    @Test
    public void simpleComponent() {
        var component = randomSimpleComponent();
        assertMatches(List.of(component), component);
    }

    @Test
    public void someExtras() {
        Component[] extras = new Component[] {
                randomSimpleComponent(), randomSimpleComponent()};
        Component main = Component.text().append(extras).build();

        assertMatches(List.of(main, extras[0], extras[1]), main);
    }

    @Test
    public void nestingPlusPlus() {
        Component[] deepExtrasOne = new Component[] {
                randomSimpleComponent(), randomSimpleComponent()};
        Component[] deepExtrasTwo = new Component[] {
                randomSimpleComponent(), randomSimpleComponent(), randomSimpleComponent()};
        Component extraOne = randomSimpleComponent().children(List.of(deepExtrasOne));
        Component extraTwo = randomSimpleComponent().children(List.of(deepExtrasTwo));
        Component[] extras = new Component[] {extraOne, extraTwo};
        Component main = randomSimpleComponent().children(List.of(extras));

        assertMatches(
                List.of(main, extraOne, deepExtrasOne[0], deepExtrasOne[1],
                        extraTwo, deepExtrasTwo[0], deepExtrasTwo[1], deepExtrasTwo[2]),
                main);
    }

    @Test
    public void nestingWithInheritance() {
        Component[] deepExtrasOne = new Component[] {
                randomSimpleComponent(), randomSimpleComponent()};
        ClickEvent deepExtrasTwoOverridingClick = ClickEvent.suggestCommand("/suggestion");
        Component[] deepExtrasTwo = new Component[] {
                randomSimpleComponent().clickEvent(deepExtrasTwoOverridingClick),
                randomSimpleComponent().clickEvent(deepExtrasTwoOverridingClick),
                randomSimpleComponent().clickEvent(deepExtrasTwoOverridingClick)};

        HoverEvent<?> inheritedHover = HoverEvent.showText(randomSimpleComponent());
        Component extraOne = randomSimpleComponent().children(List.of(deepExtrasOne)).hoverEvent(inheritedHover);

        // This ClickEvent is not inherited because child components override it
        ClickEvent uninheritedClick = ClickEvent.runCommand("/say hello");
        Component extraTwo = randomSimpleComponent().children(List.of(deepExtrasTwo)).clickEvent(uninheritedClick);

        Component[] extras = new Component[] {extraOne, extraTwo};
        String everyonesInsertion = "insert me";
        Component main = randomSimpleComponent().children(List.of(extras)).insertion(everyonesInsertion);

        assertMatches(
                mapList(List.of(main, extraOne,
                        deepExtrasOne[0].hoverEvent(inheritedHover), deepExtrasOne[1].hoverEvent(inheritedHover),
                        extraTwo, deepExtrasTwo[0], deepExtrasTwo[1], deepExtrasTwo[2]),
                        (component) -> component.insertion(everyonesInsertion)),
                main);
    }
}
