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

import java.util.Iterator;

/**
 * Chat message writer which performs the inverse operation of
 * {@link ChatMessageParser}. <br>
 * <br>
 * Mutable and non-reusable. Not thread safe.
 *
 */
public final class ChatMessageReconstitutor {

    private final StringBuilder segmentBuilder = new StringBuilder();

    /**
     * Creates
     */
    public ChatMessageReconstitutor() {}

    /**
     * Writes part of the message
     *
     * @param messagePart the message part
     */
    public void writeMessagePart(ChatMessagePart messagePart) {
        if (segmentBuilder.length() != 0) {
            segmentBuilder.append("||");
        }
        CharSequence plainText = messagePart.plainText().toString();
        if (JsonTag.getTag(plainText) != JsonTag.NONE || startsWithDoublePipes(plainText)) {
            // Escape literals
            segmentBuilder.append("nil:");
        }
        appendEscapingDoublePipes(plainText);

        messagePart.associatedHoverEvent().ifPresent((hoverEvent) -> {
            segmentBuilder.append("||ttp:");
            appendEscapingDoublePipes(hoverEvent);
        });

        messagePart.associatedClickEvent().ifPresent((clickEvent) -> {
            segmentBuilder.append("||");
            switch (clickEvent.clickType()) {
            case OPEN_URL:
                segmentBuilder.append("url");
                break;
            case RUN_COMMAND:
                segmentBuilder.append("cmd");
                break;
            case SUGGEST_COMMAND:
                segmentBuilder.append("sgt");
                break;
            default:
                throw new UnsupportedOperationException("Not implemented for " + clickEvent.clickType());
            }
            segmentBuilder.append(':');
            appendEscapingDoublePipes(clickEvent.value());
        });

        messagePart.associatedInsertion().ifPresent((insertion) -> {
            segmentBuilder.append("||ins:");
            appendEscapingDoublePipes(insertion);
        });
    }

    private static boolean startsWithDoublePipes(CharSequence text) {
        return text.length() >= 2 && text.charAt(0) == '|' && text.charAt(1) == '|';
    }

    private void appendEscapingDoublePipes(CharSequence text) {
        // Replace while appending. Should be equivalent to
        // var escaped = text.toString().replace("||", "||||");
        // segmentBuilder.append(escaped);
        boolean foundOnePipe = false;
        for (int n = 0; n < text.length(); n++) {
            char character = text.charAt(n);
            if (character == '|') {
                if (foundOnePipe) {
                    segmentBuilder.append("|||");
                    foundOnePipe = false;
                    continue;
                } else {
                    foundOnePipe = true;
                }
            } else {
                foundOnePipe = false;
            }
            segmentBuilder.append(character);
        }
    }

    /**
     * Convenience method to write every message part in the iterator
     *
     * @param messageParts all of the message parts to write
     */
    public void writeAllMessageParts(Iterator<ChatMessagePart> messageParts) {
        while (messageParts.hasNext()) {
            writeMessagePart(messageParts.next());
        }
    }

    /**
     * Forms the message as a string
     *
     * @return the created message
     * @throws IllegalStateException optional, if the reconstitutor detects it is being re-used
     */
    public String reconstitute() {
        return segmentBuilder.toString();
    }
}
