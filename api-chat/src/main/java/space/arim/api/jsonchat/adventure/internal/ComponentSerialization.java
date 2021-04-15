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

import net.kyori.adventure.text.format.TextColor;
import space.arim.api.chat.PredefinedColour;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class ComponentSerialization {

    private ComponentSerialization() {}

    private static void hexToString(int color, Appendable builder) throws IOException {
        int hex = color & 0xFFFFFF;
        int firstDigit, thirdDigit, fifthDigit;
        if ((firstDigit = hex >> 20) == ((hex >> 16) & 0xF)
                && (thirdDigit = (hex >> 12) & 0xF) == ((hex >> 8) & 0xF)
                && (fifthDigit = (hex >> 4) & 0xF) == (hex & 0xF)) {
            builder.append(Integer.toHexString(firstDigit));
            builder.append(Integer.toHexString(thirdDigit));
            builder.append(Integer.toHexString(fifthDigit));
            return;
        }
        // Ensure 6 digits are appended
        String hexString = Integer.toHexString(hex);
        int addDigits = 6 - hexString.length();
        for (int n = 0; n < addDigits; n++) {
            builder.append('0');
        }
        builder.append(hexString);
    }

    public static void appendColor(int color, Appendable builder) throws IOException {
        PredefinedColour predefinedColor = PredefinedColour.getExactTo(color);
        if (predefinedColor == null) {
            builder.append("<#");
            hexToString(color, builder);
            builder.append('>');
        } else {
            char code = predefinedColor.getCodeChar();
            builder.append('&').append(code);
        }
    }

    public static void appendColor(TextColor color, Appendable builder) throws IOException {
        appendColor(color.value(), builder);
    }

    public static void appendColor(int color, StringBuilder builder) {
        try {
            appendColor(color, (Appendable) builder);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
