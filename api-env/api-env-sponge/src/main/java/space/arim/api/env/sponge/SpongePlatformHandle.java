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
import space.arim.api.env.PlatformHandle;
import space.arim.api.env.PlatformPluginInfo;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
import space.arim.omnibus.util.concurrent.impl.SimplifiedEnhancedExecutor;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Implementation of {@link PlatformHandle} specifically for Sponge servers.
 *
 */
public final class SpongePlatformHandle {

	private SpongePlatformHandle() {}

	/**
	 * Creates from a plugin and game to use
	 *
	 * @param plugin the plugin
	 * @param game the game
	 * @return the platform handle
	 */
	public static PlatformHandle create(PluginContainer plugin, Game game) {
		return new SpongePlatformHandleImpl(plugin, game);
	}

}
final class SpongePlatformHandleImpl implements PlatformHandle {

	private final PluginContainer plugin;
	private final Game game;

	SpongePlatformHandleImpl(PluginContainer plugin, Game game) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
		this.game = Objects.requireNonNull(game, "game");
	}

	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		return SpongeFactoryOfTheFuture.create(plugin, game);
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		Executor executor = game.asyncScheduler().executor(plugin);
		class SpongeEnhancedExecutor extends SimplifiedEnhancedExecutor {

			@Override
			public void execute(Runnable command) {
				executor.execute(command);
			}
		}
		return new SpongeEnhancedExecutor();
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return new PlatformPluginInfo(plugin, game);
	}

	@Override
	public String getPlatformVersion() {
		return game.platform().minecraftVersion().name();
	}
}
