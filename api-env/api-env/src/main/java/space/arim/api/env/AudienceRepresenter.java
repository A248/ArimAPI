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

package space.arim.api.env;

import net.kyori.adventure.audience.Audience;
import space.arim.api.env.annote.PlatformCommandSender;

/**
 * Allows representing a platform specific command sender as a {@code Audience}
 *
 * @param <C> the command sender type
 */
public interface AudienceRepresenter<@PlatformCommandSender C> {

    /**
     * Provides an adventure for the given command sender
     *
     * @param commandSender the command sender
     * @return the adventure
     */
    Audience toAudience(C commandSender);

    /**
     * Obtains an audience representer for a command sender type
     * which is known to already be an {@code Audience}.
     *
     * @param <C> the command sender type
     * @return the audience representer, which simply passes through the audience
     */
    static <C extends Audience> AudienceRepresenter<C> identity() {
        return (audience) -> audience;
    }

}
