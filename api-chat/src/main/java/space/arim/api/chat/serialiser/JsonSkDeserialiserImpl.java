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
package space.arim.api.chat.serialiser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

class JsonSkDeserialiserImpl extends DeserialiserImpl {
	
	private static final Pattern DOUBLE_PIPES_PATTERN = Pattern.compile("||", Pattern.LITERAL);
	
	private final SendableMessage.Builder messageBuilder = new SendableMessage.Builder();
	
	private JsonSection.Builder sectionBuilder = new JsonSection.Builder();

	JsonSkDeserialiserImpl(String content) {
		super(content);
	}
	
	private List<String> getSegmentsIgnoringEscaped() {
		String content = content();
		String[] split = DOUBLE_PIPES_PATTERN.split(content);
		if (content.indexOf("||||") == -1) {
			return Arrays.asList(split);
		}
		List<CharSequence> segments = new ArrayList<>(split.length - 1);

		/*
		 * Objective: convert all |||| to ||
		 */
		mainLoop:
		for (int n = 0; n < split.length; n++) {
			int emptyCount = 0;
			while (split[n].isEmpty()) {
				emptyCount++;
				n++;
				if (n == split.length) {
					addDoublePipesToLastSegment(segments, emptyCount);
					break mainLoop;
				}
			}
			if (emptyCount > 0) {
				addDoublePipesToLastSegment(segments, emptyCount);
			}
			String preSegment = split[n];
			if (emptyCount % 2 == 0) {
				// Empty count is even - new segment
				segments.add(preSegment);
			} else {
				// Add to last segment
				int lastIndex = segments.size() - 1;
				segments.set(lastIndex, new StringBuilder(segments.get(lastIndex)).append(preSegment));
			}
		}
		// Build all unfinished StringBuilders
		for (ListIterator<CharSequence> it = segments.listIterator(); it.hasNext();) {
			it.set(it.next().toString());
		}
		@SuppressWarnings("unchecked")
		List<String> casted = (List<String>) ((List<? extends CharSequence>) segments);
		return casted;
	}
	
	private static void addDoublePipesToLastSegment(List<CharSequence> segments, int emptyCount) {
		/*
		 * |||| - 1 empty, 1 pipes
		 * |||| || - 2 empty, 1 pipes
		 * |||| |||| - 3 empty, 2 pipes
		 * |||| |||| || - 4 empty, 2 pipes
		 * |||| |||| |||| - 5 empty, 3 pipes
		 * 
		 * e - p = floor (e/2)
		 * p = e - floor (e/2)
		 */
		int doublePipeCount = emptyCount - (emptyCount / 2);
		StringBuilder pipesBuilder = new StringBuilder();
		for (int n = 0; n < doublePipeCount; n++) {
			pipesBuilder.append("||");
		}
		int lastIndex = segments.size() - 1;
		if (lastIndex >= 0) {
			segments.set(lastIndex, new StringBuilder(segments.get(lastIndex)).append(pipesBuilder));
		} else {
			segments.add(pipesBuilder);
		}
	}
	
	private void parseJson() {
		List<String> segments = getSegmentsIgnoringEscaped();
		for (String segment : segments) {
			if (segment.isEmpty()) {
				continue;
			}
			JsonTag tag = JsonTag.getTag(segment);
			if (tag == JsonTag.NONE || tag == JsonTag.NIL) {

				if (tag == JsonTag.NIL) {
					segment = segment.substring(4);
				}
				messageBuilder.add(sectionBuilder.build());
				sectionBuilder = new AllColourDeserialiserImpl(segment).deserialiseBuilder();

			} else if (!sectionBuilder.isEmpty()) {

				parseTag(tag, segment.substring(4));
			}
		}
		messageBuilder.add(sectionBuilder.build());
	}
	
	private void parseTag(JsonTag tag, String value) {
		switch (tag) {
		case TTP:
			JsonHover hover = JsonHover.create(new AllColourDeserialiserImpl(value).deserialiseBuilder().getContents());
			sectionBuilder.hoverAction(hover);
			break;
		case CMD:
			sectionBuilder.clickAction(JsonClick.runCommand(value));
			break;
		case SGT:
			sectionBuilder.clickAction(JsonClick.suggestCommand(value));
			break;
		case URL:
			sectionBuilder.clickAction(JsonClick.openUrl(value));
			break;
		case INS:
			sectionBuilder.insertionAction(JsonInsertion.create(value));
			break;
		default:
			throw new IllegalStateException("Unknown JsonTag " + tag);
		}
	}

	@Override
	SendableMessage deserialise() {
		parseJson();
		return messageBuilder.build();
	}
	
}
