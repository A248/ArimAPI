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

package space.arim.api.jsonchat.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import space.arim.api.jsonchat.ClickEventInfo;
import space.arim.api.jsonchat.ParsingVisitor;
import space.arim.api.jsonchat.testing.CombinationsOfActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static space.arim.api.jsonchat.testing.AdventureUtil.text;
import static space.arim.api.jsonchat.testing.AdventureUtil.textBuilder;
import static space.arim.api.jsonchat.testing.TestUtil.randomString;

public class ComponentVisitorTest {

    private static ComponentVisitor createVisitor() {
        return new ComponentVisitor(new PlainTextFormattingSerializer());
    }

    @Test
    public void simpleSegment() {
        ComponentVisitor visitor = createVisitor();
        visitor.visitPlainText("Some text");
        assertEquals(text("Some text"), visitor.buildResult());
    }

    @TestFactory
    public Stream<DynamicNode> oneSegmentWithPossiblyMultipleEvents() {
        return new CombinationsOfActions<>(new Boolean[] {true, false}).getAllCombinations(3)
                .map((combination) -> {
                    boolean hover = combination[0];
                    boolean click = combination[1];
                    boolean insert = combination[2];
                    return DynamicTest.dynamicTest(
                            "With events hover: " + hover + ", click: " + click + ", insert " + insert,
                            () -> runTestOneSegment(hover, click, insert));
                });
    }

    private static void runTestOneSegment(boolean hover, boolean click, boolean insert) {
        ComponentVisitor visitor = createVisitor();
        assertEquals(
                generateExpectedComponent(hover, click, insert, visitor),
                visitor.buildResult());
    }

    private static TextComponent generateExpectedComponent(boolean hover, boolean click, boolean insert,
                                                           ParsingVisitor visitor) {

        String mainMessage = randomString();
        visitor.visitPlainText(mainMessage);
        String hoverEvent = null;
        if (hover) {
            hoverEvent = randomString();
            visitor.visitHoverEvent(hoverEvent);
        }
        ClickEventInfo clickEvent = null;
        if (click) {
            var clickValues = ClickEventInfo.ClickType.values();
            clickEvent = new ClickEventInfo(
                    clickValues[ThreadLocalRandom.current().nextInt(clickValues.length)],
                    randomString());
            visitor.visitClickEvent(clickEvent.clickType(), clickEvent.value().toString());
        }
        String insertion = null;
        if (insert) {
            insertion = randomString();
            visitor.visitInsertion(insertion);
        }

        TextComponent.Builder textBuilder = textBuilder().content(mainMessage);
        if (hoverEvent != null) {
            textBuilder.hoverEvent(HoverEvent.showText(text(hoverEvent)));
        }
        if (clickEvent != null) {
            textBuilder.clickEvent(ClickEvent.clickEvent(
                    ComponentVisitor.clickTypeToAction(clickEvent.clickType()), clickEvent.value().toString()));
        }
        if (insertion != null) {
            textBuilder.insertion(insertion);
        }
        return textBuilder.build();
    }

    @TestFactory
    public Stream<DynamicNode> multipleSegmentsMultipleEvents() {
        return Stream.of(
                testMultipleSegmentsMultipleEvents(2),
                testMultipleSegmentsMultipleEvents(3),
                testMultipleSegmentsMultipleEvents(4))
                .flatMap(Function.identity());
    }

    private Stream<DynamicNode> testMultipleSegmentsMultipleEvents(int segmentCount) {
        Boolean[][] availableEventCombinations = new CombinationsOfActions<>(new Boolean[] {true, false})
                .getAllCombinations(3)
                .toArray(Boolean[][]::new);
        return new CombinationsOfActions<>(availableEventCombinations).getAllCombinations(segmentCount)
                .map((eventCombinationsCombination) -> {
                    return DynamicTest.dynamicTest(
                            "With event combinations combination: " + Arrays.deepToString(eventCombinationsCombination),
                            () -> runTestMultipleSegments(eventCombinationsCombination));
                });
    }

    private static void runTestMultipleSegments(Boolean[][] eventToggles) {
        ComponentVisitor visitor = createVisitor();
        List<Component> expectedComponents = new ArrayList<>();
        for (Boolean[] combination : eventToggles) {
            boolean hover = combination[0];
            boolean click = combination[1];
            boolean insert = combination[2];
            expectedComponents.add(
                    generateExpectedComponent(hover, click, insert, visitor));
        }
        assertEquals(
                Component.empty().children(expectedComponents),
                visitor.buildResult());
    }


}
