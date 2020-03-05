/* 
 * ArimAPI-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform.sponge;

import org.spongepowered.api.plugin.PluginContainer;

import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.registry.RequireRegistration;

import space.arim.api.concurrent.SyncExecution;
import space.arim.api.platform.AbstractTPSMeter;
import space.arim.api.util.TPSMeter;

/**
 * A default implementation of {@link TPSMeter} on the Sponge platform.
 * 
 * @author A248
 *
 */
public class DefaultTPSMeter extends AbstractTPSMeter {
	
	/**
	 * Creates the instance. See {@link SpongeRegistrable#SpongeRegistrable(PluginContainer)} for more information.
	 * 
	 * @param plugin the plugin to use for Registrable information
	 * @param registry the {@link Registry} to use. It must have a registration for {@link SyncExecution} as specified by the annotation.
	 */
	public DefaultTPSMeter(PluginContainer plugin, @RequireRegistration(SyncExecution.class) Registry registry) {
		super(SpongePlatform.get().convertPluginInfo(plugin), registry.getRegistration(SyncExecution.class));
	}
	
	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}

}
