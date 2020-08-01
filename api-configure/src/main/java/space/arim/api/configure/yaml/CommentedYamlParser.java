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
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ValueTransformer;
import space.arim.api.configure.impl.SimpleConfigData;

class CommentedYamlParser implements AutoCloseable {

	private final PushbackReader reader;
	private final Yaml yaml = new Yaml();
	
	private Map<String, Object> values;
	private final List<ConfigComment> comments = new ArrayList<>();
	
	CommentedYamlParser(Path source) throws IOException {
		reader = new PushbackReader(Files.newBufferedReader(source, StandardCharsets.UTF_8));
	}
	
	@SuppressWarnings("unchecked")
	ConfigData parse(List<? extends ValueTransformer> transformers) throws IOException, YamlSyntaxException {
		// The amount of whitespace preceding a comment
		int whitespace = 0;
		// Nonnull indicates found '#' and adding chars to builder
		StringBuilder commentBuilder = null;
		readLoop:
		for (;;) {
			int character = reader.read();
			if (character == -1) {
				// End of stream, cleanup
				if (commentBuilder != null) {
					comments.add(new ConfigComment(whitespace, commentBuilder.toString()));
				}
				return new SimpleConfigData(Map.of(), comments);
			}
			if (commentBuilder == null) {
				if (character == ' ') {
					whitespace++;
				} else if (character == '\n' || character == '\r') {
					whitespace = 0;
				} else if (character == '#') {
					commentBuilder = new StringBuilder();
				} else {
					reader.unread(character);
					for (int n = 0; n < whitespace; n++) {
						reader.unread(' ');
					}
					break readLoop;
				}
			} else {
				if (character == '\n') {
					comments.add(new ConfigComment(whitespace, commentBuilder.toString()));
					whitespace = 0;
					commentBuilder = null;
				} else {
					commentBuilder.append((char) character);
				}
			}
		}
		Map<String, Object> parsed;
		try {
			parsed = yaml.loadAs(reader, Map.class);
		} catch (YAMLException ex) {
			throw new YamlSyntaxException("From SnakeYAML", ex);
		}
		values = transform(parsed, transformers);
		return new SimpleConfigData(values, comments);
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> transform(Map<String, Object> values, List<? extends ValueTransformer> transformers) {
		Map<String, Object> transformed = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map<?, ?>) {
				transformed.put(key, transform((Map<String, Object>) value, transformers));
			} else {
				transformed.put(key, transformAll(key, value, transformers));
			}
		}
		return transformed;
	}
	
	private static Object transformAll(String key, Object value, List<? extends ValueTransformer> transformers) {
		Object result = value;
		for (ValueTransformer transformer : transformers) {
			result = transformer.transform(key, result);
		}
		return result;
	}
	
	@Override
	public void close() throws IOException {
		reader.close();
	}

}
