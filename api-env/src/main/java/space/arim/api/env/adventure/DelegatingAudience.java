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

/**
 * {@code Audience} implementation which delegates to another
 *
 */
public abstract class DelegatingAudience implements Audience {

    /**
     * Creates
     */
    protected DelegatingAudience() {}

    /**
     * Gets the delegate adventure to be used
     *
     * @return the delegate adventure
     */
    protected abstract Audience delegate();

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
        delegate().sendMessage(source, message, type);
    }

    @Override
    public void sendActionBar(@NonNull Component message) {
        delegate().sendActionBar(message);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer) {
        delegate().sendPlayerListHeaderAndFooter(header, footer);
    }

    @Override
    public void showTitle(@NonNull Title title) {
        delegate().showTitle(title);
    }

    @Override
    public void clearTitle() {
        delegate().clearTitle();
    }

    @Override
    public void resetTitle() {
        delegate().resetTitle();
    }

    @Override
    public void showBossBar(@NonNull BossBar bar) {
        delegate().showBossBar(bar);
    }

    @Override
    public void hideBossBar(@NonNull BossBar bar) {
        delegate().hideBossBar(bar);
    }

    @Override
    public void playSound(@NonNull Sound sound) {
        delegate().playSound(sound);
    }

    @Override
    public void playSound(@NonNull Sound sound, double x, double y, double z) {
        delegate().playSound(sound, x, y, z);
    }

    @Override
    public void stopSound(@NonNull SoundStop stop) {
        delegate().stopSound(stop);
    }

    @Override
    public void openBook(@NonNull Book book) {
        delegate().openBook(book);
    }
}
