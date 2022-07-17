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


import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;
import space.arim.api.env.concurrent.ClosableFactoryOfTheFuture;
import space.arim.managedwaits.LightSleepManagedWaitStrategy;
import space.arim.managedwaits.ManagedWaitStrategy;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

/**
 * An implementation of {@link FactoryOfTheFuture} for the Sponge platform, using a specified
 * plugin to execute synchronous tasks.
 *
 */
public final class SpongeFactoryOfTheFuture {

	private SpongeFactoryOfTheFuture() {}

	/**
	 * Creates from a plugin and game to use, with the default wait strategy
	 *
	 * @param plugin the plugin
	 * @param game the game
	 * @return the futures factory, which should be closed when disposed of
	 */
	public static ClosableFactoryOfTheFuture create(PluginContainer plugin, Game game) {
		return create(plugin, game, new LightSleepManagedWaitStrategy());
	}

	/**
	 * Creates from a plugin and game and {@link ManagedWaitStrategy} to use for managed waits
	 *
	 * @param plugin the plugin
	 * @param game the game
	 * @return the futures factory, which should be closed when disposed of
	 */
	public static ClosableFactoryOfTheFuture create(PluginContainer plugin, Game game, ManagedWaitStrategy waitStrategy) {
		return SpongeFactoryOfTheFutureImpl.create(plugin, game.server(), waitStrategy);
	}

}
