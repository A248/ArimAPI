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
import java.util.Set;
import java.util.function.UnaryOperator;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

/**
 * Utility for manipulating {@link SendableMessage} instances. <br>
 * <br>
 * Both {@link #getInstance()} and the constructor may be used.
 * 
 * @author A248
 *
 */
public class SendableMessageManipulator {

	private final SendableMessage message;
	
	/**
	 * Creates an instance
	 * 
	 * @param message the message to manipulate
	 */
	public SendableMessageManipulator(SendableMessage message) {
		this.message = message;
	}
	
	/**
	 * Gets the message this manipulator is for
	 * 
	 * @return the message
	 */
	public SendableMessage getMessage() {
		return message;
	}
	
	/**
	 * Sums the length of all the text of the {@link ChatComponent}s of this message. This method
	 * works by adding the length of {@link ChatComponent#getText()} from each component.
	 * 
	 * @return the length of all text in the components of this message
	 */
	public int length() {
		int length = 0;
		for (JsonSection section : message.getSections()) {
			for (ChatComponent component : section.getContents()) {
				length += component.getText().length();
			}
		}
		return length;
	}
	
	/**
	 * Possible parts of a {@link SendableMessage} which can be somehow replaced via string manipulation
	 * 
	 * @author A248
	 *
	 */
	public enum ReplaceGoal {
		/**
		 * Text of components ({@link ChatComponent#getText()}) in {@link JsonSection#getContents()}
		 * 
		 */
		SIMPLE_TEXT,
		/**
		 * Text of components ({@link ChatComponent#getText()}) in {@link JsonHover#getContent()}
		 * 
		 */
		HOVER_TEXT,
		/**
		 * Value in {@link JsonClick#getValue()}
		 * 
		 */
		CLICK_VALUE,
		/**
		 * Insertion value in {@link JsonInsertion#getValue()}
		 * 
		 */
		INSERTION_VALUE;
		
		/**
		 * An immutable set consisting of all replace goals
		 * 
		 */
		public static final Set<ReplaceGoal> ALL_GOALS = Set.of(values());
		
	}
	
	/**
	 * Replaces all text in the message using the specified operator and returns a new message with
	 * the text replaced. Equivalent to <code>replaceText(operator, ReplaceGoal.ALL_GOALS)</code>
	 * 
	 * @param operator the operator used to replace content
	 * @return the new message with the text replaced according to the operator
	 * @throws NullPointerException if {@code operator} is null
	 */
	public SendableMessage replaceText(UnaryOperator<String> operator) {
		return replaceText(operator, ReplaceGoal.ALL_GOALS);
	}
	
	/**
	 * Replaces all text in the message using the specified operator and returns a new message with
	 * the text replaced. Only strings targeted the specified goals are repalced.
	 * 
	 * @param operator the operator used to replace content
	 * @param goals the specific parts of the message to replace
	 * @return the new message with the text replaced according to the operator
	 * @throws NullPointerException if {@code operator}, {@code goals}, or an element in {@code goals} is null
	 */
	public SendableMessage replaceText(UnaryOperator<String> operator, ReplaceGoal...goals) {
		return replaceText(operator, Set.of(goals));
	}
	
	/**
	 * Replaces all text in the message using the specified operator and returns a new message with
	 * the text replaced  Only strings targeted the specified goals are repalced.
	 * 
	 * @param operator the operator used to replace content
	 * @param goals the specific parts of the message to replace
	 * @return the new message with the text replaced according to the operator
	 * @throws NullPointerException if {@code operator}, {@code goals}, or an element in {@code goals} is null
	 */
	public SendableMessage replaceText(UnaryOperator<String> operator, Set<ReplaceGoal> goals) {
		return replaceText0(Objects.requireNonNull(operator, "operator"), Set.copyOf(goals));
	}
	
	private static String apply(UnaryOperator<String> operator, String value) {
		return Objects.requireNonNull(operator.apply(value), "operator returned null");
	}
	
	private SendableMessage replaceText0(final UnaryOperator<String> operator, final Set<ReplaceGoal> goals) {
		if (goals.isEmpty() || message.isEmpty()) {
			return message;
		}
		boolean changedAny = false;
		List<JsonSection> sections = new ArrayList<>(message.getSections().size());
		for (JsonSection section : message.getSections()) {

			JsonSection.Builder sectionBuilder = new JsonSection.Builder(section);

			if (goals.contains(ReplaceGoal.SIMPLE_TEXT)) {
				List<ChatComponent> oldContents = section.getContents();
				List<ChatComponent> newContents = replaceTextInComponents(oldContents, operator);
				if (!oldContents.equals(newContents)) {
					sectionBuilder.contents(newContents);
				}
			}
			JsonHover hover;
			if (goals.contains(ReplaceGoal.HOVER_TEXT) && (hover = section.getHoverAction()) != null) {
				List<ChatComponent> oldContents = hover.getContents();
				List<ChatComponent> newContents = replaceTextInComponents(oldContents, operator); 
				if (!oldContents.equals(newContents)) {
					sectionBuilder.hoverAction(JsonHover.create(newContents));
				}
			}
			JsonClick click;
			if (goals.contains(ReplaceGoal.CLICK_VALUE) && (click = section.getClickAction()) != null) {
				String oldValue = click.getValue();
				String newValue = apply(operator, oldValue);
				if (!oldValue.equals(newValue)) {
					sectionBuilder.clickAction(JsonClick.create(click.getType(), newValue));
				}
			}
			JsonInsertion insertion;
			if (goals.contains(ReplaceGoal.INSERTION_VALUE) && (insertion = section.getInsertionAction()) != null) {
				String oldValue = insertion.getValue();
				String newValue = apply(operator, oldValue);
				if (!oldValue.equals(newValue)) {
					sectionBuilder.insertionAction(JsonInsertion.create(newValue));
				}
			}

			JsonSection built = sectionBuilder.build();
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
			return message;
		}
		return SendableMessage.create(sections);
	}
	
	private static List<ChatComponent> replaceTextInComponents(List<ChatComponent> sourceContents,
			UnaryOperator<String> operator) {
		if (sourceContents.isEmpty()) {
			return sourceContents;
		}
		boolean changedAny = false;
		List<ChatComponent> contents = new ArrayList<>(sourceContents.size());
		for (ChatComponent component : sourceContents) {

			ChatComponent result;
			String oldText = component.getText();
			String newText = apply(operator, oldText);
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
