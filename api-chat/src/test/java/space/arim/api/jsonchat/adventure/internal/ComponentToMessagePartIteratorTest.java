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

package space.arim.api.jsonchat.adventure.internal;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import space.arim.api.jsonchat.ChatMessagePart;
import space.arim.api.jsonchat.ClickEventInfo;
import space.arim.api.jsonchat.adventure.FormattingCodeSerializer;
import space.arim.api.jsonchat.adventure.FormattingSerializer;
import space.arim.api.jsonchat.testing.CombinationsOfActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static space.arim.api.jsonchat.testing.TestUtil.collect;
import static space.arim.api.jsonchat.testing.TestUtil.mapList;
import static space.arim.api.jsonchat.testing.AdventureUtil.toComponentList;

public class ComponentToMessagePartIteratorTest {

    private final FormattingSerializer formattingSerializer = new FormattingCodeSerializer();

    @Test
    public void singleAlikeSegment() {
        String text = "&aSome &c&lbold &atext";
        List<Component> formatting = toComponentList(formattingSerializer.readFormatting(text));
        PeekingIterator<Component> peekingIter = new ListIteratorPeekingIterator<>(formatting.listIterator());
        assertEquals(
                List.of(new ChatMessagePart.Builder().build(text)),
                collect(new ComponentToMessagePartIterator(peekingIter, formattingSerializer)));
    }

    private static final Integer[] ALL_REPEAT_COUNTS = new Integer[] {1, 2, 3};

    @TestFactory
    public Stream<DynamicNode> allPossibleCombinations() {
        // It is important that the text end in a different color than it starts with
        // so that when it is repeated and re-serialized, it will not differ
        String text = "&aSome &c&lbold &btext";
        return new CombinationsOfActions<>(ALL_REPEAT_COUNTS).getAllCombinations(Segment.values().length)
                .map((repeat_counts) -> {
                    return DynamicTest.dynamicTest("For repeat counts " + Arrays.toString(repeat_counts),
                            () -> { runTest(text, repeat_counts); });
                });
    }

    private void runTest(String text, Integer[] repeat_counts) {
        List<ChatMessagePart> allMessageParts = new ArrayList<>();
        List<Component> allComponents = new ArrayList<>();
        int segmentIndex = 0;
        for (int repeat_count : repeat_counts) {
            Segment segment = Segment.values()[segmentIndex];

            allMessageParts.add(segment.generateChatMessagePart(text.repeat(repeat_count)));
            List<Component> components = segment.generateComponents(text, formattingSerializer);
            for (int n = 0; n < repeat_count; n++) {
                allComponents.addAll(components);
            }
            segmentIndex++;
        }
        PeekingIterator<Component> peekingIter = new ListIteratorPeekingIterator<>(allComponents.listIterator());
        assertEquals(
                allMessageParts,
                collect(new ComponentToMessagePartIterator(peekingIter, formattingSerializer)));
    }

    private enum Segment {
        NONE(false, false, false),
        WITH_HOVER(true, false, false),
        WITH_CLICK(false, true, false),
        WITH_INSERT(false, false, true),
        WITH_HOVER_AND_CLICK(true, true, false),
        WITH_HOVER_AND_INSERT(true, false, true),
        WITH_CLICK_AND_INSERT(false, true, true),
        WITH_HOVER_AND_CLICK_AND_INSERT(true, true, true);

        private final boolean hover;
        private final boolean click;
        private final boolean insert;

        private static final String HOVER_MSG = "hover";
        private static final String CLICK_VALUE = "/spawn";
        private static final String INSERT_VALUE = "surprise";

        Segment(boolean hover, boolean click, boolean insert) {
            this.hover = hover;
            this.click = click;
            this.insert = insert;
        }

        ChatMessagePart generateChatMessagePart(String text) {
            var messagePartBuilder = new ChatMessagePart.Builder();
            if (hover) {
                messagePartBuilder.associatedHoverEvent(HOVER_MSG);
            }
            if (click) {
                messagePartBuilder.associatedClickEvent(
                        new ClickEventInfo(ClickEventInfo.ClickType.RUN_COMMAND, CLICK_VALUE));
            }
            if (insert) {
                messagePartBuilder.associatedInsertion(INSERT_VALUE);
            }
            return messagePartBuilder.build(text);
        }

        List<Component> generateComponents(String text, FormattingSerializer formattingSerializer) {
            return mapList(formattingSerializer.readFormatting(text), (componentLike) -> {
                Component component = componentLike.asComponent();
                if (hover) {
                    component = component.hoverEvent(HoverEvent.showText(Component.text(HOVER_MSG)));
                }
                if (click) {
                    component = component.clickEvent(ClickEvent.runCommand(CLICK_VALUE));
                }
                if (insert) {
                    component = component.insertion(INSERT_VALUE);
                }
                return component;
            });
        }
    }

}
