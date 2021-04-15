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

/**
 * A visitor which is provided with the parts of a chat message as they are parsed. <br>
 * <br>
 * Implementations are usually mutable as they collect the message parts into some
 * data structure.
 *
 */
public interface ParsingVisitor {

    /**
     * Visits a hover event
     *
     * @param hoverValue the hover event value, which may be parsed any which way
     *                   as decided by the implementation
     */
    void visitHoverEvent(String hoverValue);

    /**
     * Visits a click event
     *
     * @param clickType the click type
     * @param value the click event value
     */
    void visitClickEvent(ClickEventInfo.ClickType clickType, String value);

    /**
     * Visits an insertion
     *
     * @param value the insertion value
     */
    void visitInsertion(String value);

    /**
     * Visits plain text
     *
     * @param text the simple text
     */
    void visitPlainText(String text);

}
