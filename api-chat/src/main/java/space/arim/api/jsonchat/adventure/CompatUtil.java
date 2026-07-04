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

package space.arim.api.jsonchat.adventure;

import net.kyori.adventure.text.event.ClickEvent;
import space.arim.api.jsonchat.ClickEventInfo;

final class CompatUtil {

    private CompatUtil() {}

    static ClickEvent clickEvent(ClickEventInfo.ClickType clickType, String value) {
        switch (clickType) {
            case RUN_COMMAND:
                return ClickEvent.runCommand(value);
            case SUGGEST_COMMAND:
                return ClickEvent.suggestCommand(value);
            case OPEN_URL:
                return ClickEvent.openUrl(value);
            default:
                throw new IncompatibleClassChangeError("Unknown " + clickType);
        }
    }
}
