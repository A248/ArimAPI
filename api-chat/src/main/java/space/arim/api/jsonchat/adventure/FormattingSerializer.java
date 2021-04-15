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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

/**
 * A serializer of colors and styles to and from {@code Component}s
 * and strings. <br>
 * <br>
 * Implementations typically use a specific color formatting scheme. For example,
 * {@literal '&'} formatting codes are one such scheme. <br>
 * <br>
 * <b>Warning: This is a low-level interface whose contracts must be observed closely.</b>
 * Otherwise, surprising behavior may occur arising from interactions with calling code.
 * Failing to adhere to the specification will lead to bugs.
 */
public interface FormattingSerializer {

    /**
     * Reads colored and styled text as components. Creates components in a linear fashion,
     * with no implied relationship between one two another. <br>
     * <br>
     * <b>Requirements</b> <br>
     * No component returned should have children components ({@code component.children()}).
     * Rather, each component should correspond to a segment of the text with a certain combination
     * of color and styling. <br>
     * <br>
     * There should be no hover events, click events, or insertions on any of the returned
     * components. Their presence may indicate a conceptual error or implementation issue.
     *
     * @param text the text, which may be interpreted using the color formatting scheme
     * @return the colored components, which may be unfinished builders.
     */
    List<? extends ComponentLike> readFormatting(String text);

    /**
     * Writes components as colored and styled text. Should be the inverse operation
     * of {@link #readFormatting(String)}. <br>
     * <br>
     * The implementor should ignore any child components on any component objects.
     * Rather, the implementor should only use components provided in the {@code Iterable}.
     * The caller is responsible for populating the {@code Iterable} with the desired components. <br>
     * <br>
     * Some (possibly most) implementations will support only {@code TextComponent}. Where
     * such an implementation encounters another kind of component, it is free to take whatever
     * course of action, whether that is omitting the component or producing dummy text.
     *
     * @param components the components. It is suggested, but not required that the implementor
     *                   traverse this only once, as traversal may be an expensive operation.
     * @param output the output buffer
     * @throws IOException only if the underlying {@code output} did so, and for no other reason.
     */
    void writeFormatting(Iterable<Component> components, Appendable output) throws IOException;

    /**
     * Writes components as colored and styled text. Should be the inverse operation
     * of {@link #readFormatting(String)}. <br>
     * <br>
     * This is a convenience method for {@link #writeFormatting(Iterable, Appendable)}
     *
     * @param components the components
     * @return the colored and styled text, which may use the color formatting scheme
     */
    default CharSequence writeFormatting(Iterable<Component> components) {
        StringBuilder output = new StringBuilder();
        try {
            writeFormatting(components, output);
        } catch (IOException ex) {
            throw new UncheckedIOException("Shouldn't happen - broken implementation", ex);
        }
        return output;
    }

}
