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
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;

import java.io.IOException;
import java.util.List;

import static space.arim.api.jsonchat.testing.AdventureUtil.text;

public final class PlainTextFormattingSerializer implements FormattingSerializer {

    @Override
    public List<? extends ComponentLike> readFormatting(String text) {
        return List.of(text(text));
    }

    @Override
    public void writeFormatting(Iterable<Component> components, Appendable output) throws IOException {
        for (Component component : components) {
            if (component instanceof TextComponent tc) {
                output.append(tc.content());
            }
        }
    }

}
