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

import java.util.Objects;

import space.arim.api.chat.SendableMessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/**
 * Enables compatibility with the Kyori Adventure API. <br>
 * <br>
 * The Adventure API is used on Velocity and thus may be used to send chat messages directly there. Additionally,
 * it has its own platform adapters used to send messages on other platforms. Adventure API classes are immutable,
 * making it well-suitable for a variety of contexts. Its textual API is similar to the Sponge Text API. As an arguable
 * improvement, Adventure uses annotation based null analysis rather than {@code Optional}s. <br>
 * <br>
 * <b>Comparison with ArimAPI</b> <br>
 * <br>
 * <i>Colourful messages</i> <br>
 * With regards to chat colours and style formatting, Adventure defines styles (a.k.a. "decorations") as tristates.
 * ArimAPI only permits enabled and disabled styles. Unset Adventure decorations are converted to disabled styles.
 * The Adventure API fully supports hex colours; conversion of respective colour representations is simple and well defined. <br>
 * <br>
 * <i>Json Messages</i> <br>
 * ArimAPI enables tooltips, urls, commands, suggestions, and insertions. Adventure components have equivalent support
 * for each of these. However, some features from the Adventure API are not available in ArimAPI. <br>
 * <br>
 * For hover actions, ArimAPI only supports displaying tooltip messages. Therefore, only {@code HoverEvent.Action#SHOW_TEXT}
 * is supported. Other actions are ignored during conversion. <br>
 * <br>
 * For click actions, ArimAPI only supports urls, commands, and suggestions. There are no equivalents for the others,
 * which are dropped. <br>
 * <br>
 * <i>Nontext Messages</i> <br>
 * ArimAPI only supports components which are text based. Hence, this class converts {@link TextComponent} and not
 * {@link Component}. <br>
 * <br>
 * <i>Conversion Policy</i> <br>
 * When there are no equivalents for an Adventure API feature, it is simply discarded from the final result.
 * 
 * @author A248
 *
 */
public class AdventureTextConverter implements PlatformMessageAdapter<TextComponent> {

	/**
	 * Creates an instance
	 * 
	 */
	public AdventureTextConverter() {}
	
	@Override
	public TextComponent convertTo(SendableMessage message) {
		Objects.requireNonNull(message, "message");

		return ConversionsToAdventure.convertFrom0(message);
	}
	
	@Override
	public SendableMessage convertFrom(TextComponent message) {
		Objects.requireNonNull(message, "message");

		return new FromAdventureConverter(message).parseToMessage();
	}
	
	@Override
	public boolean supportsHexColoursFunctionality() {
		return true;
	}

}
