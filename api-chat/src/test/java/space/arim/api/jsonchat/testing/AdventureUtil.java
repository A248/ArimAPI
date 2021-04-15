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

package space.arim.api.jsonchat.testing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.Style.style;

public final class AdventureUtil {

    private AdventureUtil() {}

    public static List<Component> toComponentList(List<? extends ComponentLike> componentLikes) {
        return componentLikes.stream().map(ComponentLike::asComponent).toList();
    }

    public static Style plainStyle() {
        return style().color(WHITE).build();
    }

    public static Component text(String text) {
        return Component.text(text, plainStyle());
    }

    public static Component text(String text, TextColor color) {
        return Component.text(text, plainStyle().color(color));
    }

    public static Component text(String text, Style style) {
        return Component.text(text, style.merge(plainStyle(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
    }

    public static TextComponent.Builder textBuilder() {
        return Component.text().style(plainStyle());
    }

}
