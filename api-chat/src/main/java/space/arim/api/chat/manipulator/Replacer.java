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
package space.arim.api.chat.manipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.manipulator.SendableMessageManipulator.TextGoal;

class Replacer extends ManipulationFeature {

	private final UnaryOperator<String> operator;
	
	Replacer(SendableMessageManipulator manipulator, UnaryOperator<String> operator) {
		super(manipulator);
		this.operator = Objects.requireNonNull(operator, "operator");
	}
	
	private String apply(String value) {
		return Objects.requireNonNull(operator.apply(value), "operator returned null");
	}
	
	SendableMessage replace() {
		if (isNoOp()) {
			return message();
		}
		boolean changedAny = false;
		List<JsonSection> sections = new ArrayList<>(message().getSections().size());
		for (JsonSection section : message().getSections()) {

			JsonSection built = rebuildSection(section);
			JsonSection result;
			if (section.equals(built)) {
				result = section;
			} else {
				result = built;
				changedAny = true;
			}
			sections.add(result);
		}
		if (!changedAny) {
			return message();
		}
		return SendableMessage.create(sections);
	}
	
	private JsonSection rebuildSection(JsonSection section) {
		JsonSection.Builder sectionBuilder = new JsonSection.Builder(section);

		if (hasGoal(TextGoal.SIMPLE_TEXT)) {
			List<ChatComponent> oldContents = section.getContents();
			List<ChatComponent> newContents = replaceTextInComponents(oldContents);
			if (!oldContents.equals(newContents)) {
				sectionBuilder.contents(newContents);
			}
		}
		JsonHover oldHover;
		if (hasGoal(TextGoal.HOVER_TEXT)
				&& (oldHover = section.getHoverAction()) != null) {
			JsonHover newHover = JsonHover.create(replaceTextInComponents(oldHover.getContents()));
			if (!oldHover.equals(newHover)) {
				sectionBuilder.hoverAction(newHover);
			}
		}
		JsonClick click;
		if (hasGoal(TextGoal.CLICK_VALUE)
				&& (click = section.getClickAction()) != null) {
			String oldValue = click.getValue();
			String newValue = apply(oldValue);
			if (!oldValue.equals(newValue)) {
				sectionBuilder.clickAction(JsonClick.create(click.getType(), newValue));
			}
		}
		JsonInsertion insertion;
		if (hasGoal(TextGoal.INSERTION_VALUE)
				&& (insertion = section.getInsertionAction()) != null) {
			String oldValue = insertion.getValue();
			String newValue = apply(oldValue);
			if (!oldValue.equals(newValue)) {
				sectionBuilder.insertionAction(JsonInsertion.create(newValue));
			}
		}
		return sectionBuilder.build();
	}
	
	private List<ChatComponent> replaceTextInComponents(List<ChatComponent> sourceContents) {
		if (sourceContents.isEmpty()) {
			return sourceContents;
		}
		boolean changedAny = false;
		List<ChatComponent> contents = new ArrayList<>(sourceContents.size());
		for (ChatComponent component : sourceContents) {

			ChatComponent result;
			String oldText = component.getText();
			String newText = apply(oldText);
			if (oldText.equals(newText)) {
				result = component;
			} else {
				result = new ChatComponent.Builder(component).text(newText).build();
				changedAny = true;
			}
			contents.add(result);
		}
		if (!changedAny) {
			return sourceContents;
		}
		return contents;
	}
	
}
