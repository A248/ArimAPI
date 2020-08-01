/* 
 * ArimAPI-configure
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-configure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-configure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-configure. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure.yaml;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigSerialisable;

class CommentedYamlDumper implements AutoCloseable {

	private final Writer writer;
	
	private Map<String, Object> values;
	private List<ConfigComment> commentHeader;
	
	CommentedYamlDumper(Path target) throws IOException {
		writer = Files.newBufferedWriter(target, StandardCharsets.UTF_8);
	}
	
	void dump(Map<String, Object> values, List<ConfigComment> commentHeader) throws IOException {
		this.values = values;
		this.commentHeader = commentHeader;
		dump();
	}
	
	private void dump() throws IOException {
		for (ConfigComment comment : commentHeader) {
			writer.append(comment.toCommentString('#')).append('\n');
		}
		writeDepth(values, 0);
	}
	
	private void writeDepth(Map<String, Object> values, int depth) throws IOException {
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			for (int n = 0; n < depth; n ++) {
				// 2 spaces per depth
				writer.append("  ");
			}
			writer.append(key).append(':');
			if (value instanceof Map<?, ?>) {
				writer.append('\n');
				@SuppressWarnings("unchecked")
				Map<String, Object> subMap = (Map<String, Object>) value;
				writeDepth(subMap, depth + 1);
			} else {
				writer.append(' ').append(toConfigString(value)).append('\n');
			}
		}
	}
	
	private static CharSequence toConfigString(Object obj) {
		if (obj instanceof Number || obj instanceof Boolean) {
			return obj.toString();
		}
		if (obj instanceof List<?>) {
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			Object[] elements = ((List<?>) obj).toArray();
			for (int n = 0; n < elements.length; n++) {
				if (n != 0) {
					builder.append(", ");
				}
				builder.append(toConfigString(elements[n]));
			}
			return builder.append(']');
		}
		if (obj instanceof Collection<?>) {
			return toConfigString(new ArrayList<>((Collection<?>) obj));
		}
		CharSequence str0;
		if (obj instanceof ConfigSerialisable) {
			str0 = ((ConfigSerialisable) obj).toConfigString();
		} else {
			str0 = obj.toString();
		}
		return new StringBuilder().append('\'').append(str0).append('\'');
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
	
}
