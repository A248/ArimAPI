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
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.chat.BaseComponent;
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
    private static final MethodHandle SET_PLAYER_LIST_HEADER_FOOTER;
    private static final TitleSupport TITLE_SUPPORT;

    static {
        SERIALIZER = GsonComponentSerializer.builder().downsampleColors().emitLegacyHoverEvent().build();

        MethodHandle sendActionBar;
        try {
            sendActionBar = MethodHandles.lookup().findVirtual(Player.class, "sendActionBar", MethodType.methodType(void.class, String.class));
        } catch (NoSuchMethodException ex) {
            sendActionBar = null;
        } catch (IllegalAccessException ex) {
            throw new ExceptionInInitializerError(ex);
        }
        SEND_ACTION_BAR = sendActionBar;

        MethodHandle setPlayerListHeaderFooter;
        try {
            setPlayerListHeaderFooter = MethodHandles.lookup().findVirtual(Player.class, "setPlayerListHeaderFooter", MethodType.methodType(void.class, BaseComponent[].class, BaseComponent[].class));
        } catch (NoSuchMethodException ex) {
            setPlayerListHeaderFooter = null;
        } catch (IllegalAccessException ex) {
            throw new ExceptionInInitializerError(ex);
        }
        SET_PLAYER_LIST_HEADER_FOOTER = setPlayerListHeaderFooter;

        TitleSupport titleSupport;
        try {
            Class.forName("com.destroystokyo.paper.Title");
            titleSupport = new TitleSupport.PaperTitleSupport();
        } catch (ClassNotFoundException ex) {
            titleSupport = new TitleSupport.NoTitleSupport();
        }
        TITLE_SUPPORT = titleSupport;
    }

    private final Player player;

    PlayerAudience(Player player) {
        this.player = player;
    }

    static BaseComponent[] convertComponent(Component message) {
        // Careful with GsonComponentSerializer, which will silently accept nulls!
        Objects.requireNonNull(message, "message");
        // Adventure -> json -> BungeeChat
        return ComponentSerializer.parse(SERIALIZER.serialize(message));
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
        BaseComponent[] bungeeMessage = convertComponent(message);
        if (!source.equals(Identity.nil()) | !type.equals(MessageType.SYSTEM)) {
            throw new UnsupportedOperationException(
                    "This platform supports neither a non-system message type nor a non-default source");
        }
        player.spigot().sendMessage(bungeeMessage);
    }

    @Override
    public void sendActionBar(@NonNull Component message) {
        String actionBar = LegacyComponentSerializer.legacySection().serialize(message);
        if (SEND_ACTION_BAR == null) {
            throw notSupportedException();
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
    public void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer) {
        BaseComponent[] bungeeHeader = convertComponent(header);
        BaseComponent[] bungeeFooter = convertComponent(footer);
        if (SET_PLAYER_LIST_HEADER_FOOTER == null) {
            throw notSupportedException();
        }
        try {
            SET_PLAYER_LIST_HEADER_FOOTER.invokeExact(player, bungeeHeader, bungeeFooter);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void showTitle(@NonNull Title title) {
        Objects.requireNonNull(title, "title");
        TITLE_SUPPORT.showTitle(player, title);
    }

    @Override
    public void clearTitle() {
        TITLE_SUPPORT.clearTitle(player);
    }

    @Override
    public void resetTitle() {
        TITLE_SUPPORT.resetTitle(player);
    }

    @Override
    public UnsupportedOperationException notSupportedException() {
        return notSupported();
    }

    static UnsupportedOperationException notSupported() {
        return new UnsupportedOperationException("Not supported on this platform");
    }
}
