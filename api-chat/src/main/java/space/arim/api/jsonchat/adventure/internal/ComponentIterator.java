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
import net.kyori.adventure.text.format.Style;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class ComponentIterator extends IteratorBase<Component> {

    private Component parent;
    private final List<Component> components;
    private int position;
    private ComponentIterator childIterator;

    private ComponentIterator(List<Component> components) {
        this.components = components;
    }

    public ComponentIterator(Component...components) {
        this(Arrays.asList(components));
    }

    public ComponentIterator(Component component) {
        this(List.of(component));
    }

    @Override
    protected Component getNext() {
        if (childIterator != null) {
            if (childIterator.hasNext()) {
                return childIterator.next();
            }
            childIterator = null;
        }
        if (position == components.size()) {
            return null;
        }
        Component current = components.get(position++);
        if (parent != null) {
            current = inheritParentInfo(current);
        }
        List<Component> children = current.children();
        if (!children.isEmpty()) {
            childIterator = new ComponentIterator(children);
            childIterator.parent = current;
        }
        return current;
    }

    private Component inheritParentInfo(Component subject) {
        Style newStyle = subject.style().merge(parent.style(),
                Style.Merge.Strategy.IF_ABSENT_ON_TARGET,
                Set.of(Style.Merge.values()));
        return subject.style(newStyle);
    }
}
