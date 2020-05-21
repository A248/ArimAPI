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

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * A sendable message comprised of an array of {@link Component} or {@link JsonComponent} objects. <br>
 * <br>
 * Each <code>Component</code> is considered distinct. No formatting from the first <code>Component</code> affects the next. <br>
 * Accordingly, no {@link Format#RESET} codes need be applied at the start or end of a <code>Component</code>.
 * 
 * @author A248
 *
 */
public class Message {

	private final Component[] components;
	
	public Message(Component...components) {
		this.components = components;
	}
	
	private Message convertEach(UnaryOperator<Component> converter) {
		Component[] converted = new Component[components.length - 1];
		for (int n = 0; n < components.length; n++) {
			converted[n] = converter.apply(components[n]);
		}
		return new Message(converted);
	}
	
	/**
	 * Gets all the components which comprise this message. <br>
	 * Some or all the components may be json components if the message is a JSON message.
	 * 
	 * @return the array of components
	 */
	public Component[] getComponents() {
		return Arrays.copyOf(components, components.length);
	}
	
	/**
	 * Removes internal <code>JsonComponent</code>s without any Json features, replacing them with normal <code>Component</code>s. <br>
	 * Makes no functional changes. See {@link JsonMessageBuilder#cleanBuild()}.
	 * 
	 * @return a fresh Message cleaned of unnecessary <code>JsonComponent</code>s
	 */
	public Message clean() {
		return convertEach((component) -> (component instanceof JsonComponent && !((JsonComponent) component).hasAnyJsonFeatures()) ? ((JsonComponent) component).stripJson() : component);
	}
	
	/**
	 * Creates a new Message with all colour formatting removed
	 * 
	 * @return a fresh Message with colour removed
	 */
	public Message stripColour() {
		return convertEach((component) -> component.stripColour());
	}
	
	/**
	 * Creates a new Message with all styles formatting removed
	 * 
	 * @return a fresh Message with styles removed
	 */
	public Message stripStyles() {
		return convertEach((component) -> component.stripStyles());
	}
	
	/**
	 * Creates a new Message with all JSON formatting removed
	 * 
	 * @return a fresh Message with JSON removed
	 */
	public Message stripJson() {
		return convertEach((component) -> (component instanceof JsonComponent) ? ((JsonComponent) component).stripJson() : component);
	}
	
	/**
	 * Creates a new Message with all colour, styles, and JSON formatting removed. <br>
	 * 
	 * @return a fresh Message with colour, styles, and JSON removed
	 */
	public Message stripAll() {
		return convertEach((component) -> component.stripAll());
	}
	
	/**
	 * Creates a new Message with the specified content appended.
	 * 
	 * @param component the component to add
	 * @return a combined Message
	 */
	public Message concat(Component component) {
		if (component == null || component.isEmpty()) {
			return this;
		}
		Component[] result = Arrays.copyOf(components, components.length + 1);
		result[components.length] = component;
		return new Message(result);
	}
	
	/**
	 * Creates a new Message with the specified content appended.
	 * 
	 * @param components the components to add
	 * @return a combined Message
	 */
	public Message concat(Component...components) {
		if (components == null || components.length == 0) {
			return this;
		}
		Component[] result = Arrays.copyOf(this.components, this.components.length + components.length);
		for (int n = this.components.length; n < result.length; n++) {
			result[n] = components[n];
		}
		return new Message(result);
	}
	
	/**
	 * Creates a new Message with the content of the specified Message appended.
	 * 
	 * @param message the message to add
	 * @return a combined Message
	 */
	public Message concat(Message message) {
		return concat(message.getComponents());
	}
	
	/**
	 * Identical to {@link SimpleMessageBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class SimpleBuilder extends SimpleMessageBuilder {
		
		/**
		 * Creates an empty builder
		 * 
		 */
		public SimpleBuilder() {
			
		}
		
		/**
		 * Creates a builder with the given components
		 * 
		 * @param components the components
		 */
		public SimpleBuilder(List<Component> components) {
			super(components);
		}
		
	}
	
	/**
	 * Identical to {@link JsonMessageBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class JsonBuilder extends JsonMessageBuilder {
		
		/**
		 * Creates an empty builder
		 * 
		 */
		public JsonBuilder() {
			
		}
		
		/**
		 * Creates a builder with the given components
		 * 
		 * @param components the components
		 */
		public JsonBuilder(List<Component> components) {
			super(components);
		}
		
	}
	
	@Override
	public int hashCode() {
		return 31 + Arrays.hashCode(components);
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof Message && Arrays.equals(((Message) object).components, components);
	}
	
	@Override
	public String toString() {
		return "Message [components=" + Arrays.toString(components) + "]";
	}
	
}
