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
import net.md_5.bungee.api.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import space.arim.api.jsonchat.adventure.implementor.MessageOnlyAudience;

import java.util.Objects;

import static space.arim.api.env.bungee.BungeeAudienceRepresenter.convertComponent;

final class ConsoleAudience implements MessageOnlyAudience {

    private final CommandSender sender;

    ConsoleAudience(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(type, "type");
        sender.sendMessage(convertComponent(message));
    }

    @Override
    public UnsupportedOperationException notSupportedException() {
        return new UnsupportedOperationException("Not a player");
    }
}
