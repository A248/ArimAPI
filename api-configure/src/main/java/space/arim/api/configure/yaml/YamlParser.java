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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import space.arim.api.configure.impl.SimpleConfigData;

class YamlParser implements AutoCloseable {

	// Static and instance constants
	
	private static final boolean DEBUG = false;
	
	private static final int READ_AHEAD_LIMIT = 1024;
	
	private final BufferedReader reader;
	private final List<? extends ValueTransformer> transformers;
	
	private final Map<String, Object> values = new LinkedHashMap<>();
	private final Map<String, List<ConfigComment>> comments = new HashMap<>();
	
	private YamlParser(BufferedReader reader, List<? extends ValueTransformer> transformers) {
		this.reader = reader;
		this.transformers = transformers;
	}
	
	YamlParser(InputStream inputStream, List<? extends ValueTransformer> transformers) {
		this(new BufferedReader(new InputStreamReader(inputStream)), transformers);
	}
	
	YamlParser(Path source, List<? extends ValueTransformer> transformers) throws IOException {
		this(Files.newBufferedReader(source, StandardCharsets.UTF_8), transformers);
	}
	
	/*
	 * 
	 * Parsing
	 * 
	 */
	
	private int lineNumber = 1; // global line increment
	
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
		
		// After whitespace, letters start a key, # starts a comment
		WHITESPACE(Expect.WHITESPACE, Expect.LETTERS, Expect.COMMENT_SYMBOL),
		
		// In a comment, everything keeps the comment going except a line break
		COMMENT(Expect.WHITESPACE, Expect.LETTERS, Expect.COMMENT_SYMBOL, Expect.COLON, Expect.SINGLE_QUOTE, Expect.DOUBLE_QUOTE),
		
		// A key grows with letters or ends with a colon
		// Whitespace is only accepted when reading multiline lists
		// KEY is the only State which does not permit a LINE_BREAK
		KEY(false, Expect.LETTERS, Expect.COLON, Expect.WHITESPACE),
		
		// After a colon starts a value. There is an optional single space
		COLON(Expect.WHITESPACE, Expect.LETTERS, Expect.SINGLE_QUOTE, Expect.DOUBLE_QUOTE),
		
		// When a colon has a single space after it, that is a singular space, which functions alike
		SINGULAR_SPACE(Expect.LETTERS, Expect.SINGLE_QUOTE, Expect.DOUBLE_QUOTE),
		
		// Values grow by more letters and end with whitespace
		VALUE(Expect.LETTERS, Expect.WHITESPACE),
		
		// Quoted values grow or close with their kind of quote
		// They accept the other kind of quote as normal text
		VALUE_SINGLEQUOTE(Expect.LETTERS, Expect.WHITESPACE, Expect.COLON, Expect.DOUBLE_QUOTE, Expect.SINGLE_QUOTE),
		VALUE_DOUBLEQUOTE(Expect.LETTERS, Expect.WHITESPACE, Expect.COLON, Expect.SINGLE_QUOTE, Expect.DOUBLE_QUOTE);
		
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
	
	private String keyChainDotPrefix(String key) {
		if (keyChain.size() == 0) {
			return key;
		}
		return YamlDumper.flattenKeyChain(keyChain.toArray(String[]::new)).append('.').append(key).toString();
	}
	
