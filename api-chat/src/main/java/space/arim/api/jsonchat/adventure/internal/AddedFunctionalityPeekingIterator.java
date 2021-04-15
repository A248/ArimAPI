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
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class AddedFunctionalityPeekingIterator<T> implements PeekingIterator<T> {

    private final Iterator<T> delegate;
    /** Can be peeked value or {@code NULL_BOX} */
    private T peekedBox;
    private static final Object NULL_BOX = new Object();

    public AddedFunctionalityPeekingIterator(Iterator<T> delegate) {
        this.delegate = delegate;
    }

    @SafeVarargs
    AddedFunctionalityPeekingIterator(T...elements) {
        this(Arrays.asList(elements).iterator());
    }

    private static <T> T unboxNull(T boxedValue) {
        return (boxedValue == NULL_BOX) ? null : boxedValue;
    }

    @SuppressWarnings("unchecked")
    private static <T> T boxNull(T value) {
        return (value == null) ? (T) NULL_BOX : value;
    }

    @Override
    public boolean hasNext() {
        return peekedBox != null || delegate.hasNext();
    }

    @Override
    public T next() {
        if (peekedBox != null) {
            T peekedValue = unboxNull(peekedBox);
            peekedBox = null;
            return peekedValue;
        }
        return delegate.next();
    }

    @Override
    public T peek() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (peekedBox != null) {
            return unboxNull(peekedBox);
        }
        T value = delegate.next();
        peekedBox = boxNull(value);
        return value;
    }

}
