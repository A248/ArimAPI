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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.api.env.adventure.AudienceTesting;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class BukkitAudienceRepresenterTest {

    private final BukkitAudienceRepresenter representer = new BukkitAudienceRepresenter();

    @Test
    public void nullCheck() {
        assertThrows(NullPointerException.class, () ->  representer.toAudience(null));
    }

    private Audience playerAudience() {
        return representer.toAudience(mock(Player.class));
    }

    @TestFactory
    public Stream<DynamicNode> basicAudienceContracts() {
        return Stream.of(representer.toAudience(mock(CommandSender.class)), playerAudience())
                .flatMap((audience) -> new AudienceTesting(audience).verifyBasicContracts());
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
