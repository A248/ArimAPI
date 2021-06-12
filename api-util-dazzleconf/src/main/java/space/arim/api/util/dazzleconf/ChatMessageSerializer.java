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

package space.arim.api.util.dazzleconf;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import space.arim.dazzleconf.annote.ConfSerialisers;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

import java.util.Objects;

/**
 * A configuration serializer using a {@link ComponentSerializer} to serialize to
 * and from string values. <br>
 * <br>
 * The backing component serializer's {@code deserialise} implementation should
 * not usually throw exceptions, but this is not a hard requirement. <br>
 * <br>
 * Since this serializer does not provide a {@code getInstance} method or a default
 * constructor, it cannot be used with {@link ConfSerialisers}
 *
 */
public final class ChatMessageSerializer implements ValueSerialiser<Component> {

    private final ComponentSerializer<Component, ? extends Component, String> adventureSerializer;

    /**
     * Creates from the adventure component serializer to use.
     *
     * @param adventureSerializer the component serializer to use
     */
    public ChatMessageSerializer(ComponentSerializer<Component, ? extends Component, String> adventureSerializer) {
        this.adventureSerializer = Objects.requireNonNull(adventureSerializer, "adventureSerializer");
    }

    @Override
    public Class<Component> getTargetClass() {
        return Component.class;
    }

    @Override
    public Component deserialise(FlexibleType flexibleType) throws BadValueException {
        return adventureSerializer.deserialize(flexibleType.getString());
    }

    @Override
    public Object serialise(Component value, Decomposer decomposer) {
        return adventureSerializer.serialize(value);
    }
}
