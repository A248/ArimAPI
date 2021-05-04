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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static space.arim.api.jsonchat.testing.TestUtil.randomString;

public class ComponentTextReplacementTest {

    private ComponentText componentText;

    private TextComponent.Builder builder(Function<String, String> operator) {
        String content = operator.apply("Some content with %ONE% really %TWO% variables");
        String hoverValue = operator.apply("Contains %ONE%");
        String clickValue = operator.apply("As well as %THREE%");
        String insertion = operator.apply("%TWO% again");
        return Component.text()
                .content(content)
                .hoverEvent(HoverEvent.showText(Component.text(hoverValue)))
                .clickEvent(ClickEvent.runCommand(clickValue))
                .insertion(insertion);
    }

    private TextComponent.Builder childComponent(Function<String, String> operator) {
        String childContent = operator.apply("A %FOUR%th variable");
        String childHoverValue = operator.apply("Back to %ONE%");
        return Component.text()
                .content(childContent)
                .hoverEvent(HoverEvent.showText(Component.text(childHoverValue)));
    }

    private Component component(Function<String, String> operator) {
        return builder(operator).append(childComponent(operator)).build();
    }

    @BeforeEach
    public void setup() {
        componentText = ComponentText.create(component(Function.identity()));
    }

    private String cleanRandomString() {
        return randomString().replace("%", "");
    }

    @Test
    public void replaceText() {
        String replacement1 = cleanRandomString();
        String replacement2 = cleanRandomString();
        String replacement3 = cleanRandomString();
        String replacement4 = cleanRandomString();
        Function<String, String> operator = (str) -> {
            return str.replace("%ONE%", replacement1).replace("%TWO%", replacement2)
                    .replace("%THREE%", replacement3).replace("%FOUR%", replacement4);
        };
        assertEquals(component(operator), componentText.replaceText(operator));
    }
}
