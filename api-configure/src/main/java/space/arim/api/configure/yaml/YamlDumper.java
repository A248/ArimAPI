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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigSerialisable;

class YamlDumper implements AutoCloseable {

	private final Writer writer;
	
	private Map<String, Object> values;
	private Map<String, List<ConfigComment>> comments;
	
	YamlDumper(Path target) throws IOException {
		writer = Files.newBufferedWriter(target, StandardCharsets.UTF_8);
	}
	
	void dump(Map<String, Object> values, Map<String, List<ConfigComment>> comments) throws IOException {
		this.values = values;
		this.comments = comments;
		dump();
	}
	
	private void writeComments(List<ConfigComment> comments) throws IOException {
		if (comments != null) {
			for (ConfigComment comment : comments) {
				writer.append(comment.toCommentString('#')).append('\n');
			}
		}
	}
	
	private void dump() throws IOException {
		writeDepth(values, 0, new String[] {});
		writeComments(comments.get(""));
	}
	
	static StringBuilder flattenKeyChain(String[] keyChain) {
		StringBuilder builder = new StringBuilder();
		for (int n = 0; n < keyChain.length; n++) {
			if (n != 0) {
				builder.append('.');
			}
			builder.append(keyChain[n]);
		}
		return builder;
	}
	
	private static String flattenKeyChain(String[] keyChain, String currentKey) {
		if (keyChain.length == 0) {
			return currentKey;
		}
		return flattenKeyChain(keyChain).append('.').append(currentKey).toString();
	}
	
	private void writeDepth(Map<String, Object> values, int depth, String[] keyChain) throws IOException {
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			writeComments(comments.get(flattenKeyChain(keyChain, key)));
			// 2 leading spaces per depth
			for (int n = 0; n < (depth * 2); n ++) {
				writer.append(' ');
			}
			writer.append(key).append(':');
			if (value instanceof Map<?, ?>) {
				writer.append('\n');
				@SuppressWarnings("unchecked")
				Map<String, Object> subMap = (Map<String, Object>) value;

				String[] nextKeyChain = Arrays.copyOf(keyChain, keyChain.length + 1);
				nextKeyChain[keyChain.length] = key;
				writeDepth(subMap, depth + 1, nextKeyChain);
			} else {
				writeConfigString(value, depth);
				writer.append('\n');
			}
		}
	}
	
	private void writeConfigString(Object obj, final int depth) throws IOException {
		if (obj instanceof Object[]) {
			writeList(List.of((Object[]) obj), depth);
			return;
		}
		if (obj instanceof List<?>) {
			writeList((List<?>) obj, depth);
			return;
		}
		if (obj instanceof Collection<?>) {
			// Using null in config objects has never been supported anyway
			writeList(List.copyOf((Collection<?>) obj), depth);
			return;
		}
		writer.append(' ').append(toConfigString(obj));
	}
	
	private void writeList(List<?> list, final int depth) throws IOException {
		for (Object e : list) {
			writer.append('\n');
			// 2 spaces per depth + 2 initial spaces per element
			for (int n = -2; n < depth; n++) {
				writer.append(' ');
			}
			writer.append("- ").append(toConfigString(e));
		}
	}
	
	private static CharSequence toConfigString(Object obj) {
		if (obj instanceof Number || obj instanceof Boolean) {
			return obj.toString();
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
