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
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.api.env.adventure.AudienceTesting;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

    private Audience consoleAudience() {
        return representer.toAudience(mock(CommandSender.class));
    }
    private Audience playerAudience(ProxiedPlayer player) {
        return representer.toAudience(player);
    }
    private Audience playerAudience() {
        return playerAudience(mock(ProxiedPlayer.class));
    }

    private Stream<AudienceTesting> testableAudiences() {
        return Stream.of(consoleAudience(), playerAudience()).map(AudienceTesting::new);
    }

    @TestFactory
    public Stream<DynamicNode> basicAudienceContracts() {
        return testableAudiences().flatMap(AudienceTesting::verifyBasicContracts);
    }

    /*
     * The following tests regard Identity/MessageType support
     *
     * BungeeCord supports MessageType.SYSTEM with no identity
     * or MessageType.CHAT with a non-default Identity
     */

    private static final String MESSAGE = "message";
    private Stream<DynamicNode> playerOnlySupport(Identity identity, MessageType messageType,
                                                  Consumer<ProxiedPlayer> verification) {
        Executable verificationTest = () -> {
            ProxiedPlayer player = mock(ProxiedPlayer.class);
            Audience audience = playerAudience(player);
            audience.sendMessage(identity, Component.text(MESSAGE), messageType);
            verification.accept(player);
            verifyNoMoreInteractions(player);
        };
        return Stream.of(
                new AudienceTesting(playerAudience()).verifyDoesSupport(identity, messageType),
                DynamicTest.dynamicTest("Player verification", verificationTest));
    }
    private Stream<DynamicNode> fullSupport(Identity identity, MessageType messageType,
                                            Consumer<ProxiedPlayer> verification) {
        return Stream.concat(
                Stream.of(new AudienceTesting(consoleAudience()).verifyDoesSupport(identity, messageType)),
                playerOnlySupport(identity, messageType, verification));
    }

    @TestFactory
    public Stream<DynamicNode> sendMessageWithSystemType() {
        return fullSupport(Identity.nil(), MessageType.SYSTEM, (player) -> {
            verify(player).sendMessage(ChatMessageType.SYSTEM, TextComponent.fromLegacyText(MESSAGE));
        });
    }

    @TestFactory
    public Stream<DynamicNode> sendMessageWithChatTypeAndIdentity() {
        UUID sender = UUID.randomUUID();
        return playerOnlySupport(Identity.identity(sender), MessageType.CHAT, (player) -> {
            verify(player).sendMessage(sender, TextComponent.fromLegacyText(MESSAGE));
        });
    }

    @TestFactory
    public Stream<DynamicNode> sendMessageWithChatType() {
        return playerOnlySupport(Identity.nil(), MessageType.CHAT, (player) -> {
            verify(player).sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(MESSAGE));
        });
    }

    @TestFactory
    public Stream<DynamicNode> sendMessageWithSystemTypeAndIdentity() {
        Identity identity = Identity.identity(UUID.randomUUID());
        return Stream.of(new AudienceTesting(playerAudience()).verifyDoesNotSupport(identity, MessageType.SYSTEM));
    }

    @Test
    public void convertComponentEqual() {
        String text = "text";
        assertArrayEquals(TextComponent.fromLegacyText(text), convertComponent(Component.text(text)));
    }

    @Test
    public void sendActionBar() {
        ProxiedPlayer player = mock(ProxiedPlayer.class);
        Audience audience = playerAudience(player);
        Component actionBar = Component.text("action bar");
        assertDoesNotThrow(() -> audience.sendActionBar(actionBar));
        verify(player).sendMessage(ChatMessageType.ACTION_BAR, convertComponent(actionBar));
    }

    @Test
    public void sendPlayerListHeaderAndFooter() {
        ProxiedPlayer player = mock(ProxiedPlayer.class);
        Audience audience = playerAudience(player);
        Component header = Component.text("header");
        Component footer = Component.text("footer");
        assertDoesNotThrow(() -> audience.sendPlayerListHeaderAndFooter(header, footer));
        verify(player).setTabHeader(convertComponent(header), convertComponent(footer));
    }

}
