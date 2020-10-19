/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env;

import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.serialiser.LegacyCodeSerialiser;
import space.arim.api.env.annote.PlatformCommandSender;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.chat.BungeeComponentConverter;
import space.arim.api.env.concurrent.BukkitEnhancedExecutor;
import space.arim.api.env.concurrent.BukkitFactoryOfTheFuture;
import space.arim.api.env.realexecutor.BukkitRealExecutorFinder;
import space.arim.api.env.realexecutor.RealExecutorFinder;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.TextComponent;

/**
 * Implementation of {@link PlatformHandle} specifically for Bukkit servers.
 * 
 * @author A248
 *
 */
public class BukkitPlatformHandle extends AbstractPlatformHandle {
	
	/**
	 * Creates from a {@code JavaPlugin} to use
	 * 
	 * @param plugin the plugin
	 */
	public BukkitPlatformHandle(JavaPlugin plugin) {
		super(plugin, plugin.getServer());
	}
	
	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return (JavaPlugin) getImplementingPluginInfo().getPlugin();
	}
	
	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		return new BukkitFactoryOfTheFuture(getPlugin());
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		return new BukkitEnhancedExecutor(getPlugin());
	}

	/**
	 * Sends a {@link SendableMessage} to a command sender based on this platform. <br>
	 * <br>
	 * <b>This method is not strictly speaking thread safe.</b> However, in practice, multiple
	 * plugins send messages asynchronously.
	 * 
	 */
	@Override
	public void sendMessage(@PlatformCommandSender Object recipient, SendableMessage message) {
		sendMessage((CommandSender) recipient, message);
	}
	
	/**
	 * Disconnects a player with a reason. <br>
	 * <br>
	 * <b>This method is not thread safe.</b>
	 * 
	 */
	@Override
	public void disconnectUser(@PlatformPlayer Object user, SendableMessage reason) {
		disconnectUser((Player) user, reason);
	}
	
	/**
	 * Sends a message to a recipient. Same as {@link #sendMessage(Object, SendableMessage)}. <br>
	 * <br>
	 * <b>This method is not strictly speaking thread safe.</b> However, in practice, multiple
	 * plugins send messages asynchronously.
	 * 
	 * @param recipient the recipient
	 * @param message the message
	 */
	public void sendMessage(CommandSender recipient, SendableMessage message) {
		if (recipient instanceof Player) {
			((Player) recipient).spigot().sendMessage(
					new BungeeComponentConverter().convert(message).toArray(TextComponent[]::new));
		} else {
			recipient.sendMessage(LegacyCodeSerialiser.getInstance(ChatColor.COLOR_CHAR).serialise(message));
		}
	}
	
	/**
	 * Disconnects a player with a reason. Same as {@link #disconnectUser(Object, SendableMessage)}. <br>
	 * <br>
	 * <b>This method is not thread safe.</b>
	 * 
	 * @param user the user to kick
	 * @param reason the kick message
	 */
	public void disconnectUser(Player user, SendableMessage reason) {
		user.kickPlayer(LegacyCodeSerialiser.getInstance(ChatColor.COLOR_CHAR).serialise(reason));
	}
	
	@Override
	public RealExecutorFinder getRealExecutorFinder() {
		return new BukkitRealExecutorFinder(getPlugin());
	}

	@Override
	public String getPlatformVersion() {
		Server server = getPlugin().getServer();
		return server.getVersion() + " (API " + server.getBukkitVersion() + ")";
	}

}
