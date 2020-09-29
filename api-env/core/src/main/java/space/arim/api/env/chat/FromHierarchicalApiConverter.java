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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

/**
 * Hierarchy aware converter
 * 
 * @author A248
 *
 * @param <B> the type of the base component
 * @param <T> the type of the text component
 * @param <H> the type of the hover event
 * @param <C> the type of the click event
 */
abstract class FromHierarchicalApiConverter<B, T extends B, H, C> {
	
	private final List<T> components;
	
	private final SendableMessage.Builder parentBuilder = new SendableMessage.Builder();
	private JsonSection.Builder sectionBuilder;
	
	/*
	 * A list of parent components when recursing.
	 * 
	 * BungeeCord - Although some getters for colours and formatting use the parent component, Json events do not.
	 * Adventure - All hierarchical formatting must be handled by the caller.
	 */
	private final List<T> parents = new LinkedList<>();
	
	private JsonHover currentHover;
	private JsonClick currentClick;
	private JsonInsertion currentInsertion;
	
	private H currentPlatformHover;
	private C currentPlatformClick;
	private String currentPlatformInsertion;
	
	FromHierarchicalApiConverter(List<T> components) {
		this.components = List.copyOf(components);
	}
	
	List<ChatComponent> parseToContent() {
		parseToMessage(false);
		List<JsonSection> sections = parentBuilder.getSections();
		if (sections.isEmpty()) {
			return List.of();
		}
		List<ChatComponent> content = new ArrayList<>();
		for (JsonSection section : sections) {
			content.addAll(section.getContents());
		}
		return content;
	}
	
	SendableMessage parseToMessage() {
		parseToMessage(true);
		return parentBuilder.build();
	}
	
	<J> J getFirstNonNullFromHierarchy(T component, Function<T, J> getter) {
		J result = getter.apply(component);
		if (result != null) {
			return result;
		}
		for (T parent : parents) {
			result = getter.apply(parent);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	private void parseToMessage(boolean json) {
		parseToMessage(json, components);
		if (sectionBuilder != null) {
			parentBuilder.add(sectionBuilder.build());
		}
	}
	
	private void parseToMessage(boolean json, List<? extends B> components) {
		for (B baseComponent : components) {
			if (!getTextComponentClass().isInstance(baseComponent)) {
				continue;
			}
			T component = getTextComponentClass().cast(baseComponent);
			if (json) {
				H platformHover = getFirstNonNullFromHierarchy(component, this::getHoverEvent);
				C platformClick = getFirstNonNullFromHierarchy(component, this::getClickEvent);
				String platformInsertion = getFirstNonNullFromHierarchy(component, this::getInsertion);
				boolean mayNeedRebuild = platformHover != currentPlatformHover
						|| platformClick != currentPlatformClick
						|| platformInsertion != currentPlatformInsertion;
				currentPlatformHover = platformHover;
				currentPlatformClick = platformClick;
				currentPlatformInsertion = platformInsertion;
				if (mayNeedRebuild && rebuildJsonActions()) {
					if (sectionBuilder != null) {
						parentBuilder.add(sectionBuilder.build());
					}
					sectionBuilder = new JsonSection.Builder();
					sectionBuilder.hoverAction(currentHover);
					sectionBuilder.clickAction(currentClick);
					sectionBuilder.insertionAction(currentInsertion);
				}
			}
			sectionBuilder.addContent(convertComponent(component));

			parents.add(0, component);
			parseToMessage(json, getChildren(component));
			parents.remove(0);
		}
	}

	private boolean rebuildJsonActions() {
		JsonHover nextHover = convertHover(currentPlatformHover);
		boolean sameHover = Objects.equals(currentHover, nextHover);
		if (!sameHover) {
			currentHover = nextHover;
		}

		JsonClick nextClick = convertClick(currentPlatformClick);
		boolean sameClick = Objects.equals(currentClick, nextClick);
		if (!sameClick) {
			currentClick = nextClick;
		}

		JsonInsertion nextInsertion = (currentPlatformInsertion == null) ? null : JsonInsertion.create(currentPlatformInsertion);
		boolean sameInsertion = Objects.equals(currentInsertion, nextInsertion);
		if (!sameInsertion) {
			currentInsertion = nextInsertion;
		}

		return !sameHover || !sameClick || !sameInsertion;
	}
	
	abstract ChatComponent convertComponent(T component);
	
	abstract JsonClick convertClick(C clickEvent);
	
	abstract JsonHover convertHover(H hoverEvent);
	
	abstract H getHoverEvent(T component);
	
	abstract C getClickEvent(T component);
	
	abstract String getInsertion(T component);
	
	abstract Class<T> getTextComponentClass();
	
	abstract List<? extends B> getChildren(T component);

}
