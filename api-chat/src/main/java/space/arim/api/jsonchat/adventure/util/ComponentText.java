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

package space.arim.api.jsonchat.adventure.util;

import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import space.arim.api.jsonchat.adventure.internal.ComponentIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import static space.arim.api.jsonchat.adventure.util.TextGoal.CLICK_VALUE;
import static space.arim.api.jsonchat.adventure.util.TextGoal.HOVER_TEXT;
import static space.arim.api.jsonchat.adventure.util.TextGoal.INSERTION_VALUE;
import static space.arim.api.jsonchat.adventure.util.TextGoal.SIMPLE_TEXT;

/**
 * Manipulator class for transforming and analyzing text in components. <br>
 * <br>
 * All "mutative" operations return new components, because a {@code Component} is immutable. <br>
 * <br>
 * A {@code ComponentText} is itself immutable and thread-safe.
 *
 */
public final class ComponentText {

    private final Component component;
    private final Set<TextGoal> goals;

    private ComponentText(Component component, Set<TextGoal> goals) {
        this.component = Objects.requireNonNull(component, "component");
        this.goals = Set.copyOf(goals);
    }

    /**
     * Creates from the given component and text goals
     *
     * @param component the component to analyze
     * @param goals the text goals to use
     * @return the text editor
     */
    public static ComponentText create(Component component, Set<TextGoal> goals) {
        return new ComponentText(component, goals);
    }

    /**
     * Creates from the given component and text goals
     *
     * @param component the component to analyze
     * @param goals the text goals to use
     * @return the text editor
     */
    public static ComponentText create(Component component, TextGoal...goals) {
        return create(component, Set.of(goals));
    }

    /**
     * Creates from the given component. Uses all text goals
     *
     * @param component the component to analyze
     * @return the text editor
     */
    public static ComponentText create(Component component) {
        return create(component, TextGoal.allGoals());
    }

    /**
     * Sums the length of all the text in the component
     *
     * @return the sum of the lengths of all text
     */
    public int sumLength() {
        int sum = 0;
        for (Iterator<String> iter = iter(); iter.hasNext(); ) {
            String next = iter.next();
            sum += next.length();
        }
        return sum;
    }

    /**
     * Determines whether any text in this component contains the given text
     *
     * @param text the text to check for
     * @return true if any text in this component contains the specified text, false otherwise
     */
    public boolean contains(String text) {
        Objects.requireNonNull(text, "text");
        for (Iterator<String> iter = iter(); iter.hasNext(); ) {
            String next = iter.next();
            if (next.contains(text)) {
                return true;
            }
        }
        return false;
    }

    // Visible for testing
    Iterator<String> iter() {
        return new ComponentTextIterator(new ComponentIterator(component), goals);
    }

    /**
     * Replaces all occurrence of the target text in the source component with
     * the given replacement
     *
     * @param target the text to literally match
     * @param replacement the replacement text
     * @return the new component
     */
    public Component replaceText(CharSequence target, CharSequence replacement) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(replacement, "replacement");
        return replaceText((str) -> str.replace(target, replacement));
    }

    /**
     * Replaces all occurrence of the pattern in the source component with
     * the given replacement
     *
     * @param regex the pattern to match match
     * @param replacement the replacement text
     * @return the new component
     */
    public Component replaceText(Pattern regex, CharSequence replacement) {
        Objects.requireNonNull(regex, "regex");
        String replacementValue = replacement.toString();
        return replaceText((str) -> regex.matcher(str).replaceAll(replacementValue));
    }

    /**
     * Replaces all text in the source component using the given function. <br>
     * <br>
     * The function will be applied for all text in the source component and its result
     * used to determine the new text.
     *
     * @param operator the function to run all text through
     * @return the new component
     */
    public Component replaceText(Function<? super String, ? extends CharSequence> operator) {
        Objects.requireNonNull(operator, "operator");
        return mapComponent(component, operator, goals);
    }

    private static Component mapComponent(Component component,
                                          Function<? super String, ? extends CharSequence> operator, Set<TextGoal> goals) {
        boolean changed = false;
        ComponentBuilder<?, ?> builder;
        if (component instanceof TextComponent) {
            TextComponent.Builder textBuilder = ((TextComponent) component).toBuilder();
            if (goals.contains(SIMPLE_TEXT)) {
                String oldContent = textBuilder.content();
                String newContent = operator.apply(oldContent).toString();
                //noinspection StringEquality
                if (oldContent != newContent) { // Intentional reference equality
                    changed = true;
                    textBuilder.content(newContent);
                }
            }
            builder = textBuilder;
        } else if (component instanceof BuildableComponent) {
            builder = ((BuildableComponent<?, ?>) component).toBuilder();
        } else {
            builder = Component.text().style(component.style()).append(component.children());
        }
        if (goals.contains(HOVER_TEXT)) {
            HoverEvent<?> hoverEvent = component.hoverEvent();
            Object hoverValue;
            if (hoverEvent != null && (hoverValue = hoverEvent.value()) instanceof Component) {
                Component oldHover = (Component) hoverValue;
                Component newHover = mapComponent(oldHover, operator, TextGoal.simpleTextOnly());
                if (oldHover != newHover) {
                    changed = true;
                    builder.hoverEvent(HoverEvent.showText(newHover));
                }
            }
        }
        if (goals.contains(CLICK_VALUE)) {
            ClickEvent clickEvent = component.clickEvent();
            if (clickEvent != null) {
                String oldClick = clickEvent.value();
                String newClick = operator.apply(oldClick).toString();
                //noinspection StringEquality
                if (oldClick != newClick) {
                    changed = true;
                    builder.clickEvent(ClickEvent.clickEvent(clickEvent.action(), newClick));
                }
            }
        }
        if (goals.contains(INSERTION_VALUE)) {
            String oldInsert = component.insertion();
            if (oldInsert != null) {
                String newInsert = operator.apply(oldInsert).toString();
                //noinspection StringEquality
                if (oldInsert != newInsert) {
                    changed = true;
                    builder.insertion(newInsert);
                }
            }
        }

        List<Component> oldChildren = component.children();
        List<Component> newChildren = new ArrayList<>(oldChildren.size());
        {
            boolean changedChildren = false;
            for (Component oldChild : oldChildren) {
                Component newChild = mapComponent(oldChild, operator, goals);
                if (oldChild != newChild) {
                    changedChildren = true;
                }
                newChildren.add(newChild);
            }
            if (changedChildren) {
                changed = true;
            } else {
                newChildren = oldChildren;
            }
        }
        if (!changed) {
            return component;
        }
        Component built = builder.build();
        if (newChildren == oldChildren) {
            return built;
        }
        return built.children(newChildren);
    }

    @Override
    public String toString() {
        return "ComponentText{" +
                "component=" + component +
                ", goals=" + goals +
                '}';
    }
}
