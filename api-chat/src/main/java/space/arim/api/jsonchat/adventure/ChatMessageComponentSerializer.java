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
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import space.arim.api.jsonchat.ChatMessageParser;
import space.arim.api.jsonchat.ChatMessagePart;
import space.arim.api.jsonchat.ChatMessageReconstitutor;
import space.arim.api.jsonchat.adventure.internal.AddedFunctionalityPeekingIterator;
import space.arim.api.jsonchat.adventure.internal.ComponentIterator;
import space.arim.api.jsonchat.adventure.internal.ComponentToMessagePartIterator;

import java.util.Iterator;
import java.util.Objects;

/**
 * A component serializer which converts chat messages to and from the
 * modified JSON.sk format. <br>
 * <br>
 * <b>The Modified JSON.sk format</b> <br>
 * Unlike the original JSON.sk format, the modified format adds escaping features.
 * The "nil:" tag signifies a plain segment and allows text such as "nil:ttp:hello"
 * which is displayed as "ttp:hello". Escaping double pipes is done by doubling
 * the double pipes, to make quadruple pipes: "||||" is displayed as "||". <br>
 * <br>
 * Like the original format, the modified format uses {@literal '&'} formatting codes.
 * See {@link JsonSkFormattingSerializer} for more information and syntax. <br>
 * <br>
 * {@code JsonSkFormattingSerializer} is used by default but may be changed by using
 * {@link #ChatMessageComponentSerializer(FormattingSerializer)} instead of the default constructor.
 */
public final class ChatMessageComponentSerializer implements ComponentSerializer<Component, Component, String> {

    private final FormattingSerializer formattingSerializer;

    /**
     * Creates from a given formatting serializer, which may be used to change
     * the mechanism for parsing colors and styling in simple text. <br>
     * <br>
     * Note that a different formatting serializer will change the output
     * of colored and styled text, deviating from JSON.sk's precedent of
     * {@literal '&'} formatting codes.
     *
     * @param formattingSerializer the formatting serializer
     */
    public ChatMessageComponentSerializer(FormattingSerializer formattingSerializer) {
        this.formattingSerializer = Objects.requireNonNull(formattingSerializer, "formattingSerializer");
    }

    /**
     * Creates a serializer using the default parsing for colors and styles.
     *
     */
    public ChatMessageComponentSerializer() {
        this(new JsonSkFormattingSerializer());
    }

    /**
     * Parses a JSON.sk format string as a {@code Component}. The returned Component
     * will be empty except for the fact that it will have several child components,
     * each of which corresponds to a segment of the JSON message.
     *
     * @param input the JSON.sk format string
     * @return the {@code Component}
     */
    @Override
    public @NonNull Component deserialize(@NonNull String input) {
        ComponentVisitor visitor = new ComponentVisitor(formattingSerializer);
        new ChatMessageParser(visitor, input).parse();
        return visitor.buildResult();
    }

    /**
     * Reconstitutes a JSON.sk format string from a {@code Component}. Will re-formulate
     * any escape sequences which may have been used to create the original component. <br>
     * <br>
     * This is the inverse operation of {@link #deserialize(String)}. <br>
     * <br>
     * Note that, for {@code Component}s obtained through other means, care must be taken
     * to ensure the component does not use any unsupported features.
     *
     * @param component the componet
     * @return the JSON.sk format string
     * @throws UnsupportedOperationException if the component uses an adventure feature
     * not representable in the JSON.sk format
     */
    @Override
    public @NonNull String serialize(@NonNull Component component) {
        var reconstitutor = new ChatMessageReconstitutor();
        Iterator<ChatMessagePart> messagePartIterator =
                new ComponentToMessagePartIterator(
                        new AddedFunctionalityPeekingIterator<>(new ComponentIterator(component)),
                        formattingSerializer
                );
        reconstitutor.writeAllMessageParts(messagePartIterator);
        return reconstitutor.reconstitute();
    }
}
