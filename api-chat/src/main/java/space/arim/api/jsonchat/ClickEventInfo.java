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

package space.arim.api.jsonchat;

import java.util.Objects;

import static space.arim.api.jsonchat.ClickEventInfo.ClickType.OPEN_URL;
import static space.arim.api.jsonchat.ClickEventInfo.ClickType.RUN_COMMAND;
import static space.arim.api.jsonchat.ClickEventInfo.ClickType.SUGGEST_COMMAND;

/**
 * A click event, with a type and value
 *
 */
public record ClickEventInfo(ClickType clickType, String value) {

    public ClickEventInfo {
        Objects.requireNonNull(clickType, "clickType");
        Objects.requireNonNull(value, "value");
    }

    /**
     * Click types supported
     *
     */
    public enum ClickType {

        /**
         * Runs a command. The value should usually include a starting "/"
         */
        RUN_COMMAND,
        /**
         * Suggests text to be entered into the chat prompt
         */
        SUGGEST_COMMAND,
        /**
         * Opens the specified URL
         */
        OPEN_URL
    }

    // Used for testing purposes
    static ClickEventInfo newFrom(String tag, String value) {
        ClickType clickType = switch (tag) {
            case "cmd" -> RUN_COMMAND;
            case "sgt" -> SUGGEST_COMMAND;
            case "url" -> OPEN_URL;
            default -> throw new IllegalArgumentException();
        };
        return new ClickEventInfo(clickType, value);
    }
}
