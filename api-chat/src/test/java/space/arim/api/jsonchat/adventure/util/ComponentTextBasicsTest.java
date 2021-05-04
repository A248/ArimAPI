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
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComponentTextBasicsTest {

    @Test
    public void badConstruction() {
        assertThrows(NullPointerException.class, () -> ComponentText.create(null));
        assertThrows(NullPointerException.class,
                () -> ComponentText.create(Component.empty(), (Set<TextGoal>) null));
        assertThrows(NullPointerException.class, () -> ComponentText.create(null));
        assertThrows(NullPointerException.class,
                () -> ComponentText.create(Component.empty(), Collections.singleton(null)));
    }

    @Test
    public void badArguments() {
        ComponentText componentText = ComponentText.create(Component.empty());
        assertThrows(NullPointerException.class, () -> componentText.contains(null));
        assertThrows(NullPointerException.class, () -> componentText.replaceText("a", null));
        assertThrows(NullPointerException.class, () -> componentText.replaceText((CharSequence) null, "b"));
        assertThrows(NullPointerException.class, () -> componentText.replaceText(Pattern.compile("a"), null));
        assertThrows(NullPointerException.class, () -> componentText.replaceText((Pattern) null, "b"));
    }
}
