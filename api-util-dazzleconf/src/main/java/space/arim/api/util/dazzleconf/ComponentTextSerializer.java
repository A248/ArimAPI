/*
 * ArimAPI
 * Copyright © 2026 Anand Beh
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
import space.arim.api.jsonchat.adventure.util.Adventure5Compat;
import space.arim.api.jsonchat.adventure.util.ComponentText;
import space.arim.api.jsonchat.adventure.util.TextGoal;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

import java.util.Set;

/**
 * A configuration serializer for {@link ComponentText} instances. <br>
 * <br>
 * This serializer depends on the presence of another configuration serializer which is
 * capable of handling {@code Component} values. (For example, {@link ChatMessageSerializer}
 * meets this requirement.) If this is not the case, deserialization and serialization
 * will
 *
 */
public final class ComponentTextSerializer implements ValueSerialiser<ComponentText> {

    private final Set<TextGoal> goals;
    private final Adventure5Compat adventure5Compat;

    /**
     * Creates from the given text goals. Produced {@code ComponentText} instances
     * will have these goals set for them.
     *
     * @param goals the text goals to target
     * @param adventure5Compat adventure 5 compatibility layer
     */
    public ComponentTextSerializer(Set<TextGoal> goals, Adventure5Compat adventure5Compat) {
        this.goals = Set.copyOf(goals);
        this.adventure5Compat = adventure5Compat;
    }

    /**
     * Creates using all text goals
     *
     * @param adventure5Compat adventure 5 compatibility layer
     */
    public ComponentTextSerializer(Adventure5Compat adventure5Compat) {
        this(TextGoal.allGoals(), adventure5Compat);
    }

    @Override
    public Class<ComponentText> getTargetClass() {
        return ComponentText.class;
    }

    @Override
    public ComponentText deserialise(FlexibleType flexibleType) throws BadValueException {
        return ComponentText.create(flexibleType.getObject(Component.class), goals, adventure5Compat);
    }

    @Override
    public Object serialise(ComponentText value, Decomposer decomposer) {
        return decomposer.decompose(Component.class, value.asComponent());
    }
}
