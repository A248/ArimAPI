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

public final class ColorCodes {

    private ColorCodes() {}

    static final int BLACK = 0x000000; // &0
    static final int DARK_BLUE = 0x0000AA; // &1
    static final int DARK_GREEN = 0x00AA00; // &2
    static final int DARK_AQUA = 0x00AAAA; // &3
    static final int DARK_RED = 0xAA0000; // &4
    static final int DARK_PURPLE = 0xAA00AA; // &5
    static final int GOLD = 0xFFAA00; // &6
    static final int GRAY = 0xAAAAAA; // &7
    static final int DARK_GRAY = 0x555555; // &8
    static final int BLUE = 0x5555FF; // &9
    static final int GREEN = 0x55FF55; // &a
    static final int AQUA = 0x55FFFF; // &b
    static final int RED = 0xFF5555; // &c
    static final int LIGHT_PURPLE = 0xFF55FF; // &d
    static final int YELLOW = 0xFFFF55; // &e
    static final int WHITE = 0xFFFFFF; // &f

    /**
     * Gets the color from a formatting code character
     *
     * @param codeChar the formatting code character, case insensitive
     * @return the color
     * @throws IllegalArgumentException if no such formatting code exists
     */
    public static int obtainColorFromCode(char codeChar) {
        switch (codeChar) {
        case '0':
            return BLACK;
        case '1':
            return DARK_BLUE;
        case '2':
            return DARK_GREEN;
        case '3':
            return DARK_AQUA;
        case '4':
            return DARK_RED;
        case '5':
            return DARK_PURPLE;
        case '6':
            return GOLD;
        case '7':
            return GRAY;
        case '8':
            return DARK_GRAY;
        case '9':
            return BLUE;
        case 'A':
        case 'a':
            return GREEN;
        case 'B':
        case 'b':
            return AQUA;
        case 'C':
        case 'c':
            return RED;
        case 'D':
        case 'd':
            return LIGHT_PURPLE;
        case 'E':
        case 'e':
            return YELLOW;
        case 'F':
        case 'f':
            return WHITE;
        default:
            throw new IllegalArgumentException("Formatting code " + codeChar + " does not exist");
        }
    }

}
