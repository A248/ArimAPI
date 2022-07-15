/*
 * ArimAPI
 * Copyright © 2022 Anand Beh
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

package space.arim.api.env.sponge;

import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.CommandCause;
import space.arim.api.env.AudienceRepresenter;

/**
 * Simple implementation of {@code AudienceRepresenter} for a Sponge {@code CommandCause}.
 *
 */
public final class SpongeAudienceRepresenter implements AudienceRepresenter<CommandCause> {

	@Override
	public Audience toAudience(CommandCause commandSender) {
		return commandSender.audience();
	}
}
