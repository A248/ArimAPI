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

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

/**
 * Utility for manipulating the text of {@link SendableMessage} instances. <br>
 * <br>
 * A manipulator is created for a {@link SendableMessage} and a set of {@link TextGoal}. When an operation
 * occurs, it is only applied to strings matching the goals. <br>
 * <br>
 * Unless otherwise noted, passing a null parameter, null collection element, or null array element to any method
 * in this class throws {@code NullPointerException}
 * 
 * @author A248
 *
 */
public final class SendableMessageManipulator {

	private final SendableMessage message;
	private final Set<TextGoal> goals;
	
	private SendableMessageManipulator(SendableMessage message, Set<TextGoal> goals) {
		this.message = Objects.requireNonNull(message, "message");
		this.goals = Set.copyOf(goals);
	}
	
	/**
	 * Creates an instance targeting all text goals
	 * 
	 * @param message the message to manipulate
	 * @return the message manipulator
	 */
	public static SendableMessageManipulator create(SendableMessage message) {
		return create(message, TextGoal.ALL_GOALS);
	}
	
	/**
	 * Creates an instance targeting the specified text goals
	 * 
	 * @param message the message to manipulate
	 * @param goals the text goals to target
	 * @return the message manipulator
	 */
	public static SendableMessageManipulator create(SendableMessage message, TextGoal...goals) {
		return create(message, Set.of(goals));
	}
	
	/**
	 * Creates an instance targeting the specified text goals
	 * 
	 * @param message the message to manipulate
	 * @param goals the text goals to target
	 * @return the message manipulator
	 */
	public static SendableMessageManipulator create(SendableMessage message, Set<TextGoal> goals) {
		return new SendableMessageManipulator(message, goals);
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
	 * Gets the immutable set of goals determing the text this manipulator targets
	 * 
	 * @return the text goals to determine the text to be manipulated
	 */
	public Set<TextGoal> getGoals() {
		return goals;
	}
	
	/**
	 * Possible parts of a {@link SendableMessage} containing strings
	 * 
	 * @author A248
	 *
	 */
	public enum TextGoal {
		/**
		 * Text of components ({@link ChatComponent#getText()}) in {@link JsonSection#getContents()}
		 * 
		 */
		SIMPLE_TEXT,
		/**
		 * Text of components ({@link ChatComponent#getText()}) in {@link JsonHover#getContents()}
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
		public static final Set<TextGoal> ALL_GOALS = Set.of(values());
		
	}
	
	/**
	 * Derives another manipulator for another message, using the same goals as in this manipulator
	 * 
	 * @param message the message to manipulate
	 * @return a sendable message manipulator for the specified message with the same goals as this one
	 */
	public SendableMessageManipulator deriveManipulator(SendableMessage message) {
		return new SendableMessageManipulator(message, goals);
	}
	
	/*
	 * 
	 * Replacement
	 * 
	 */
	
	/**
	 * Replaces text in the message using the specified operator and returns a new message with
	 * the text replaced.
	 * 
	 * @param operator the operator used to replace content
	 * @return a manipulator wrapping the new message with the text replaced according to the operator
	 */
	public SendableMessage replaceText(Function<? super String, String> operator) {
		return new Replacer(this, operator).replace();
	}
	
	/**
	 * Replaces all text matching the regex pattern with the specified replacement
	 * 
	 * @param pattern the regex pattern
	 * @param replacement the replacement character sequence
	 * @return a manipulator wrapping the new message with text matched the specified pattern replaced with the replacement
	 */
	public SendableMessage replaceText(Pattern pattern, String replacement) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(replacement, "replacement");
		return replaceText((str) -> pattern.matcher(str).replaceAll(replacement));
	}
	
	/**
	 * Replaces all text matching the literal string with the specified replacement
	 * 
	 * @param text the text to replace
	 * @param replacement the replacement character sequence
	 * @return a manipulator wrapping the new message with text matched the specified text replaced with the replacement
	 */
	public SendableMessage replaceText(CharSequence text, CharSequence replacement) {
		Objects.requireNonNull(text, "text");
		Objects.requireNonNull(replacement, "replacement");
		return replaceText((str) -> str.replace(text, replacement));
	}

	/*
	 * 
	 * Predicate evaluation
	 * 
	 */
	
	/**
	 * Checks if any text contains the specified char sequence
	 * 
	 * @param text the car sequence to check for
	 * @return true if any text matched by the specified goals contains the specified string
	 */
	public boolean contains(CharSequence text) {
		Objects.requireNonNull(text, "text");
		return evaluate((str) -> str.contains(text));
	}
	
	/**
	 * Checks if any text matches the specified predicate
	 * 
	 * @param predicate the predicate used to perform the evaluation
	 * @return true if any text matched by the specified goals matched the predicate
	 */
	public boolean evaluate(Predicate<? super String> predicate) {
		return new Evaluator(this, predicate).evaluate();
	}
	
}
