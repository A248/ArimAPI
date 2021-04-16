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
 * A section of a message with JSON formatting enabled
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public final class JsonSection implements JsonSectionInfo {

	private final List<ChatComponent> contents;
	private final JsonHover hoverAction;
	private final JsonClick clickAction;
	private final JsonInsertion insertionAction;
	
	private static final JsonSection EMPTY = new JsonSection(List.of(), null, null, null);
	
	private JsonSection(List<ChatComponent> contents, JsonHover hoverAction, JsonClick clickAction,
			JsonInsertion insertionAction) {
		this.contents = List.copyOf(contents);
		this.hoverAction = hoverAction;
		this.clickAction = clickAction;
		this.insertionAction = insertionAction;
	}
	
	private static JsonSection create0(List<ChatComponent> sourceContents, JsonHover hoverAction,
			JsonClick clickAction, JsonInsertion insertionAction) {
		List<ChatComponent> contents = new ArrayList<>(sourceContents);
		if (contents.isEmpty()) {
			return EMPTY;
		}
		Compactions.compactComponents(contents);
		return new JsonSection(contents, hoverAction, clickAction, insertionAction);
	}
	
	/**
	 * Creates from {@code JsonSectionInfo}. The attributes of the json section info are copied.
	 * 
	 * @param info the component info whose attributes to use
	 * @return the json section
	 */
	public static JsonSection create(JsonSectionInfo info) {
		if (info instanceof JsonSection) {
			return (JsonSection) info;
		}
		return create0(info.getContents(), info.getHoverAction(), info.getClickAction(), info.getInsertionAction());
	}
	
	/**
	 * Creates a json section without any json attributes
	 * 
	 * @param contents the component contents
	 * @return the json section
	 * @throws NullPointerException if {@code contents} or an element in it is null
	 */
	public static JsonSection create(List<ChatComponent> contents) {
		return create0(contents, null, null, null);
	}
	
	@Override
	public List<ChatComponent> getContents() {
		return contents;
	}
	
	@Override
	public JsonHover getHoverAction() {
		return hoverAction;
	}
	
	@Override
	public JsonClick getClickAction() {
		return clickAction;
	}
	
	@Override
	public JsonInsertion getInsertionAction() {
		return insertionAction;
	}
	
	@Override
	public String toString() {
		return "JsonSection [contents=" + contents + ", hoverAction=" + hoverAction + ", clickAction=" + clickAction
				+ ", insertionAction=" + insertionAction + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contents.hashCode();
		result = prime * result + Objects.hashCode(hoverAction);
		result = prime * result + Objects.hashCode(clickAction);
		result = prime * result + Objects.hashCode(insertionAction);
		return result;
	}

	/**
	 * Determines equality with another object consistent with the visual output of this section
	 * 
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof JsonSection)) {
			return false;
		}
		JsonSection other = (JsonSection) object;
		return contents.equals(other.contents)
				&& Objects.equals(hoverAction, other.hoverAction)
				&& Objects.equals(clickAction, other.clickAction)
				&& Objects.equals(insertionAction, other.insertionAction);
	}

	/**
	 * Builder for creating JSON sections
	 * 
	 * @author A248
	 *
	 */
	public static class Builder implements JsonSectionInfo {
		
		private final List<ChatComponent> contents;
		private transient final List<ChatComponent> contentsView;
		private JsonHover hoverAction;
		private JsonClick clickAction;
		private JsonInsertion insertionAction;
		
		private Builder(List<ChatComponent> contents, JsonHover hoverAction, JsonClick clickAction,
				JsonInsertion insertionAction) {
			this.contents = contents;
			this.hoverAction = hoverAction;
			this.clickAction = clickAction;
			this.insertionAction = insertionAction;
			contentsView = Collections.unmodifiableList(contents);
		}
		
		/**
		 * Creates the builder without any existing formatting
		 * 
		 */
		public Builder() {
			this(new ArrayList<>(), null, null, null);
		}
		
		/**
		 * Creates the builder, using the provided existing {@code JsonSectionInfo}, whose attributes
		 * are copied to this builder.
		 * 
		 * @param info the json section info to use for default values
		 */
		public Builder(JsonSectionInfo info) {
			this(new ArrayList<>(List.copyOf(info.getContents())),
					info.getHoverAction(), info.getClickAction(), info.getInsertionAction());
		}
		
		/**
		 * Sets the contents of this builder to the specified components
		 * 
		 * @param contents the contents to add
		 * @return this builder
		 * @throws NullPointerException if {@code components} or an element in it is null
		 */
		public Builder contents(ChatComponent...contents) {
			this.contents.clear();
			this.contents.addAll(List.of(contents));
			return this;
		}
		
		/**
		 * Sets the contents of this builder to the specified components
		 * 
		 * @param contents the contents
		 * @return this builder
		 * @throws NullPointerException if {@code components} or an element in it is null
		 */
		public Builder contents(List<ChatComponent> contents) {
			this.contents.clear();
			this.contents.addAll(List.copyOf(contents));
			return this;
		}
		
		/**
		 * Adds the specified component to this builder.
		 * 
		 * @param content the component to add
		 * @return this builder
		 * @throws NullPointerException if {@code content} is null
		 */
		public Builder addContent(ChatComponent content) {
			contents.add(Objects.requireNonNull(content, "content"));
			return this;
		}
		
		/**
		 * Adds the specified components to this builder.
		 * 
		 * @param contents the components to add
		 * @return this builder
		 * @throws NullPointerException if {@code contents} or an element in it is null
		 */
		public Builder addContent(ChatComponent...contents) {
			this.contents.addAll(List.of(contents));
			return this;
		}
		
		/**
		 * Adds the specified components to this builder.
		 * 
		 * @param contents the components to add
		 * @return this builder
		 * @throws NullPointerException if {@code contenst} or an element in it is null
		 */
		public Builder addContent(List<ChatComponent> contents) {
			this.contents.addAll(List.copyOf(contents));
			return this;
		}
		
		/**
		 * Sets the hover action of this builder to the specified hover action
		 * 
		 * @param hoverAction the new hover action, or {@code null} for none
		 * @return the builder
		 */
		public Builder hoverAction(JsonHover hoverAction) {
			this.hoverAction = hoverAction;
			return this;
		}
		
		/**
		 * Sets the click action of this builder to the specified click action
		 * 
		 * @param clickAction the new click action, or {@code null} for none
		 * @return the builder
		 */
		public Builder clickAction(JsonClick clickAction) {
			this.clickAction = clickAction;
			return this;
		}
		
		/**
		 * Sets the insertion action of this builder to the specified insertion action
		 * 
		 * @param insertionAction the new insertion action, or {@code null} for none
		 * @return the builder
		 */
		public Builder insertionAction(JsonInsertion insertionAction) {
			this.insertionAction = insertionAction;
			return this;
		}
		
		@Override
		public List<ChatComponent> getContents() {
			Compactions.compactComponents(contents);
			return contentsView;
		}

		@Override
		public JsonHover getHoverAction() {
			return hoverAction;
		}

		@Override
		public JsonClick getClickAction() {
			return clickAction;
		}

		@Override
		public JsonInsertion getInsertionAction() {
			return insertionAction;
		}
		
		/**
		 * Creates a {@link JsonSection} from the details of this builder
		 * 
		 * @return the built JSON section
		 */
		public JsonSection build() {
			return JsonSection.create(this);
		}

		@Override
		public String toString() {
			return "Builder [contents=" + contents + ", hoverAction=" + hoverAction + ", clickAction=" + clickAction
					+ ", insertionAction=" + insertionAction + "]";
		}
		
		private List<ChatComponent> contentsCompact() {
			Compactions.compactComponents(contents);
			return contents;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + contentsCompact().hashCode();
			result = prime * result + Objects.hashCode(hoverAction);
			result = prime * result + Objects.hashCode(clickAction);
			result = prime * result + Objects.hashCode(insertionAction);
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
			return contentsCompact().equals(other.contentsCompact())
					&& Objects.equals(hoverAction, other.hoverAction)
					&& Objects.equals(clickAction, other.clickAction)
					&& Objects.equals(insertionAction, other.insertionAction);
		}
		
	}

}
