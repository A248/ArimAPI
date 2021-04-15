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
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import space.arim.api.jsonchat.adventure.FormattingSerializer;
import space.arim.api.jsonchat.adventure.internal.ComponentIterator;

import java.io.IOException;
import java.util.List;

import static space.arim.api.jsonchat.testing.TestUtil.collect;

public final class FormattingCodeSerializer implements FormattingSerializer {

    private final ComponentSerializer<Component, TextComponent, String> serializer;

    FormattingCodeSerializer(ComponentSerializer<Component, TextComponent, String> serializer) {
        this.serializer = serializer;
    }

    public FormattingCodeSerializer() {
        this(LegacyComponentSerializer.legacyAmpersand());
    }

    @Override
    public List<? extends ComponentLike> readFormatting(String text) {
        return collect(new ComponentIterator(serializer.deserialize(text)));
    }

    @Override
    public void writeFormatting(Iterable<Component> components, Appendable output) throws IOException {
        String serialized = serializer.serialize(TextComponent.ofChildren(
                // Remove children from specified components so that adventure cannot see them
                // FormattingSerializer requires child components to be ignored
                collect(components.iterator()).stream().map((component) -> component.children(List.of()))
                        .toArray(Component[]::new)));
        output.append(serialized);
    }
}
