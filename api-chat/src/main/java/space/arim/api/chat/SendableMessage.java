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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A fully formed, immutable message
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public final class SendableMessage implements SendableMessageInfo {

	private final List<JsonSection> sections;
	
	private static final SendableMessage EMPTY = new SendableMessage(List.of());
	
	private SendableMessage(List<JsonSection> sections) {
		this.sections = List.copyOf(sections);
	}
	
	/**
	 * Creates from a list of {@link JsonSection}s comprising this message
	 * 
	 * @param sections the sections of this message
	 * @return the sendable message
	 * @throws NullPointerException if {@code sections} or an element in it is null
	 */
	public static SendableMessage create(List<JsonSection> sections) {
		List<JsonSection> sectionsCopy = new ArrayList<>(sections);
		if (sectionsCopy.isEmpty()) {
			return EMPTY;
		}
		Compactions.compactSections(sectionsCopy);
		return new SendableMessage(sectionsCopy);
	}
	
	/**
	 * Creates from an array of {@link JsonSection}s comprising this message
	 * 
	 * @param sections the sections of this message
	 * @return the sendable message
	 * @throws NullPointerException if {@code sections} or an element in it is null
	 */
	public static SendableMessage create(JsonSection...sections) {
		if (sections.length == 0) {
			return EMPTY;
		}
		return create(List.of(sections));
	}
	
	/**
	 * Creates from {@code SendableMessageInfo}. The sections of such info are used
	 * in this one.
	 * 
	 * @param info the message info to use
	 * @return the sendable message
	 */
	public static SendableMessage create(SendableMessageInfo info) {
		if (info instanceof SendableMessage) {
			return (SendableMessage) info;
		}
		return create(info.getSections());
	}
	
	/**
	 * Creates an empty message
	 * 
	 * @return an empty sendable message
	 */
	public static SendableMessage empty() {
		return EMPTY;
	}
	
	/**
	 * Gets the immutable sections which comprise this sendable message. Attempts to mutate
	 * the list will throw {@code UnsupportedOperationException}
	 * 
	 */
	@Override
	public List<JsonSection> getSections() {
		return sections;
	}
	
	/**
	 * Creates a new {@code SendableMessage} which is the concatenation of this one with a
	 * specified another.
	 * 
	 * @param other the other message to concatenate
	 * @return the combined result
	 */
	public SendableMessage concatenate(SendableMessage other) {
		if (isEmpty()) {
			return other;
		}
		if (other.isEmpty()) {
			return this;
		}
		List<JsonSection> resultSections = new ArrayList<>(sections.size() + other.sections.size());
		resultSections.addAll(sections);
		resultSections.addAll(other.sections);
		Compactions.compactSections(resultSections);
		return new SendableMessage(resultSections);
	}
	
	@Override
	public String toString() {
		return "SendableMessage [sections=" + sections + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sections.hashCode();
		return result;
	}

	/**
	 * Determines equality with another object consistent with the visual output of this message
	 * 
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof SendableMessage)) {
			return false;
		}
		SendableMessage other = (SendableMessage) object;
		return sections.equals(other.sections);
	}

	/**
	 * Builder to create {@link SendableMessage}
	 * 
	 * @author A248
	 *
	 */
	public static class Builder implements SendableMessageInfo {
		
		private final List<JsonSection> sections;
		private transient final List<JsonSection> sectionsView;
		
		private Builder(List<JsonSection> sections) {
			this.sections = sections;
			sectionsView = Collections.unmodifiableList(sections);
		}
		
		/**
		 * Creates the builder
		 * 
		 */
		public Builder() {
			this(new ArrayList<>());
		}
		
		/**
		 * Creates, using the provided existing {@code SendableMessageInfo}, whose sections
		 * are copied to this builder
		 * 
		 * @param info the message info to use
		 */
		public Builder(SendableMessageInfo info) {
			this(new ArrayList<>(List.copyOf(info.getSections())));
		}
		
		/**
		 * Adds the specified section to this builder.
		 * 
		 * @param section the section, must not be null
		 * @return this builder
		 * @throws NullPointerException if {@code section} is null
		 */
		public Builder add(JsonSection section) {
			sections.add(Objects.requireNonNull(section, "section"));
			return this;
		}
		
		/**
		 * Adds the specified sections to this builder
		 * 
		 * @param sections the section array
		 * @return this builder
		 * @throws NullPointerException if {@code sections} or an element in it is null
		 */
		public Builder add(JsonSection...sections) {
			this.sections.addAll(List.of(sections));
			return this;
		}
		
		/**
		 * Adds the specified sections to this builder
		 * 
		 * @param sections the section list
		 * @return this builder
		 * @throws NullPointerException if {@code sections} or an element in it is null
		 */
		public Builder add(List<JsonSection> sections) {
			this.sections.addAll(List.copyOf(sections));
			return this;
		}

		@Override
		public List<JsonSection> getSections() {
			Compactions.compactSections(sections);
			return sectionsView;
		}
		
		/**
		 * Builds into a full message
		 * 
		 * @return the built message
		 */
		public SendableMessage build() {
			return SendableMessage.create(this);
		}

		@Override
		public String toString() {
			return "SendableMessage.Builder [sections=" + sections + "]";
		}
		
		private List<JsonSection> sectionsCompact() {
			Compactions.compactSections(sections);
			return sections;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + sectionsCompact().hashCode();
			return result;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (!(object instanceof Builder)) {
				return false;
			}
			Builder other = (Builder) object;
			return sectionsCompact().equals(other.sectionsCompact());
		}
		
	}
	
}
