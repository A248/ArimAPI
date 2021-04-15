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
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static space.arim.api.jsonchat.testing.TestUtil.randomColor;
import static space.arim.api.jsonchat.testing.TestUtil.randomString;

public class ComponentIteratorTest {

    private <T> void assertIteratorsEqual(Iterator<T> iter1, Iterator<T> iter2) {
        while (iter1.hasNext()) {
            assertTrue(iter2.hasNext(), "Iterator1 has more elements than Iterator2");
            assertEquals(iter1.next(), iter2.next(), "Iterator elements do not match");
        }
        assertFalse(iter2.hasNext(), "Iterator2 has more elements than Iterator1");
    }

    private void assertMatches(List<Component> componentsIterated, Component mainComponent) {
        assertIteratorsEqual(componentsIterated.iterator(), new ComponentIterator(mainComponent));
    }

    private Component randomSimpleComponent() {
        var tlr = ThreadLocalRandom.current();
        TextComponent.Builder textBuilder = Component.text()
                .content(randomString())
                .color(randomColor());
        for (TextDecoration decoration : TextDecoration.values()) {
            textBuilder.decoration(decoration, tlr.nextBoolean());
        }
        return textBuilder.build();
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
}
