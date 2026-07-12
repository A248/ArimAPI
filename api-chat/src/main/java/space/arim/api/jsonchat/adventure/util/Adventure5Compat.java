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

package space.arim.api.jsonchat.adventure.util;

import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import space.arim.api.jsonchat.ClickEventInfo;

import java.util.function.Function;

/**
 * A compatibility interface that enables both Adventure 4 and Adventure 5 to be used.
 */
public interface Adventure5Compat {

    /**
     * The "default" implementation uses Adventure 4 methods.
     */
    Adventure5Compat DEFAULT = new Adventure5Compat() {
        @Override
        public @Nullable ComponentBuilder<?, ?> toBuilder(Component component) {
            if (component instanceof BuildableComponent) {
                return ((BuildableComponent<?, ?>) component).toBuilder();
            }
            return null;
        }

        @Override
        public ClickEvent mapClickEventValue(ClickEvent original, Function<? super String, String> stringMap) {
            ClickEvent.Action action = original.action();
            if (action.equals(ClickEvent.Action.CHANGE_PAGE)) {
                return original;
            }
            String oldValue = original.value();
            String newValue = stringMap.apply(oldValue);
            return oldValue.equals(newValue) ? original : ClickEvent.clickEvent(action, newValue);
        }

        @Override
        public String clickEventValue(ClickEvent clickEvent) {
            return clickEvent.value();
        }

        @Override
        public ClickEventInfo.ClickType clickActionToType(ClickEvent.Action action) {
            return switch (action) {
                case OPEN_URL -> ClickEventInfo.ClickType.OPEN_URL;
                case RUN_COMMAND -> ClickEventInfo.ClickType.RUN_COMMAND;
                case SUGGEST_COMMAND -> ClickEventInfo.ClickType.SUGGEST_COMMAND;
                default ->
                        throw new UnsupportedOperationException("Click event action " + action + " is not supported");
            };
        }

        @Override
        public ClickEvent.Action clickTypeToAction(ClickEventInfo.ClickType clickType) {
            return switch (clickType) {
                case RUN_COMMAND -> ClickEvent.Action.RUN_COMMAND;
                case SUGGEST_COMMAND -> ClickEvent.Action.SUGGEST_COMMAND;
                case OPEN_URL -> ClickEvent.Action.OPEN_URL;
            };
        }

        @Override
        public Component join(ComponentLike separator, ComponentLike... components) {
            return Component.join(separator, components);
        }
    };

    /**
     * Converts a component into its builder
     *
     * @param component the component
     * @return the component builder, or null for older versions of Adventure which don't guarantee that Components can
     * be turned into builders and a non-buildable component was passed
     */
    @Nullable ComponentBuilder<?, ?> toBuilder(Component component);

    /**
     * Maps the value of a click event to another, <b>only</b> if the click event value is a string.
     * <p>
     * For the change page action, this may do nothing depending on the Adventure version.
     *
     * @param original the original click event
     * @param stringMap how to map the string value (if the action has a string value)
     * @return the new click event, which could be the same event if no changes would occur
     */
    ClickEvent mapClickEventValue(ClickEvent original, Function<? super String, String> stringMap);

    /**
     * Extracts the string value of a click event. If the value is not a string, uses {@code toString()} on it.
     *
     * @param clickEvent the click event
     * @return the click event value, as a string
     */
    String clickEventValue(ClickEvent clickEvent);

    /**
     * Converts a click event action into a click type (an API subset)
     *
     * @param action the click event action
     * @return the click type
     * @throws UnsupportedOperationException if the action cannot be represented as a click type
     */
    ClickEventInfo.ClickType clickActionToType(ClickEvent.Action action);

    /**
     * Converts a click type (an API subset) into a click event action
     *
     * @param clickType the click type
     * @return the action
     */
    ClickEvent.Action clickTypeToAction(ClickEventInfo.ClickType clickType);

    /**
     * Joins components, similar to Adventure 4's {@code Component.join(ComponentLike, ComponentLike[])} but it
     * might not return a TextComponent.
     *
     * @param separator the separator
     * @param components the component
     * @return the joined result
     */
    Component join(ComponentLike separator, ComponentLike...components);
}
