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

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import space.arim.api.env.adventure.MessageOnlyAudience;

import java.time.Duration;
import java.util.Objects;

import static space.arim.api.env.bungee.BungeeAudienceRepresenter.convertComponent;

final class PlayerAudience implements MessageOnlyAudience {

    private final ProxiedPlayer player;
    private final ProxyServer server;

    PlayerAudience(ProxiedPlayer player, ProxyServer server) {
        this.player = player;
        this.server = server;
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
        Objects.requireNonNull(type, "type");
        BaseComponent[] converted = convertComponent(message);
        if (!source.equals(Identity.nil())) {
            if (type != MessageType.CHAT) {
                throw new UnsupportedOperationException(
                        "BungeeCord does not allow specifying both a non-default message type and non-default source");
            }
            player.sendMessage(source.uuid(), converted);
        } else {
            ChatMessageType messageType;
            switch (type) {
            case CHAT:
                messageType = ChatMessageType.CHAT;
                break;
            case SYSTEM:
                messageType = ChatMessageType.SYSTEM;
                break;
            default:
                throw new UnsupportedOperationException("Unknown chat message type " + type);
            }
            player.sendMessage(messageType, converted);
        }
    }

    @Override
    public void sendActionBar(@NonNull Component message) {
        player.sendMessage(ChatMessageType.ACTION_BAR, convertComponent(message));
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer) {
        player.setTabHeader(convertComponent(header), convertComponent(footer));
    }

    @Override
    public void showTitle(@NonNull Title title) {
        net.md_5.bungee.api.Title bungeeTitle = server.createTitle()
                .title(convertComponent(title.title()))
                .subTitle(convertComponent(title.subtitle()));
        Title.Times titleTimes = title.times();
        if (titleTimes != null) {
            bungeeTitle = bungeeTitle
                    .fadeIn(durationToTicks(titleTimes.fadeIn()))
                    .fadeOut(durationToTicks(titleTimes.fadeOut()))
                    .stay(durationToTicks(titleTimes.stay()));
        }
        player.sendTitle(bungeeTitle);
    }

    private static int durationToTicks(Duration duration) {
        return (int) (duration.toMillis() / 50L);
    }

    @Override
    public void clearTitle() {
        player.sendTitle(server.createTitle().clear());
    }

    @Override
    public void resetTitle() {
        player.sendTitle(server.createTitle().reset());
    }

    @Override
    public UnsupportedOperationException notSupportedException() {
        return new UnsupportedOperationException("Not supported on this platform");
    }
}
