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

package space.arim.api.env.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageOnlyAudienceTest {

    private static final UnsupportedOperationException UOE = new UnsupportedOperationException();

    private final Audience audience = new MessageOnlyAudience() {
        @Override
        public void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
            throw UOE;
        }

        @Override
        public UnsupportedOperationException notSupportedException() {
            throw UOE;
        }
    };

    @TestFactory
    public Stream<DynamicNode> throwNotSupportedException() {
        return Arrays.stream(audience.getClass().getMethods()).filter((method) -> {
            return Audience.class.isAssignableFrom(method.getDeclaringClass());
        }).map((method) -> {
            return DynamicTest.dynamicTest("Calling method " + method, () -> runThrowNotSupportedException(method));
        });
    }

    private void runThrowNotSupportedException(Method method) throws IllegalAccessException {
        Object[] arguments = createDummyArguments(method);
        try {
            method.invoke(audience, arguments);
            fail("UnsupportedOperationException not thrown for " + method);
        } catch (InvocationTargetException ex) {
            assertEquals(UOE, ex.getCause(), "Different UnsupportedOperationException thrown for " + method);
        }
    }

    private static Object[] createDummyArguments(Method method) {
        Object[] arguments = new Object[method.getParameterCount()];
        int n = 0;
        for (Class<?> paramType : method.getParameterTypes()) {
            arguments[n++] = dummyValue(paramType);
        }
        return arguments;
    }

    private static Object dummyValue(Class<?> type) {
        if (type.equals(Identity.class)) {
            return Identity.nil();
        }
        if (type.equals(Identified.class)) {
            return new Identified() {
                @Override
                public @NonNull Identity identity() {
                    return Identity.nil();
                }
            };
        }
        if (type.equals(Component.class) || type.equals(ComponentLike.class)) {
            return Component.empty();
        }
        if (type.equals(MessageType.class)) {
            return MessageType.CHAT;
        }
        if (type.equals(Title.class)) {
            return Title.title(Component.empty(), Component.empty());
        }
        if (type.equals(BossBar.class)) {
            return BossBar.bossBar(Component.empty(), 0f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
        }
        if (type.equals(Sound.class)) {
            return Sound.sound(Key.key("a", "b"), Sound.Source.AMBIENT, 0f, 0f);
        }
        if (type.equals(double.class)) {
            return 0D;
        }
        if (type.equals(SoundStop.class)) {
            return SoundStop.all();
        }
        if (type.equals(Book.class)) {
            return Book.builder().build();
        }
        if (type.equals(Book.Builder.class)) {
            return Book.builder();
        }
        throw Assertions.<RuntimeException>fail("Test must be updated for type " + type);
    }
}