	private Object transform(String key, Object value) {
		Object result = value;
		for (ValueTransformer transformer : transformers) {
			result = transformer.transform(key, result);
			if (result == null) {
				return null;
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void addToMap(String key, Object value) throws YamlSyntaxException {
		String fullKey = keyChainDotPrefix(key);
		value = transform(fullKey, value);
		if (value == null) {
			// transformers didn't like it
			return;
		}
		Map<String, Object> currentMap = values;
		for (String keyOnChain : keyChain) {
			currentMap = (Map<String, Object>) currentMap.computeIfAbsent(keyOnChain, (k) -> new LinkedHashMap<>());
		}
		Object previous = currentMap.putIfAbsent(key, value);
		if (previous != null) {
			throw syntaxError("Duplicate key " + fullKey);
		}
	}
	
	private Object parseUnquotedObject(String value) throws YamlSyntaxException {
		checkNoQuotes(value, '\'');
		checkNoQuotes(value, '"');
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
	
	private String errorPrefix() {
		return ". Line number: " + lineNumber + ". State: " + state;
	}
	
	private YamlSyntaxException syntaxError(Set<Expect> expects, Expect actual) {
		return new YamlSyntaxException("Expected any of " + expects + " but got " + actual + errorPrefix());
	}
	
	private YamlSyntaxException syntaxError(String expected, String actual) {
		return new YamlSyntaxException("Expected " + expected + " but got " + actual + errorPrefix());
	}
	
	private YamlSyntaxException syntaxError(String message) {
		return new YamlSyntaxException(message + errorPrefix());
	}
	
	private void trimKeyChain(int whitespace) {
		int maxSize = whitespace / 2;
		int sz;
		while ((sz = keyChain.size()) > maxSize) {
			keyChain.remove(sz - 1);
		}
	}
	
	private void processChar(char ch) throws YamlSyntaxException, IOException {
		final Expect expectation = getActualExpect(ch);
		if (expectation == Expect.LINE_BREAK) {
			lineNumber++;
			reader.mark(READ_AHEAD_LIMIT);
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
		case 0b10001:
			// COMMENT / COLON - fallthrough
		case 0b10100:
			// COMMENT / SINGLE_QUOTE - fallthrough
		case 0b10101:
			// COMMENT / DOUBLE_QUOTE - fallthrough
		case 0b10000:
			// COMMENT / LETTERS
			commentBuilder.append(ch);
			break;

		// KEY
		case 0b100001:
			// KEY / COLON
			currentKey = keyBuilder.toString();
			if (currentComments != null) {
				comments.put(keyChainDotPrefix(currentKey), currentComments);
				currentComments = null;
			}
			state = State.COLON;
			break;
		case 0b100000:
			// KEY / LETTERS
			keyBuilder.append(ch);
			break;
		case 0b100010:
			// KEY / WHITESPACE
			// When this happens, it may be because there is a multiline list
			if (keyChain.isEmpty() || keyBuilder.toString().charAt(0) != '-') {
				throw syntaxError("colon", "whitespace");
			}
			if (DEBUG) System.out.println("Parsing multiline list");
			attemptParseMutilineList();
			if (DEBUG) System.out.println("Parsed multiline list");
			state = State.WHITESPACE;
			whitespace = 0;
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
			addToMap(currentKey, parseUnquotedObject(valueBuilder.toString()));
			state = State.WHITESPACE;
			break;

		// VALUE_SINGLEQUOTE
		case 0b1100001:
			// VALUE_SINGLEQUOTE / COLON - fallthrough
		case 0b1100010:
			// VALUE_SINGLEQUOTE / WHITESPACE - fallthrough
		case 0b1100101:
			// VALUE_SINGLEQUOTE / DOUBLE_QUOTE - fallthrough
		case 0b1100000:
			// VALUE_SINGLEQUOTE / LETTERS
			valueBuilder.append(ch);
			break;
		case 0b1100100:
			// VALUE_SINGLEQUOTE / SINGLE_QUOTE
			String value1 = valueBuilder.toString();
			checkNoQuotes(value1, '\'');
			addToMap(currentKey, value1);
			eatWhitespaceUntilLineBreak();
			state = State.WHITESPACE;
			break;
		case 0b1100011:
			// VALUE_SINGLEQUOTE / LINE_BREAK
			throw syntaxError("close of single quotes", "end of line");

		// VALUE_DOUBLEQUOTE
			// VALUE_DOUBLEQUOTE / COLON - fallthrough
		case 0b1110001:
		case 0b1110010:
			// VALUE_DOUBLEQUOTE / WHITESPACE - fallthrough
		case 0b1110100:
			// VALUE_DOUBLEQUOTE / SINGLE_QUOTE - fallthrough
		case 0b1110000:
			// VALUE_DOUBLEQUOTE / LETTERS
			valueBuilder.append(ch);
			break;
		case 0b1110101:
			// VALUE_DOUBLEQUOTE / DOUBLE_QUOTE
			String value2 = valueBuilder.toString();
			checkNoQuotes(value2, '"');
			addToMap(currentKey, value2);
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
		if (DEBUG) System.out.println("State " + state + " and keyChain " + keyChain);
	}
	
	private static boolean isNewLine(char ch) {
		return (ch == '\r' || ch == '\n');
	}
	
	private void attemptParseMutilineList() throws IOException, YamlSyntaxException {
		reader.reset(); // reset to beginning of line

		final int requiredWhitespace = keyChain.size() * 2;
		StringBuilder requiredPrefixBuilder = new StringBuilder();
		for (int n = 0; n < requiredWhitespace; n++) {
			requiredPrefixBuilder.append(' ');
		}
		requiredPrefixBuilder.append("- ");
		String requiredPrefix = requiredPrefixBuilder.toString();

		int index = keyChain.size() - 1;
		String currentKey = keyChain.get(index);
		keyChain.remove(index);

		StringBuilder lineBuilder = new StringBuilder();
		List<Object> result = new ArrayList<>();
		for (;;) {
			int readResult = reader.read();

			boolean endOfLine = false;
			if (readResult == -1 || (endOfLine = isNewLine((char) readResult))) {
				String line = lineBuilder.toString();
				if (DEBUG) System.out.println("Matching line '" + line + "' with '" + requiredPrefix + "'");
				if (!line.startsWith(requiredPrefix)) {
					if (!result.isEmpty()) {
						this.addToMap(currentKey, result);
					}
					reader.reset(); // reset to beginning of line
					return;
				}
				Object value = parseListValue(line.substring(requiredPrefix.length()));
				result.add(value);
				if (endOfLine) {
					lineNumber++;
					reader.mark(READ_AHEAD_LIMIT);
				} else {
					this.addToMap(currentKey, result);
					return;
				}
				lineBuilder = new StringBuilder();
				continue;
			}
			lineBuilder.append((char) readResult);
		}
	}
	
	private void checkNoQuotes(String val, char quote) throws YamlSyntaxException {
		if (val.indexOf(quote) != -1) {
			throw syntaxError("Unclosed " + ((quote == '\'') ? "single" : " double") + " quote");
		}
	}
	
	private Object parseListValue(String val) throws YamlSyntaxException {
		int length = val.length();
		if (length == 0) {
			throw syntaxError("element value within list", "line break");
		}
		char quote = val.charAt(0);
		if (quote == '\'' || quote == '"') {
			if (val.charAt(length - 1) != quote) {
				throw syntaxError("Unclosed " + ((quote == '\'') ? "single" : " double") + " quote");
			}
			val = val.substring(1, length - 1);
			checkNoQuotes(val, quote);
			return val;
		} else {
			return parseUnquotedObject(val);
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
				lineNumber++;
				whitespace = 0;
				return;
			default:
				throw syntaxError("whitespace or end of line", "character " + (char) ch);
			}
		}
		// stream ended, perfectly fine
	}
	
	private void finish() throws YamlSyntaxException, IOException {
		switch (state) {
		case COLON:
		case KEY:
		case SINGULAR_SPACE:
		case VALUE:
		case VALUE_DOUBLEQUOTE:
		case VALUE_SINGLEQUOTE:
			// Pretend there is a new blank line
			processChar('\n');
			break;

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
		return new SimpleConfigData(values, comments);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
	
}
