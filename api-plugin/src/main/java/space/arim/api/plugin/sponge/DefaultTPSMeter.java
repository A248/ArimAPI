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
package space.arim.api.plugin.sponge;

import org.spongepowered.api.plugin.PluginContainer;

import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.concurrent.SyncExecution;
import space.arim.api.server.TPSMeter;
import space.arim.api.server.sponge.SpongeRegistrable;

public class DefaultTPSMeter extends SpongeRegistrable implements TPSMeter {

	private long last = System.currentTimeMillis();
	private double tps = 20D;
	
	public DefaultTPSMeter(PluginContainer plugin) {
		super(plugin);
		UniversalRegistry.get().getRegistration(SyncExecution.class).runTaskTimer(() -> {
			long current = System.currentTimeMillis();
			tps = 1000L/(current - last);
			last = current;
		}, 20L);
	}
	
	@Override
	public double getTPS() {
		return tps;
	}
	
	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}

}
