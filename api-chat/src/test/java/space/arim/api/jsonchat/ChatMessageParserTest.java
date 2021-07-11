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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static space.arim.api.jsonchat.ClickEventInfo.clickTypeFromTag;
import static space.arim.api.jsonchat.testing.TestUtil.randomString;

@ExtendWith(MockitoExtension.class)
public class ChatMessageParserTest {

    private final ParsingVisitor visitor;

    public ChatMessageParserTest(@Mock ParsingVisitor visitor) {
        this.visitor = visitor;
    }

    private void parse(String text) {
        new ChatMessageParser(visitor, text).parse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "h", "hi", " \n \n   "})
    public void simpleText(String text) {
        parse(text);
        verify(visitor).visitPlainText(text);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void simpleRandomText() {
        String text = randomString().replace("||", "");
        parse(text);
        verify(visitor).visitPlainText(text);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void multiplePlainSegments() {
        parse("Some text||Some more text");
        verify(visitor).visitPlainText("Some text");
        verify(visitor).visitPlainText("Some more text");
        verifyNoMoreInteractions(visitor);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cmd", "sgt", "url"})
    public void hoverEventAndClickEvent(String clickTag) {
        parse("Some text||ttp:Hover on me||" + clickTag + ":/spawn");
        verify(visitor).visitPlainText("Some text");
        verify(visitor).visitHoverEvent("Hover on me");
        verify(visitor).visitClickEvent(clickTypeFromTag(clickTag), "/spawn");
        verifyNoMoreInteractions(visitor);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cmd", "sgt", "url"})
    public void hoverEventAndClickEventAndFurtherSegment(String clickTag) {
        parse("Some text||ttp:Hover on me||" + clickTag + ":/spawn||continues here");
        verify(visitor).visitPlainText("Some text");
        verify(visitor).visitHoverEvent("Hover on me");
        verify(visitor).visitClickEvent(clickTypeFromTag(clickTag), "/spawn");
        verify(visitor).visitPlainText("continues here");
        verifyNoMoreInteractions(visitor);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cmd", "sgt", "url"})
    public void startWithHoverEventAndClickEventThenFurtherSegment(String clickTag) {
        parse("ttp:Hover on me||" + clickTag + ":/spawn||continues here");
        verify(visitor).visitHoverEvent("Hover on me");
        verify(visitor).visitClickEvent(clickTypeFromTag(clickTag), "/spawn");
        verify(visitor).visitPlainText("continues here");
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void insertionAndHoverEvent() {
        parse("Some text||ttp:Hover on me to insert||ins:insert me||continues here");
        verify(visitor).visitPlainText("Some text");
        verify(visitor).visitHoverEvent("Hover on me to insert");
        verify(visitor).visitInsertion("insert me");
        verify(visitor).visitPlainText("continues here");
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void escapeDoublePipes() {
        parse("Some text with |||| pipes||ttp:Hover on me to insert||ins:insert me");
        verify(visitor).visitPlainText("Some text with || pipes");
        verify(visitor).visitHoverEvent("Hover on me to insert");
        verify(visitor).visitInsertion("insert me");
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void escapeMultipleDoublePipes() {
        parse("Some text with |||||||| pipes||ttp:Hover on me to insert||ins:insert me");
        verify(visitor).visitPlainText("Some text with |||| pipes");
        verify(visitor).visitHoverEvent("Hover on me to insert");
        verify(visitor).visitInsertion("insert me");
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void escapeDoublePipesAdjacentToEnd() {
        parse("Some text with pipes: ||||||ttp:Hover on me to insert||ins:insert me");
        verify(visitor).visitPlainText("Some text with pipes: ||");
        verify(visitor).visitHoverEvent("Hover on me to insert");
        verify(visitor).visitInsertion("insert me");
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void escapeMultipleDoublePipesAdjacentToEnd() {
        parse("Some text with pipes: ||||||||||ttp:Hover on me to insert||ins:insert me");
        verify(visitor).visitPlainText("Some text with pipes: ||||");
        verify(visitor).visitHoverEvent("Hover on me to insert");
        verify(visitor).visitInsertion("insert me");
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void escapeStartTagUsingNil() {
        parse("Some text||nil:ttp:Hover on me to insert||nil:ins:insert me");
        verify(visitor).visitPlainText("Some text");
        verify(visitor).visitPlainText("ttp:Hover on me to insert");
        verify(visitor).visitPlainText("ins:insert me");
        verifyNoMoreInteractions(visitor);
    }

    @Override
    public String toString() {
        return "ChatMessageParserTest " + hashCode();
    }
}
