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

package space.arim.api.jsonchat;

import java.util.Objects;

import static space.arim.api.jsonchat.ClickEventInfo.ClickType.OPEN_URL;
import static space.arim.api.jsonchat.ClickEventInfo.ClickType.RUN_COMMAND;
import static space.arim.api.jsonchat.ClickEventInfo.ClickType.SUGGEST_COMMAND;

/**
 * A click event, with a type and value
 *
 */
public final class ClickEventInfo {

    private final ClickType clickType;
    private final CharSequence value;

    /**
     * Creates from a click type and value
     *
     * @param clickType the click type
     * @param value the value
     */
    public ClickEventInfo(ClickType clickType, CharSequence value) {
        this.clickType = Objects.requireNonNull(clickType, "clickType");
        this.value = Objects.requireNonNull(value, "value");
    }

    /**
     * The click type
     *
     * @return the click type
     */
    public ClickType clickType() {
        return clickType;
    }

    /**
     * The click event value
     *
     * @return the value
     */
    public CharSequence value() {
        return value;
    }

    @Override
    public String toString() {
        return "ClickEvent{" +
                "clickType=" + clickType +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickEventInfo that = (ClickEventInfo) o;
        return clickType == that.clickType && value.toString().equals(that.value.toString());
    }

    @Override
    public int hashCode() {
        int result = clickType.hashCode();
        result = 31 * result + value.toString().hashCode();
        return result;
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
    static ClickEventInfo clickEventFromValues(String tag, String value) {
        return new ClickEventInfo(clickTypeFromTag(tag), value);
    }

    static ClickType clickTypeFromTag(String tag) {
        switch (tag) {
        case "cmd":
            return RUN_COMMAND;
        case "sgt":
            return SUGGEST_COMMAND;
        case "url":
            return OPEN_URL;
        default:
            throw new IllegalArgumentException();
        }
    }
}
