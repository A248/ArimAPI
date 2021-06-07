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

package space.arim.api.env.bukkit.it.legacypaper;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.api.env.bukkit.BukkitAudienceRepresenter;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class PlayerAudienceTest {

    private final Player player;
    private final Audience audience;

    public PlayerAudienceTest(@Mock Player player) {
        this.player = player;
        audience = new BukkitAudienceRepresenter().toAudience(player);
    }

    private com.destroystokyo.paper.Title fixEquals(com.destroystokyo.paper.Title expectedTitle) {
        return argThat(new TitleMatcher(expectedTitle));
    }

    @Test
    public void sendActionBar() {
        Component actionBar = Component.text("Action bar!", NamedTextColor.GREEN);
        assertDoesNotThrow(() -> audience.sendActionBar(actionBar));
        verify(player).sendActionBar(LegacyComponentSerializer.legacySection().serialize(actionBar));
        verifyNoMoreInteractions(player);
    }

    @Test
    public void sendPlayerListHeaderAndFooter() {
        Component header = Component.text("header");
        Component footer = Component.text("footer");
        assertDoesNotThrow(() -> audience.sendPlayerListHeaderAndFooter(header, footer));
        verify(player).setPlayerListHeaderFooter(
                TextComponent.fromLegacyText("header"), TextComponent.fromLegacyText("footer"));
        verifyNoMoreInteractions(player);
    }

    @Test
    public void showTitleWithoutTimes() {
        Title title = Title.title(Component.text("title"), Component.text("subtitle"), null);
        assertDoesNotThrow(() -> audience.showTitle(title));
        verify(player).sendTitle(
                fixEquals(new com.destroystokyo.paper.Title(
                        TextComponent.fromLegacyText("title"), TextComponent.fromLegacyText("subtitle"),
                        // A null Title.Times corresponds to packet values of 0
                        0, 0, 0)));
        verifyNoMoreInteractions(player);
    }

    @Test
    public void showTitleWithTimes() {
        Title title = Title.title(
                Component.text("title"), Component.text("subtitle"),
                Title.Times.of(Duration.ofMillis(50L), Duration.ofMillis(100L), Duration.ofMillis(150L)));
        assertDoesNotThrow(() -> audience.showTitle(title));
        verify(player).sendTitle(
                fixEquals(new com.destroystokyo.paper.Title(
                        TextComponent.fromLegacyText("title"), TextComponent.fromLegacyText("subtitle"), 1, 2, 3)));
        verifyNoMoreInteractions(player);
    }

    @Test
    public void clearTitle() {
        assertDoesNotThrow(audience::clearTitle);
        verify(player).hideTitle();
        verifyNoMoreInteractions(player);
    }

    @Test
    public void resetTitle() {
        assertDoesNotThrow(audience::resetTitle);
        verify(player).resetTitle();
        verifyNoMoreInteractions(player);
    }
}
