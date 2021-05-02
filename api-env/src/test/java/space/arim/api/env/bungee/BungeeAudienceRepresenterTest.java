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
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.api.env.adventure.AudienceTesting;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static space.arim.api.env.bungee.BungeeAudienceRepresenter.convertComponent;

@ExtendWith(MockitoExtension.class)
public class BungeeAudienceRepresenterTest {

    private final BungeeAudienceRepresenter representer;

    public BungeeAudienceRepresenterTest(@Mock ProxyServer server) {
        representer = new BungeeAudienceRepresenter(server);
    }

    @Test
    public void nullCheck() {
        assertThrows(NullPointerException.class, () ->  representer.toAudience(null));
    }

    @TestFactory
    public Stream<DynamicNode> basicAudienceContracts() {
        return Stream.of(representer.toAudience(mock(CommandSender.class)), representer.toAudience(mock(ProxiedPlayer.class)))
                .flatMap((audience) -> new AudienceTesting(audience).verifyBasicContracts());
    }

    @Test
    public void convertComponentEqual() {
        String text = "text";
        assertArrayEquals(
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(text),
                convertComponent(Component.text(text)));
    }

    @Test
    public void sendActionBar() {
        ProxiedPlayer player = mock(ProxiedPlayer.class);
        Audience audience = representer.toAudience(player);
        Component actionBar = Component.text("action bar");
        assertDoesNotThrow(() -> audience.sendActionBar(actionBar));
        verify(player).sendMessage(ChatMessageType.ACTION_BAR, convertComponent(actionBar));
    }

}
