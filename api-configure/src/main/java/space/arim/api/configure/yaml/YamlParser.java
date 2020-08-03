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
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ValueTransformer;

class YamlParser implements AutoCloseable {

	// Constants
	
	private final Reader reader;
	private final List<? extends ValueTransformer> transformers;
	
	private final Map<String, Object> values = new LinkedHashMap<>();
	private final Map<String, List<ConfigComment>> comments = new HashMap<>();
	
	YamlParser(Path source, List<? extends ValueTransformer> transformers) throws IOException {
		reader = Files.newBufferedReader(source, StandardCharsets.UTF_8);
		this.transformers = transformers;
	}
	
	/*
	 * 
	 * Parsing
	 * 
	 */
	
	private int lineNumber; // global line increment
	
	/*
	 * Current state
	 * 
	 */
	private State state = State.WHITESPACE;
	
	private int whitespace; // blank spaces on a line preceding any content
	
	private StringBuilder commentBuilder; // used when state = COMMENT
	private List<ConfigComment> currentComments;
	
	private StringBuilder keyBuilder;  // used when state = KEY
	private String currentKey;  // usable after key has been completed
	
	private final List<String> keyChain = new ArrayList<>(); // depth of keys, with deeper keys later
	
	private StringBuilder valueBuilder; // used when state = VALUE, VALUE_SINGLEQUOTE, or VALUE_DOUBLEQUOTE
	
	private enum State {
		
		WHITESPACE(Expect.WHITESPACE, Expect.LETTERS, Expect.COMMENT_SYMBOL),
		COMMENT(Expect.WHITESPACE, Expect.LETTERS, Expect.COMMENT_SYMBOL),
		KEY(false, Expect.LETTERS, Expect.COLON),
		COLON(Expect.WHITESPACE, Expect.LETTERS, Expect.SINGLE_QUOTE, Expect.DOUBLE_QUOTE),
		SINGULAR_SPACE(Expect.LETTERS, Expect.SINGLE_QUOTE, Expect.DOUBLE_QUOTE),
		VALUE(Expect.LETTERS, Expect.WHITESPACE),
		VALUE_SINGLEQUOTE(Expect.LETTERS, Expect.WHITESPACE, Expect.SINGLE_QUOTE),
		VALUE_DOUBLEQUOTE(Expect.LETTERS, Expect.WHITESPACE, Expect.DOUBLE_QUOTE);
		
		final Set<Expect> expects;
		
		private State(boolean acceptsLineBreak, Expect...expects) {
			if (acceptsLineBreak) {
				Expect[] allExpects = Arrays.copyOf(expects, expects.length + 1);
				allExpects[expects.length] = Expect.LINE_BREAK;
				this.expects = Set.of(allExpects);
			} else {
				this.expects = Set.of(expects);
			}
		}
		
		private State(Expect...expects) {
			this(true, expects);
		}
		
		int multiState(Expect category) {
			return ordinal() << 4 | category.ordinal();
		}
	}
	
	/*public static void main(String[] args) {
		for (State s : State.values()) {
			for (Expect e : s.expects) {
				System.out.println(s + " / " + e);
				System.out.println(Integer.toBinaryString(s.multiState(e)));
				System.out.println();
			}
			System.out.println();
		}
	}*/
	
	private enum Expect {
		LETTERS,
		COLON,
		WHITESPACE,
		LINE_BREAK,
		SINGLE_QUOTE,
		DOUBLE_QUOTE,
		COMMENT_SYMBOL
	}
	
	private static Expect getActualExpect(char ch) {
		switch (ch) {
		case ':':
			return Expect.COLON;
		case ' ':
			return Expect.WHITESPACE;
		case '\n':
		case '\r':
			return Expect.LINE_BREAK;
		case '\'':
			return Expect.SINGLE_QUOTE;
		case '\"':
			return Expect.DOUBLE_QUOTE;
		case '#':
			return Expect.COMMENT_SYMBOL;
		default:
			return Expect.LETTERS;
		}
	}
	
	private StringBuilder flattenKeyChain() {
		return YamlDumper.flattenKeyChain(keyChain.toArray(String[]::new));
	}
	
