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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import space.arim.api.jsonchat.adventure.internal.ComponentIterator;
import space.arim.api.jsonchat.adventure.internal.IteratorBase;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

final class ComponentTextIterator extends IteratorBase<String> {

    private final Iterator<Component> delegate;
    private final Set<TextGoal> goals;

    /** Used to traverse hover events */
    private Iterator<String> childIterator;
    private Component current;
    private int positionOnCurrent;

    private static final int CONTENT = 0;
    private static final int HOVER_VALUE = 1;
    private static final int CLICK_VALUE = 2;
    private static final int INSERTION_VALUE = 3;

    ComponentTextIterator(Iterator<Component> delegate, Set<TextGoal> goals) {
        this.delegate = Objects.requireNonNull(delegate);
        this.goals = Set.copyOf(goals);
    }

    @Override
    protected String getNext() {
        if (childIterator != null) {
            if (childIterator.hasNext()) {
                return childIterator.next();
            }
            childIterator = null;
        }
        if (current != null) {
            int position = positionOnCurrent++;
            switch (position) {
            case CONTENT:
                if (!goals.contains(TextGoal.SIMPLE_TEXT)) {
                    return getNext();
                }
                if (!(current instanceof TextComponent)) {
                    return getNext();
                }
                return ((TextComponent) current).content();

            case HOVER_VALUE:
                if (!goals.contains(TextGoal.HOVER_TEXT)) {
                    return getNext();
                }
                HoverEvent<?> hoverEvent = current.hoverEvent();
                Object hoverValue;
                if (hoverEvent == null || !((hoverValue = hoverEvent.value()) instanceof Component)) {
                    return getNext();
                }
                // The getNext() call will traverse this iterator
                childIterator = new ComponentTextIterator(
                        new ComponentIterator((Component) hoverValue), TextGoal.simpleTextOnly());
                return getNext();

            case CLICK_VALUE:
                if (!goals.contains(TextGoal.CLICK_VALUE)) {
                    return getNext();
                }
                ClickEvent clickEvent = current.clickEvent();
                if (clickEvent == null) {
                    return getNext();
                }
                return clickEvent.value();

            case INSERTION_VALUE:
                // The current component has been exhausted. Nullify and reset
                Component current = this.current;
                this.current = null;
                positionOnCurrent = 0;

                if (!goals.contains(TextGoal.INSERTION_VALUE)) {
                    return getNext();
                }
                String insertion = current.insertion();
                if (insertion == null) {
                    return getNext();
                }
                return insertion;

            default:
                throw new IllegalStateException("Unexpected position " + position);
            }
        }
        if (!delegate.hasNext()) {
            return null;
        }
        current = delegate.next();
        Objects.requireNonNull(current, "Delegate iterator returned null");
        return getNext();
    }

}
