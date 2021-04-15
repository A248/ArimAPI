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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.Style.style;

public final class TestUtil {

    private TestUtil() {}

    public static void sleepUnchecked(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new AssertionError(ex);
        }
    }

    public static <T> List<T> collect(Iterator<T> iter) {
        List<T> result = new ArrayList<>();
        iter.forEachRemaining(result::add);
        return result;
    }

    public static <T, R> List<R> mapList(List<T> source, Function<T, R> mapper) {
        return source.stream().map(mapper).toList();
    }

    public static String randomString() {
        var tlr = ThreadLocalRandom.current();
        byte[] bytes = new byte[tlr.nextInt(8, 20)];
        tlr.nextBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static TextColor randomColor() {
        var tlr = ThreadLocalRandom.current();
        return TextColor.color(tlr.nextInt(0, 0xffffff));
    }

    /**
     * Generates a text color which is NOT a NamedTextColor
     *
     * @return a rgb color
     */
    public static TextColor randomRgbColor() {
        TextColor color;
        do {
            color = randomColor();
        } while (color instanceof NamedTextColor || NamedTextColor.ofExact(color.value()) != null);
        return color;
    }

}
