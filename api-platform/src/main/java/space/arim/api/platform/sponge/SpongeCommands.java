/* 
 * ArimAPI-platform
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-platform is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-platform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-platform. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform.sponge;

import space.arim.api.util.LazySingleton;

/**
 * Sponge commands utility. Use {@link #get()} to get the instance.
 * 
 * @author A248
 *
 */
public class SpongeCommands {

	private static final LazySingleton<SpongeCommands> INST = new LazySingleton<SpongeCommands>(SpongeCommands::new);
	
	protected SpongeCommands() {}
	
	/**
	 * Gets the main instance
	 * 
	 * @return the instance
	 */
	public static SpongeCommands get() {
		return INST.get();
	}
	
	
}
