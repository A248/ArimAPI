/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
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

package space.arim.api.jsonchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static space.arim.api.jsonchat.ClickEventInfo.ClickType.OPEN_URL;
import static space.arim.api.jsonchat.ClickEventInfo.ClickType.RUN_COMMAND;
import static space.arim.api.jsonchat.ClickEventInfo.ClickType.SUGGEST_COMMAND;

/**
 * The parser itself. Mutable and not thread safe. An instance should
 * be constructed every time parsing is desired.
 *
 */
public final class ChatMessageParser {

    private final ParsingVisitor visitor;
    private final String content;

    /** Best effort re-use detection */
    private boolean used;

    private static final Pattern DOUBLE_PIPES_PATTERN = Pattern.compile("||", Pattern.LITERAL);

    /**
     * Creates an instance
     *
     * @param visitor the visitor to output events to
     * @param content the content to parse
     */
    public ChatMessageParser(ParsingVisitor visitor, String content) {
        this.visitor = Objects.requireNonNull(visitor, "visitor");
        this.content = Objects.requireNonNull(content, "content");
    }

    /**
     * Runs the parser. Collects events in the visitor
     *
     * @throws IllegalStateException optional, if the parser detects it is being re-used
     */
    public void parse() {
        if (used) {
            throw new IllegalStateException("Already used");
        }
        try {
            doParse();
        } finally {
            used = true;
        }
    }

    /*
     *
     * PARSER IMPLEMENTATION
     *
     */

    /**
     * Parse all message segments, which are separated by ||.
     * Account for escaped pipes (e.g. ||||)
     *
     * @return the parsed segments
     */
    private List<String> getSegments() {
        String[] split = DOUBLE_PIPES_PATTERN.split(content);
        if (!content.contains("||||")) {
            return Arrays.asList(split);
        }
        List<String> segments = new ArrayList<>(split.length - 1);

        /*
         * Convert all |||| to ||
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
                segments.set(lastIndex, segments.get(lastIndex) + preSegment);
            }
        }
        return segments;
    }

    private static void addDoublePipesToLastSegment(List<String> segments, int emptyCount) {
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
        String pipes = "||".repeat(doublePipeCount);

        int lastIndex = segments.size() - 1;
        if (lastIndex >= 0) {
            segments.set(lastIndex, segments.get(lastIndex) + pipes);
        } else {
            segments.add(pipes);
        }
    }

    private void doParse() {
        List<String> segments = getSegments();
        for (String segmentValue : segments) {
            if (segmentValue.isEmpty()) {
                continue;
            }
            String unprefixedValue = segmentValue.substring(4);
            JsonTag tag = JsonTag.getTag(segmentValue);
            switch (tag) {
            case NIL:
                visitor.visitPlainText(unprefixedValue);
                break;
            case NONE:
                visitor.visitPlainText(segmentValue);
                break;
            case TTP:
                visitor.visitHoverEvent(unprefixedValue);
                break;
            case CMD:
                visitor.visitClickEvent(RUN_COMMAND, unprefixedValue);
                break;
            case SGT:
                visitor.visitClickEvent(SUGGEST_COMMAND, unprefixedValue);
                break;
            case URL:
                visitor.visitClickEvent(OPEN_URL, unprefixedValue);
                break;
            case INS:
                visitor.visitInsertion(unprefixedValue);
                break;
            default:
                throw new IllegalStateException("Unknown JsonTag " + tag);
            }
        }
    }
}
