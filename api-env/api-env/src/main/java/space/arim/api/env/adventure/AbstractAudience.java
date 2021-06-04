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
 * Base interface for implementations of {@code Audience} which intentionally re-abstracts
 * default methods in {@code Audience}. Useful to ensure the implementation overrides
 * the intended set of methods.
 *
 */
/*
 * Changes to this class should be updated likewise in MessageOnlyAudience and DelegatingAudience
 */
public interface AbstractAudience extends Audience {

    @Override
    void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type);

    @Override
    void sendActionBar(@NonNull Component message);

    @Override
    void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer);

    @Override
    void showTitle(@NonNull Title title);

    @Override
    void clearTitle();

    @Override
    void resetTitle();

    @Override
    void showBossBar(@NonNull BossBar bar);

    @Override
    void hideBossBar(@NonNull BossBar bar);

    @Override
    void playSound(@NonNull Sound sound);

    @Override
    void playSound(@NonNull Sound sound, double x, double y, double z);

    @Override
    void stopSound(@NonNull SoundStop stop);

    @Override
    void openBook(@NonNull Book book);

}
