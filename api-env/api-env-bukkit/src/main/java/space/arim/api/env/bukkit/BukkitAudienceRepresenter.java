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

package space.arim.api.env.bukkit;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.arim.api.env.AudienceRepresenter;

import java.util.Objects;

/**
 * Implementation of {@code AudienceRepresenter} for the Bukkit platform. Will use
 * Paper's Adventure support if possible. Otherwise converts to the bungeecord component
 * API before sending
 *
 */
public final class BukkitAudienceRepresenter implements AudienceRepresenter<CommandSender> {

    @Override
    public Audience toAudience(CommandSender commandSender) {
        if (commandSender instanceof Audience) {
            return (Audience) commandSender;
        }
        if (commandSender instanceof Player) {
            return new PlayerAudience((Player) commandSender);
        }
        Objects.requireNonNull(commandSender, "commandSender");
        return new ConsoleAudience(commandSender);
    }

}
