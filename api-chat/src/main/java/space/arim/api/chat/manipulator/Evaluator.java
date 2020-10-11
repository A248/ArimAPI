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

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.manipulator.SendableMessageManipulator.TextGoal;

class Evaluator extends ManipulationFeature {

	private final Predicate<? super String> predicate;
	
	Evaluator(SendableMessageManipulator manipulator, Predicate<? super String> predicate) {
		super(manipulator);
		this.predicate = Objects.requireNonNull(predicate, "predicate");
	}
	
	boolean evaluate() {
		if (isNoOp()) {
			return false;
		}
		for (JsonSection section : message().getSections()) {
			if (hasGoal(TextGoal.SIMPLE_TEXT)
					&& evaluateTextInComponents(section.getContents())) {
				return true;
			}
			JsonHover hover;
			if (hasGoal(TextGoal.HOVER_TEXT)
					&& (hover = section.getHoverAction()) != null
					&& evaluateTextInComponents(hover.getContents())) {
				return true;
			}
			JsonClick click;
			if (hasGoal(TextGoal.CLICK_VALUE)
					&& (click = section.getClickAction()) != null
					&& predicate.test(click.getValue())) {
				return true;
			}
			JsonInsertion insertion;
			if (hasGoal(TextGoal.INSERTION_VALUE)
					&& (insertion = section.getInsertionAction()) != null
					&& predicate.test(insertion.getValue())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean evaluateTextInComponents(List<ChatComponent> contents) {
		for (ChatComponent content : contents) {
			if (predicate.test(content.getText())) {
				return true;
			}
		}
		return false;
	}
	
}
