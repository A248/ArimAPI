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

package space.arim.api.env.bungee;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;
import space.arim.api.env.AudienceRepresenter;

import java.util.Objects;

/**
 * Implementation of {@code AudienceRepresenter} for the BungeeCord platform.
 * Converts to the bungeecord component API before sending
 *
 */
public final class BungeeAudienceRepresenter implements AudienceRepresenter<CommandSender> {

    private final ProxyServer server;

    /**
     * Creates from the given proxy server
     *
     * @param server the proxy server
     */
    public BungeeAudienceRepresenter(ProxyServer server) {
        this.server = Objects.requireNonNull(server, "server");
    }

    @Override
    public Audience toAudience(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            return new PlayerAudience((ProxiedPlayer) commandSender, server);
        }
        Objects.requireNonNull(commandSender, "commandSender");
        return new ConsoleAudience(commandSender);
    }

    static BaseComponent[] convertComponent(Component component) {
        // Careful with GsonComponentSerializer, which will silently accept nulls!
        Objects.requireNonNull(component, "component");
        // Adventure -> json -> BungeeChat
        return ComponentSerializer.parse(GsonComponentSerializer.gson().serialize(component));
    }

}
