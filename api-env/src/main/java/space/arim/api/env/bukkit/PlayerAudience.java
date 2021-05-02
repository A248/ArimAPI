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

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import space.arim.api.env.adventure.MessageOnlyAudience;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;

final class PlayerAudience implements MessageOnlyAudience {

    private static final GsonComponentSerializer SERIALIZER;
    private static final MethodHandle SEND_ACTION_BAR;

    static {
        SERIALIZER = GsonComponentSerializer.builder().downsampleColors().emitLegacyHoverEvent().build();
        MethodHandle sendActionBar;
        try {
            sendActionBar = MethodHandles.lookup().findVirtual(Player.class, "sendActionBar", MethodType.methodType(void.class, String.class));
        } catch (NoSuchMethodException ex) {
            sendActionBar = null;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        SEND_ACTION_BAR = sendActionBar;
    }

    private final Player player;

    PlayerAudience(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(type, "type");
        // Careful with GsonComponentSerializer, which will silently accept nulls!
        Objects.requireNonNull(message, "message");
        // Adventure -> json -> BungeeChat
        player.spigot().sendMessage(ComponentSerializer.parse(SERIALIZER.serialize(message)));
    }

    @Override
    public void sendActionBar(@NonNull Component message) {
        String actionBar = LegacyComponentSerializer.legacySection().serialize(message);
        if (SEND_ACTION_BAR == null) {
            throw new UnsupportedOperationException("This version of the Bukkit API does not support Player.sendActionBar");
        }
        try {
            SEND_ACTION_BAR.invokeExact(player, actionBar);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public UnsupportedOperationException notSupportedException() {
        return new UnsupportedOperationException("Not supported on this platform");
    }
}
