/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.chat;

import java.util.List;

/**
 * Helper class for creating Messages.
 * 
 * @author A248
 *
 */
public class SimpleMessageBuilder extends AbstractMessageBuilder {
	
	private ComponentBuilder builder;
	
	/**
	 * Creates an empty builder
	 * 
	 */
	public SimpleMessageBuilder() {
		
	}
	
	/**
	 * Creates a builder with the given components
	 * 
	 * @param components the components
	 */
	public SimpleMessageBuilder(List<Component> components) {
		super(components);
	}
	
	@Override
	public SimpleMessageBuilder append(Component component) {
		reset();
		components.add(component);
		return this;
	}
	
	@Override
	public SimpleMessageBuilder append(Component...components) {
		reset();
		for (Component component : components) {
			this.components.add(component);
		}
		return this;
	}
	
	private ComponentBuilder freshenBuilder() {
		if (builder != null) {
			components.add(builder.build());
			builder = new ComponentBuilder(builder);
		} else {
			builder = new ComponentBuilder();
		}
		return builder;
	}
	
	@Override
	public SimpleMessageBuilder reset() {
		if (builder != null) {
			components.add(builder.build());
			builder = null;
		}
		return this;
	}
	
	@Override
	public SimpleMessageBuilder add(String text) {
		freshenBuilder().text(text);
		return this;
	}
	
	@Override
	public SimpleMessageBuilder colour(Colour colour) {
		if (builder == null || !builder.getColour().equals(colour)) {
			freshenBuilder().colour(colour);
		}
		return this;
	}
	
	@Override
	public SimpleMessageBuilder style(Style style) {
		if (builder == null || !builder.hasStyle(style)) {
			freshenBuilder().style(style);
		}
		return this;
	}
	
	@Override
	public SimpleMessageBuilder style(Style style, boolean enable) {
		return (SimpleMessageBuilder) super.style(style, enable);
	}
	
	@Override
	public SimpleMessageBuilder unstyle(Style style) {
		if (builder != null && builder.hasStyle(style)) {
			freshenBuilder().unstyle(style);
		}
		return this;
	}
	
	@Override
	public Message build() {
		return new Message(components.toArray(new Component[] {}));
	}

	@Override
	public String toString() {
		return "SimpleMessageBuilder [builder=" + builder + ", components=" + components + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((builder == null) ? 0 : builder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof SimpleMessageBuilder)) {
			return false;
		}
		SimpleMessageBuilder other = (SimpleMessageBuilder) obj;
		if (builder == null) {
			if (other.builder != null) {
				return false;
			}
		} else if (!builder.equals(other.builder)) {
			return false;
		}
		return true;
	}
	
}
