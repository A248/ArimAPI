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

import java.util.Objects;
import java.util.Optional;

/**
 * Information of a chat message part as it is about to be reconstituted.
 *
 */
public final class ChatMessagePart {

    private final CharSequence plainText;
    private final CharSequence associatedHoverEvent;
    private final ClickEventInfo associatedClickEvent;
    private final CharSequence associatedInsertion;

    ChatMessagePart(CharSequence plainText, CharSequence associatedHoverEvent,
                    ClickEventInfo associatedClickEvent, CharSequence associatedInsertion) {
        this.plainText = Objects.requireNonNull(plainText, "plainText");
        this.associatedHoverEvent = associatedHoverEvent;
        this.associatedClickEvent = associatedClickEvent;
        this.associatedInsertion = associatedInsertion;
    }

    /**
     * The main plain text
     *
     * @return the plain text
     */
    public CharSequence plainText() {
        return plainText;
    }

    /**
     * Any associated hover event
     *
     * @return the hover event if present
     */
    public Optional<CharSequence> associatedHoverEvent() {
        return Optional.ofNullable(associatedHoverEvent);
    }

    /**
     * Gets any associated click event
     *
     * @return the click event if present
     */
    public Optional<ClickEventInfo> associatedClickEvent() {
        return Optional.ofNullable(associatedClickEvent);
    }

    /**
     * Gets the insertion
     *
     * @return the insertion if present
     */
    public Optional<CharSequence> associatedInsertion() {
        return Optional.ofNullable(associatedInsertion);
    }

    @Override
    public String toString() {
        return "ChatMessagePart{" +
                "plainText='" + plainText + '\'' +
                ", associatedHoverEvent='" + associatedHoverEvent + '\'' +
                ", associatedClickEvent=" + associatedClickEvent +
                ", associatedInsertion='" + associatedInsertion + '\'' +
                '}';
    }

    private static boolean equalSequences(CharSequence seq1, CharSequence seq2) {
        return (seq1 == null) ? seq2 == null : seq2 != null && seq2.toString().equals(seq1.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessagePart that = (ChatMessagePart) o;
        return plainText.toString().equals(that.plainText.toString())
                && equalSequences(associatedHoverEvent, that.associatedHoverEvent)
                && Objects.equals(associatedClickEvent, that.associatedClickEvent)
                && equalSequences(associatedInsertion, that.associatedInsertion);
    }

    @Override
    public int hashCode() {
        int result = plainText.toString().hashCode();
        result = 31 * result + (associatedHoverEvent != null ? associatedHoverEvent.toString().hashCode() : 0);
        result = 31 * result + (associatedClickEvent != null ? associatedClickEvent.hashCode() : 0);
        result = 31 * result + (associatedInsertion != null ? associatedInsertion.toString().hashCode() : 0);
        return result;
    }

    /**
     * Creates a builder with the same properties as this message part. <br>
     * <br>
     * It is guaranteed that using {@link Builder#build(CharSequence)} with
     * the same plain text as on this message part will yield the same message
     * part according to {@code equals}
     *
     * @return a builder with the same properties as this message part
     */
    public Builder toBuilder() {
        return new Builder()
                .associatedHoverEvent(associatedHoverEvent)
                .associatedClickEvent(associatedClickEvent)
                .associatedInsertion(associatedInsertion);
    }

    /**
     * Builder of chat message data
     *
     */
    public static final class Builder {

        private CharSequence associatedHoverEvent;
        private ClickEventInfo associatedClickEvent;
        private CharSequence associatedInsertion;

        /**
         * Creates the builder
         */
        public Builder() {}

        /**
         * Sets the hover event, or null for none
         *
         * @param associatedHoverEvent the hover value or {@code null}
         * @return this builder
         */
        public Builder associatedHoverEvent(CharSequence associatedHoverEvent) {
            this.associatedHoverEvent = associatedHoverEvent;
            return this;
        }

        /**
         * Sets the click event, or null for none
         *
         * @param associatedClickEvent the click event or {@code null}
         * @return this builder
         */
        public Builder associatedClickEvent(ClickEventInfo associatedClickEvent) {
            this.associatedClickEvent = associatedClickEvent;
            return this;
        }

        /**
         * Sets the insertion, or null for none
         *
         * @param associatedInsertion the insertion or {@code null}
         * @return this builder
         */
        public Builder associatedInsertion(CharSequence associatedInsertion) {
            this.associatedInsertion = associatedInsertion;
            return this;
        }

        /**
         * Builds into chat message part. May be used repeatedly without side effects
         *
         * @param plainText the plain text
         * @return the chat message part
         */
        public ChatMessagePart build(CharSequence plainText) {
            return new ChatMessagePart(
                    plainText,
                    associatedHoverEvent,
                    associatedClickEvent,
                    associatedInsertion);
        }

        @Override
        public String toString() {
            return "ChatMessagePart.Builder{" +
                    "associatedHoverEvent='" + associatedHoverEvent + '\'' +
                    ", associatedClickEvent=" + associatedClickEvent +
                    ", associatedInsertion='" + associatedInsertion + '\'' +
                    '}';
        }
    }
}
