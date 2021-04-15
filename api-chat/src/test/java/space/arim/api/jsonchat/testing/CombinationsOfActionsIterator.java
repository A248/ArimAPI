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

package space.arim.api.jsonchat.testing;

import space.arim.api.jsonchat.adventure.internal.IteratorBase;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Iterator implementation of {@link CombinationsOfActions} to support lazy computation
 *
 * @param <A> the action type
 */
class CombinationsOfActionsIterator<A> extends IteratorBase<A[]> {

    private final A[] availableActions;
    private final int exponent;

    /** Actions array with the size of {@code exponent} */
    private final A[] actionsWithExponentSize;

    /** The index based on the exponent, i.e. the depth of recursion */
    private int depthIndex;
    /** A map of depth indexes to state at each level */
    private final Map<Integer, DepthState> depthStates = new HashMap<>();

    /**
     * Creates from actions and an exponent. See {@link CombinationsOfActions}
     *
     * @param actions the available action elements
     * @param exponent the exponent
     */
    CombinationsOfActionsIterator(A[] actions, int exponent) {
        availableActions = actions;
        this.exponent = exponent;
        depthIndex = exponent - 1;
        @SuppressWarnings("unchecked")
        A[] actionsWithExponentSize = (A[]) Array.newInstance(actions.getClass().getComponentType(), exponent);
        this.actionsWithExponentSize = actionsWithExponentSize;
        // Pre-fill indexes map
        for (int n = 0; n < exponent; n++) {
            DepthState depthState = new DepthState();
            depthState.actionsSoFar = actionsWithExponentSize.clone();
            depthStates.put(n, depthState);
        }
    }

    private class DepthState {
        int indexAtThisDepth;
        A[] actionsSoFar;
    }

    @Override
    protected A[] getNext() {
        if (depthIndex == exponent) {
            // The end of the iterator was already reached and we have already
            // signalled this to IteratorBase. However, IteratorBase can
            // still call getNext() even if getNext() has formerly returned null
            return null;
        }
        DepthState depthState = depthStates.get(depthIndex);
        if (depthState.indexAtThisDepth == availableActions.length) {
            depthState.indexAtThisDepth = 0;
            depthIndex++;
            if (depthIndex == exponent) {
                return null;
            }
            return getNext();
        } else {
            A[] actionsContinued = depthState.actionsSoFar.clone();
            actionsContinued[depthIndex] = availableActions[depthState.indexAtThisDepth++];
            if (depthIndex == 0) {
                return actionsContinued;
            }
            depthStates.get(depthIndex - 1).actionsSoFar = actionsContinued;
            depthIndex--;
            return getNext();
        }
    }
}
