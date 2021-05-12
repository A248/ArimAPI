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
package space.arim.api.env.chat;

import java.util.List;
import java.util.Objects;

import space.arim.api.chat.SendableMessage;

import net.md_5.bungee.api.chat.TextComponent;

/**
 * Enables compatibility with the BungeeCord/Spigot Chat Component API. <br>
 * <br>
 * The BungeeCord Component API is also included on Spigot. It is both the BungeeCord proxy's
 * and the Spigot server's JSON message API, a welcoming sight of API unity. The Bungee Component API
 * has a few breaking changes across versions, making it difficult to maintain compatibility with multiple
 * Bungee Component API versions, such as those present in older versions of Spigot. Moreover, its objects
 * are mutable, requiring cloning should they be safely exposed as public return types, and may be initialised
 * with nullable state, where the behaviour of {@code null} is not well documented. <br>
 * <br>
 * <b>Comparison with ArimAPI</b> <br>
 * <br>
 * <i>Colourful messages</i> <br>
 * With regards to chat colours and style formatting, ArimAPI distinguishes clearly between colouring and styling.
 * {@code ChatColor} makes no such distinction; all of its colours and formatting are contained within. ArimAPI fully
 * supports hex colours, just as Bungee Components do. However, older versions of the latter do not support arbitrary
 * hex colours, in which case conversion uses {@link space.arim.api.chat.PredefinedColour#getNearestTo(int)}. <br>
 * <br>
 * <i>Json Messages</i> <br>
 * ArimAPI enables tooltips, urls, commands, suggestions, and insertions. Bungee Components have equivalent support for each
 * of these. However, some features from the Bungee API are not available in ArimAPI. <br>
 * <br>
 * For hover actions, ArimAPI only supports displaying tooltip messages. Thus, there is only an equivalent for
 * {@code HoverEvent.Action.SHOW_TEXT}, but other {@code HoverEvent.Action}s are ommitted during conversion. <br>
 * <br>
 * For click actions, ArimAPI only supports urls, commands, and suggestions. There are no equivalents for the others,
 * which are ignored. <br>
 * <br>
 * <i>Nontext Messages</i> <br>
 * ArimAPI only supports components which are text based. Hence, the only subclass of {@code BaseComponent} supported is
 * {@code TextComponent}. When another kind of component is encountered, it is skipped. <br>
 * <br>
 * <i>Conversion Policy</i> <br>
 * When there are no equivalents for a Bungee Component API feature, it is simply discarded from the final result. <br>
 * <br>
 * <i>Nullness Policy</i> <br>
 * The Bungee Component API seems to omit null checking preconditions. Null parameters are rejected by this class, including
 * null array elements. Generally, objects from the Bungee Component API with null fields where null is not a defined result
 * are treated as if null indicates the lack of the relevant feature.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public class BungeeComponentConverter implements PlatformMessageAdapter<List<TextComponent>> {
	
	/**
	 * Creates an instance
	 * 
	 */
	public BungeeComponentConverter() {}
	
	@Override
	public List<TextComponent> convert(SendableMessage message) {
		Objects.requireNonNull(message, "message");

		return ConversionsToBungee.convertFrom0(message);
	}
	
	@Override
	public SendableMessage convert(List<TextComponent> message) {
		message = List.copyOf(message);

		return new FromBungeeConverter(message).parseToMessage();
	}
	
	@Override
	public boolean supportsHexColoursFunctionality() {
		return BungeeColourConversions.HAS_HEX_SUPPORT;
	}
	
}
