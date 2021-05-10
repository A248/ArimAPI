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
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static net.md_5.bungee.api.chat.TextComponent.fromLegacyText;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BungeeAudienceRepresenterTitleTest {

    private final ProxyServer server;
    private final ProxiedPlayer player;
    private final BungeeAudienceRepresenter representer;
    private Audience audience;

    public BungeeAudienceRepresenterTitleTest(@Mock ProxyServer server, @Mock ProxiedPlayer player) {
        this.server = server;
        this.player = player;
        representer = new BungeeAudienceRepresenter(server);
    }

    @BeforeEach
    public void setup() {
        audience = representer.toAudience(player);
        when(server.createTitle()).thenReturn(new TitleImpl());
    }

    @Test
    public void showTitleWithoutTimes() {
        when(server.createTitle()).thenReturn(new TitleImpl());
        Title title = Title.title(
                Component.text("title"), Component.text("subtitle"), null);
        assertDoesNotThrow(() -> audience.showTitle(title));
        verify(player).sendTitle(new TitleImpl()
                .title(fromLegacyText("title")).subTitle(fromLegacyText("subtitle"))
                .fadeIn(0).stay(0).fadeOut(0));
    }

    @Test
    public void showTitleWithTimes() {
        Title title = Title.title(
                Component.text("title"), Component.text("subtitle"),
                Title.Times.of(Duration.ofMillis(50L), Duration.ofMillis(100L), Duration.ofMillis(150L)));
        assertDoesNotThrow(() -> audience.showTitle(title));
        verify(player).sendTitle(new TitleImpl()
                .title(fromLegacyText("title")).subTitle(fromLegacyText("subtitle"))
                .fadeIn(1).stay(2).fadeOut(3));
    }

    @Test
    public void clearTitle() {
        assertDoesNotThrow(audience::clearTitle);
        verify(player).sendTitle(new TitleImpl().clear());
    }

    @Test
    public void resetTitle() {
        assertDoesNotThrow(audience::resetTitle);
        verify(player).sendTitle(new TitleImpl().reset());
    }

}
