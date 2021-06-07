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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.api.env.adventure.AudienceTesting;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static space.arim.api.env.bukkit.test.BaseComponentMatcher.fixEquals;

@ExtendWith(MockitoExtension.class)
public class BukkitAudienceRepresenterTest {

    private final BukkitAudienceRepresenter representer = new BukkitAudienceRepresenter();

    @Test
    public void nullCheck() {
        assertThrows(NullPointerException.class, () ->  representer.toAudience(null));
    }

    private Audience playerAudience(Player player) {
        return representer.toAudience(player);
    }

    private Audience playerAudience() {
        Player player = mock(Player.class);
        lenient().when(player.spigot()).thenReturn(mock(Player.Spigot.class));
        return playerAudience(player);
    }

    private Stream<AudienceTesting> testableAudiences() {
        return Stream.of(representer.toAudience(mock(CommandSender.class)), playerAudience()).map(AudienceTesting::new);
    }

    @TestFactory
    public Stream<DynamicNode> basicAudienceContracts() {
        return testableAudiences().flatMap(AudienceTesting::verifyBasicContracts);
    }

    @TestFactory
    public Stream<DynamicNode> sendMessageDefaultIdentityAndType() {
        Executable verificationTest = () -> {
            Player player = mock(Player.class);
            Player.Spigot playerSpigot = mock(Player.Spigot.class);
            when(player.spigot()).thenReturn(playerSpigot);
            Audience audience = playerAudience(player);
            String message = "message";
            audience.sendMessage(Identity.nil(), Component.text(message), MessageType.SYSTEM);
            verify(playerSpigot).sendMessage(fixEquals(TextComponent.fromLegacyText(message)));
        };
        return Stream.concat(
                testableAudiences().map(at -> at.verifyDoesSupport(Identity.nil(), MessageType.SYSTEM)),
                Stream.of(DynamicTest.dynamicTest("Player verification", verificationTest)));
    }

    @TestFactory
    public Stream<DynamicNode> sendMessageNonDefaultIdentityAndOrType() {
        return new AudienceTesting(playerAudience()).verifyDoesNotSupportNonDefaultIdentityAndOrType();
    }

    @Test
    public void sendActionBar() {
        Audience audience = playerAudience();
        Component actionBar = Component.text("action");
        assertThrows(UnsupportedOperationException.class, () -> audience.sendActionBar(actionBar));
    }

    @Test
    public void sendPlayerListHeaderAndFooter() {
        Audience audience = playerAudience();
        Component header = Component.text("header");
        Component footer = Component.text("footer");
        assertThrows(UnsupportedOperationException.class, () -> audience.sendPlayerListHeaderAndFooter(header, footer));
    }

    @Test
    public void showTitle() {
        Audience audience = playerAudience();
        Title title = Title.title(Component.empty(), Component.empty());
        assertThrows(UnsupportedOperationException.class, () -> audience.showTitle(title));
    }

    @Test
    public void clearTitle() {
        Audience audience = playerAudience();
        assertThrows(UnsupportedOperationException.class, audience::clearTitle);
    }

    @Test
    public void resetTitle() {
        Audience audience = playerAudience();
        assertThrows(UnsupportedOperationException.class, audience::resetTitle);
    }
}
