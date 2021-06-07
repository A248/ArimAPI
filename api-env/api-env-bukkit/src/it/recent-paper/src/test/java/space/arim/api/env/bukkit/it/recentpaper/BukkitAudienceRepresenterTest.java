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

package space.arim.api.env.bukkit.it.recentpaper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.api.env.AudienceRepresenter;
import space.arim.api.env.bukkit.BukkitAudienceRepresenter;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
public class BukkitAudienceRepresenterTest {

    private final AudienceRepresenter<CommandSender> representer = new BukkitAudienceRepresenter();

    @Test
    public void passthroughConsole(@Mock CommandSender console) {
        assertSame(console, representer.toAudience(console));
    }

    @Test
    public void passthroughPlayer(@Mock Player player) {
        assertSame(player, representer.toAudience(player));
    }
}
