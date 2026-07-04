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
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import space.arim.api.jsonchat.ClickEventInfo;

/**
 * Compatibility with Adventure 5 APIs.
 *
 */
public interface Adventure5Compat {

    Adventure5Compat DEFAULT = new Adventure5Compat() {};

    default @Nullable ComponentBuilder<?, ?> toBuilder(Component component) {
        if (component instanceof BuildableComponent) {
            return ((BuildableComponent<?, ?>) component).toBuilder();
        }
        return null;
    }

    default ClickEvent clickEvent(ClickEvent.Action action, String value) {
        return ClickEvent.clickEvent(action, value);
    }

    default String clickEventValue(ClickEvent clickEvent) {
        return clickEvent.value();
    }

    default ClickEventInfo.ClickType clickActionToType(ClickEvent.Action action) {
        switch (action) {
            case OPEN_URL:
                return ClickEventInfo.ClickType.OPEN_URL;
            case RUN_COMMAND:
                return ClickEventInfo.ClickType.RUN_COMMAND;
            case SUGGEST_COMMAND:
                return ClickEventInfo.ClickType.SUGGEST_COMMAND;
            case OPEN_FILE:
            case CHANGE_PAGE:
            case COPY_TO_CLIPBOARD:
            default:
                throw new UnsupportedOperationException("Click event action " + action + " is not supported");
        }
    }

    default ClickEvent.Action clickTypeToAction(ClickEventInfo.ClickType clickType) {
        switch (clickType) {
            case RUN_COMMAND:
                return ClickEvent.Action.RUN_COMMAND;
            case SUGGEST_COMMAND:
                return ClickEvent.Action.SUGGEST_COMMAND;
            case OPEN_URL:
                return ClickEvent.Action.OPEN_URL;
            default:
                throw new IllegalStateException("Unknown click type " + clickType);
        }
    }

    default TextComponent textOfChildren(ComponentLike...components) {
        return TextComponent.ofChildren(components);
    }

    default Component join(ComponentLike separator, ComponentLike...components) {
        return Component.join(separator, components);
    }
}
