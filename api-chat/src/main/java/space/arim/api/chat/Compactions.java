/* 
 * ArimAPI-chat
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-chat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-chat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-chat. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.chat;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

final class Compactions {

	private Compactions() {}
	
	private static <T extends Emptyable> void compact(List<T> mutableList, BiPredicate<T, T> similarity,
			BiFunction<T, T, T> combiner) {
		T previous = null;
		for (ListIterator<T> it = mutableList.listIterator(); it.hasNext();) {

			T current = Objects.requireNonNull(it.next(), "element");
			if (current.isEmpty()) {
				it.remove();
				continue;
			}
			if (previous != null && similarity.test(previous, current)) {
				// Update current section by combining previous with current contents
				T updated = combiner.apply(previous, current);
				it.set(updated);
				// Move temporarily back, then remove the element there
				it.previous();
				it.previous();
				it.remove();
				// Move back to place
				T updatedVerify = it.next();
				assert updated == updatedVerify : updatedVerify;
			}
			previous = current;
		}
	}
	
	static void compactComponents(List<ChatComponent> components) {
		compact(components, (previous, current) -> {
			return previous.getColour() == current.getColour()
					&& previous.getStyles() == current.getStyles();
		}, (previous, current) -> {
			return new ChatComponent.Builder(previous).text(previous.getText().concat(current.getText())).build();
		});
	}
	
	static void compactSections(List<JsonSection> sections) {
		compact(sections, (previous, current) -> {
			return Objects.equals(previous.getHoverAction(), current.getHoverAction())
					&& Objects.equals(previous.getClickAction(), current.getClickAction())
					&& Objects.equals(previous.getInsertionAction(), current.getInsertionAction());
		}, (previous, current) -> {
			return new JsonSection.Builder(previous).addContent(current.getContents()).build();
		});
	}
	
}
