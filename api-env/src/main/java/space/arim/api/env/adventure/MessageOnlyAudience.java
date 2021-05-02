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

package space.arim.api.env.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/**
 * {@code Audience} implementation which supports only {@code sendMessage} methods
 * and throws {@code UnsupportedOperationException} for all else
 *
 */
public interface MessageOnlyAudience extends Audience {

    @Override
    void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type);

    @Override
    default void sendActionBar(@NonNull Component message) {
        requireNonNull(message, "message");
        throw notSupportedException();
    }

    @Override
    default void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer) {
        requireNonNull(header, "header");
        requireNonNull(footer, "footer");
        throw notSupportedException();
    }

    @Override
    default void showTitle(@NonNull Title title) {
        requireNonNull(title, "title");
        throw notSupportedException();
    }

    @Override
    default void clearTitle() {
        throw notSupportedException();
    }

    @Override
    default void resetTitle() {
        throw notSupportedException();
    }

    @Override
    default void showBossBar(@NonNull BossBar bar) {
        requireNonNull(bar, "bar");
        throw notSupportedException();
    }

    @Override
    default void hideBossBar(@NonNull BossBar bar) {
        requireNonNull(bar, "bar");
        throw notSupportedException();
    }

    @Override
    default void playSound(@NonNull Sound sound) {
        requireNonNull(sound, "sound");
        throw notSupportedException();
    }

    @Override
    default void playSound(@NonNull Sound sound, double x, double y, double z) {
        requireNonNull(sound, "sound");
        throw notSupportedException();
    }

    @Override
    default void stopSound(@NonNull SoundStop stop) {
        requireNonNull(stop, "stop");
        throw notSupportedException();
    }

    @Override
    default void openBook(@NonNull Book book) {
        requireNonNull(book, "book");
        throw notSupportedException();
    }

    /**
     * Gets the exception thrown for unsupported operations
     *
     * @return the exception thrown
     */
    UnsupportedOperationException notSupportedException();

}
