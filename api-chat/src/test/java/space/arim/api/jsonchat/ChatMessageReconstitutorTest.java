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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static space.arim.api.jsonchat.ClickEventInfo.clickEventFromValues;

public class ChatMessageReconstitutorTest {

    private final ChatMessageReconstitutor reconstitutor = new ChatMessageReconstitutor();

    private void assertReconstitution(String text, ChatMessagePart...messageParts) {
        reconstitutor.writeAllMessageParts(List.of(messageParts).iterator());
        assertEquals(text, reconstitutor.reconstitute());
    }

    private ChatMessagePart simplePart(String text) {
        return new ChatMessagePart.Builder().build(text);
    }

    @Test
    public void simpleColoredSegment() {
        String text = "&aSome &bcolored &ctext";
        assertReconstitution(text, simplePart(text));
    }

    @Test
    public void multiplePlainSegments() {
        assertReconstitution("Some text||Some more text",
                simplePart("Some text"), simplePart("Some more text"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"cmd", "sgt", "url"})
    public void hoverEventAndClickEvent(String clickTag) {
        assertReconstitution("Some text||ttp:Hover on me||" + clickTag + ":/spawn",
                new ChatMessagePart.Builder()
                        .associatedHoverEvent("Hover on me")
                        .associatedClickEvent(clickEventFromValues(clickTag, "/spawn"))
                        .build("Some text"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"cmd", "sgt", "url"})
    public void hoverEventAndClickEventAndFurtherSegment(String clickTag) {
        assertReconstitution("Some text||ttp:Hover on me||" + clickTag + ":/spawn||continues here",
                new ChatMessagePart.Builder()
                        .associatedHoverEvent("Hover on me")
                        .associatedClickEvent(clickEventFromValues(clickTag, "/spawn"))
                        .build("Some text"),
                simplePart("continues here"));
    }

    @Test
    public void insertionAndHoverEvent() {
        assertReconstitution("Some text||ttp:Hover on me to insert||ins:insert me||continues here",
                new ChatMessagePart.Builder()
                        .associatedHoverEvent("Hover on me to insert")
                        .associatedInsertion("insert me")
                        .build("Some text"),
                simplePart("continues here"));
    }

    @Test
    public void escapeDoublePipes() {
        assertReconstitution("Some text with |||| pipes||ttp:Hover on me to insert||ins:insert me",
                new ChatMessagePart.Builder()
                        .associatedHoverEvent("Hover on me to insert")
                        .associatedInsertion("insert me")
                        .build("Some text with || pipes"));
    }

    @Test
    public void escapeMultipleDoublePipes() {
        assertReconstitution("Some text with |||||||| pipes||ttp:Hover on me to insert||ins:insert me",
                new ChatMessagePart.Builder()
                        .associatedHoverEvent("Hover on me to insert")
                        .associatedInsertion("insert me")
                        .build("Some text with |||| pipes"));
    }

    @Test
    public void escapeDoublePipesAdjacentToEnd() {
        assertReconstitution("Some text with pipes: ||||||ttp:Hover on me to insert||ins:insert me",
                new ChatMessagePart.Builder()
                        .associatedHoverEvent("Hover on me to insert")
                        .associatedInsertion("insert me")
                        .build("Some text with pipes: ||"));
    }

    @Test
    public void escapeMultipleDoublePipesAdjacentToEnd() {
        assertReconstitution("Some text with pipes: ||||||||||ttp:Hover on me to insert||ins:insert me",
                new ChatMessagePart.Builder()
                        .associatedHoverEvent("Hover on me to insert")
                        .associatedInsertion("insert me")
                        .build("Some text with pipes: ||||"));
    }

    @Test
    public void escapeStartTagUsingNil() {
        assertReconstitution("Some text||nil:ttp:Hover on me to insert||nil:ins:insert me",
                simplePart("Some text"), simplePart("ttp:Hover on me to insert"), simplePart("ins:insert me"));
    }
}
