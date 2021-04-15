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

import java.util.Arrays;
import java.util.ListIterator;

public final class ListIteratorPeekingIterator<T> implements PeekingIterator<T> {

    private final ListIterator<T> delegate;

    public ListIteratorPeekingIterator(ListIterator<T> delegate) {
        this.delegate = delegate;
    }

    @SafeVarargs
    ListIteratorPeekingIterator(T...elements) {
        this(Arrays.asList(elements).listIterator());
    }

    @Override
    public T peek() {
        T peeked = delegate.next();
        delegate.previous();
        return peeked;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public T next() {
        return delegate.next();
    }
}