	private Object transform(String key, Object value) {
		Object result = value;
		for (ValueTransformer transformer : transformers) {
			result = transformer.transform(key, result);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void addToMap(String key, Object value) throws YamlSyntaxException {
		String fullKey = flattenKeyChain().append('.').append(key).toString();
		Map<String, Object> currentMap = values;
		for (String keyOnChain : keyChain) {
			currentMap = (Map<String, Object>) currentMap.computeIfAbsent(keyOnChain, (k) -> new LinkedHashMap<>());
		}
		value = transform(fullKey, value);
		Object previous = currentMap.putIfAbsent(key, value);
		if (previous != null) {
			throw syntaxError("Duplicate key " + fullKey);
		}
	}
	
	private static Object parseObject(String value) {
		switch (value.toLowerCase(Locale.ENGLISH)) {
		case "true":
			return Boolean.TRUE;
		case "false":
			return Boolean.FALSE;
		default:
			break;
		}
		try { return Integer.parseInt(value); } catch (NumberFormatException ignored) {}
		try { return Double.parseDouble(value); } catch (NumberFormatException ignored) {}
		try { return Long.parseLong(value); } catch (NumberFormatException ignored) {}
		return value;
	}
	
	private YamlSyntaxException syntaxError(Set<Expect> expects, Expect actual) {
		return new YamlSyntaxException("Expected any of " + expects + " but got " + actual + ". Line number: " + lineNumber);
	}
	
	private YamlSyntaxException syntaxError(String expected, String actual) {
		return new YamlSyntaxException("Expected " + expected + " but got " + actual + ". Line number: " + lineNumber);
	}
	
	private YamlSyntaxException syntaxError(String message) {
		return new YamlSyntaxException(message + ". Line number: " + lineNumber);
	}
	
	private void trimKeyChain(int whitespace) {
		int maxSize = whitespace / 2;
		int sz;
		while ((sz = keyChain.size()) > maxSize) {
			keyChain.remove(sz - 1);
		}
	}
	
	private void processChar(char ch) throws YamlSyntaxException, IOException {
		Expect expectation = getActualExpect(ch);
		if (expectation == Expect.LINE_BREAK) {
			lineNumber++;
		}
		if (!state.expects.contains(expectation)) {
			throw syntaxError(state.expects, expectation);
		}
		int multiState = state.multiState(expectation);
		switch (multiState) {

		// WHITESPACE
		case 0b10:
			// WHITESPACE / WHITESPACE
			whitespace++;
			break;
		case 0b0:
			// WHITESPACE / LETTERS
			if ((whitespace & 1) != 0) {
				throw syntaxError("Got an odd amount (" + whitespace + ") of whitespace");
			}
			int maxWhitespace = keyChain.size() * 2;
			if (whitespace > maxWhitespace) {
				throw syntaxError(maxWhitespace + " spaces or less", whitespace + " spaces");
			}
			trimKeyChain(whitespace);
			keyBuilder = new StringBuilder();
			keyBuilder.append(ch);
			state = State.KEY;
			break;
		case 0b11:
			// WHITESPACE / LINE_BREAK
			break;
		case 0b110:
			// WHITESPACE / COMMENT_SYMBOL
			commentBuilder = new StringBuilder();
			state = State.COMMENT;
			break;

		// COMMENT
		case 0b10011:
			// COMMENT / LINE_BREAK
			if (currentComments == null) {
				currentComments = new ArrayList<>();
			}
			currentComments.add(new ConfigComment(whitespace, commentBuilder.toString()));
			state = State.WHITESPACE;
			break;
		case 0b10110:
			// COMMENT / COMMENT_SYMBOL - fallthrough
		case 0b10010:
			// COMMENT / WHITESPACE - fallthrough
		case 0b10000:
			// COMMENT / LETTERS
			commentBuilder.append(ch);
			break;

		// KEY
		case 0b100001:
			// KEY / COLON
			currentKey = keyBuilder.toString();
			if (currentComments != null) {
				comments.put(flattenKeyChain().append('.').append(currentKey).toString(), currentComments);
				currentComments = null;
			}
			state = State.COLON;
			break;
		case 0b100000:
			// KEY / LETTERS
			keyBuilder.append(ch);
			break;

		// COLON
		case 0b110000:
			// COLON / LETTERS
			valueBuilder = new StringBuilder();
			valueBuilder.append(ch);
			state = State.VALUE;
			break;
		case 0b110010:
			// COLON / WHITESPACE
			state = State.SINGULAR_SPACE;
			break;
		case 0b110100:
			// COLON / SINGLE_QUOTE
			valueBuilder = new StringBuilder();
			state = State.VALUE_SINGLEQUOTE;
			break;
		case 0b110101:
			// COLON / DOUBLE_QUOTE
			valueBuilder = new StringBuilder();
			state = State.VALUE_DOUBLEQUOTE;
			break;
		case 0b110011:
			// COLON / LINE_BREAK
			keyChain.add(currentKey);
			state = State.WHITESPACE;
			break;

		// SINGULAR_SPACE
		// same as COLON except that it does not allow an extra whitespace
		case 0b1000000:
			// SINGULAR_SPACE / LETTERS
			valueBuilder = new StringBuilder();
			valueBuilder.append(ch);
			state = State.VALUE;
			break;
		case 0b1000100:
			// SINGULAR_SPACE / SINGLE_QUOTE
			valueBuilder = new StringBuilder();
			state = State.VALUE_SINGLEQUOTE;
			break;
		case 0b1000101:
			// SINGULAR_SPACE / DOUBLE_QUOTE
			valueBuilder = new StringBuilder();
			state = State.VALUE_DOUBLEQUOTE;
			break;
		case 0b1000011:
			// SINGULAR_SPACE / LINE_BREAK
			keyChain.add(currentKey);
			state = State.WHITESPACE;
			break;

		// VALUE
		case 0b1010000:
			// VALUE / LETTERS
			valueBuilder.append(ch);
			break;
		case 0b1010010:
			// VALUE / WHITESPACE
			eatWhitespaceUntilLineBreak();
			// fallthrough
		case 0b1010011:
			// VALUE / LINE_BREAK
			addToMap(currentKey, parseObject(valueBuilder.toString()));
			state = State.WHITESPACE;
			break;

		// VALUE_SINGLEQUOTE
		case 0b1100010:
			// VALUE_SINGLEQUOTE / WHITESPACE - fallthrough
		case 0b1100000:
			// VALUE_SINGLEQUOTE / LETTERS
			valueBuilder.append(ch);
			break;
		case 0b1100100:
			// VALUE_SINGLEQUOTE / SINGLE_QUOTE
			addToMap(currentKey, valueBuilder.toString());
			eatWhitespaceUntilLineBreak();
			state = State.WHITESPACE;
			break;
		case 0b1100011:
			// VALUE_SINGLEQUOTE / LINE_BREAK
			throw syntaxError("close of single quotes", "end of line");

		// VALUE_DOUBLEQUOTE
		case 0b1110010:
			// VALUE_DOUBLEQUOTE / WHITESPACE - fallthrough
		case 0b1110000:
			// VALUE_DOUBLEQUOTE / LETTERS
			valueBuilder.append(ch);
			break;
		case 0b1110101:
			// VALUE_DOUBLEQUOTE / DOUBLE_QUOTE
			addToMap(currentKey, valueBuilder.toString());
			eatWhitespaceUntilLineBreak();
			state = State.WHITESPACE;
			break;
		case 0b1110011:
			// VALUE_DOUBLEQUOTE / LINE_BREAK
			throw syntaxError("close of double quotes", "end of line");
		default:
			throw new IllegalStateException("Unknown multiState " + multiState);
		}
		if (expectation == Expect.LINE_BREAK) {
			whitespace = 0;
		}
	}
	
	private void eatWhitespaceUntilLineBreak() throws IOException, YamlSyntaxException {
		int ch;
		while ((ch = reader.read()) != -1) {
			switch ((char) ch) {
			case ' ':
				continue;
			case '\n':
			case '\r':
				whitespace = 0;
				return;
			default:
				throw syntaxError("whitespace or end of line", "character " + (char) ch);
			}
		}
		// stream ended, perfectly fine
	}
	
	private void finish() throws YamlSyntaxException {
		switch (state) {
		case COLON:
		case KEY:
		case SINGULAR_SPACE:
		case VALUE:
		case VALUE_DOUBLEQUOTE:
		case VALUE_SINGLEQUOTE:
			throw syntaxError("Unexpected end of file");

		case COMMENT:
			if (currentComments == null) {
				currentComments = new ArrayList<>();
			}
			currentComments.add(new ConfigComment(whitespace, commentBuilder.toString()));
			comments.put("", currentComments);
			break;

		case WHITESPACE:
			// perfectly fine
			break;

		default:
			throw new IllegalStateException("Unknown state " + state);
		}
	}
	
	private void doParse() throws IOException, YamlSyntaxException {
		int ch;
		while ((ch = reader.read()) != -1) {
			processChar((char) ch);
		}
		finish();
	}
	
	/*
	 * 
	 * Cleanup
	 * 
	 */
	
	ConfigData parse() throws IOException, YamlSyntaxException {
		doParse();
		// return new SimpleConfigData(values, comments);
		return null;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
	
}
