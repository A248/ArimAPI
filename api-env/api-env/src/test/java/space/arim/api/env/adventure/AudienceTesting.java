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
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.function.Executable;
import space.arim.omnibus.util.ArraysUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AudienceTesting {

    private final Audience audience;

    public AudienceTesting(Audience audience) {
        this.audience = Objects.requireNonNull(audience, "adventure");
    }

    public Stream<DynamicNode> verifyBasicContracts() {
        return nullChecks();
    }

    private Stream<DynamicNode> nullChecks() {
        Set<DynamicNode> tests = new HashSet<>();
        for (Identity identity : addNull(Identity.nil(), Identity.identity(UUID.randomUUID()))) {
            for (Component component : addNull(Component.text("text"))) {
                for (MessageType type : addNull(MessageType.values())) {
                    if (identity != null && component != null && type != null) {
                        continue;
                    }
                    String testName = "sendMessage null check with values " +
                            "component=" + component + ", type=" + type + ", identity=" + identity;
                    tests.add(dynamicTest(testName, () -> {
                        assertThrows(NullPointerException.class, () -> audience.sendMessage(identity, component, type));
                    }));
                }
            }
        }
        for (Component header : addNull(Component.text("text"))) {
            for (Component footer : addNull(Component.text("text"))) {
                if (header != null && footer != null) {
                    continue;
                }
                tests.add(dynamicTest("sendPlayerListHeaderAndFooter null check", () -> {
                    assertThrows(NullPointerException.class, () -> audience.sendPlayerListHeaderAndFooter(header, footer));
                }));
            }
        }
        tests.add(dynamicTest("showTitle null check", () -> {
            assertThrows(NullPointerException.class, () -> audience.showTitle(null));
        }));
        tests.add(dynamicTest("showBossBar null check", () -> {
            assertThrows(NullPointerException.class, () -> audience.showBossBar(null));
        }));
        tests.add(dynamicTest("hideBossBar null check", () -> {
            assertThrows(NullPointerException.class, () -> audience.hideBossBar(null));
        }));
        tests.add(dynamicTest("playSound null check", () -> {
            assertThrows(NullPointerException.class, () -> audience.playSound(null));
        }));
        tests.add(dynamicTest("playSound null check", () -> {
            assertThrows(NullPointerException.class, () -> audience.playSound(null, 0D, 0D, 0D));
        }));
        tests.add(dynamicTest("stopSound null check", () -> {
            assertThrows(NullPointerException.class, () -> audience.stopSound(null));
        }));
        tests.add(dynamicTest("openBook null check", () -> {
            assertThrows(NullPointerException.class, () -> audience.openBook((Book) null));
        }));
        return tests.stream();
    }

    @SafeVarargs
    private static <T> T[] addNull(T...original) {
        return combine(original, null);
    }

    private static <T> T[] combine(T[] original, T extra) {
        return ArraysUtil.expandAndInsert(original, extra, 0);
    }

    public DynamicNode verifyDoesNotSupport(Identity identity, MessageType messageType) {
        Component message = Component.text("message");
        Executable test = () -> {
            assertThrows(UnsupportedOperationException.class,() -> audience.sendMessage(identity, message, messageType));
        };
        return dynamicTest("Does not support identity " + identity + " and message type " + messageType, test);
    }

    public DynamicNode verifyDoesSupport(Identity identity, MessageType messageType) {
        Component message = Component.text("message");
        Executable test = () -> {
            assertDoesNotThrow(() -> audience.sendMessage(identity, message, messageType));
        };
        return dynamicTest("Does support identity " + identity + " and message type " + messageType, test);
    }

    public Stream<DynamicNode> verifyDoesNotSupportNonDefaultIdentityAndOrType() {
        Set<DynamicNode> tests = new HashSet<>();
        for (Identity identity : new Identity[] {Identity.identity(UUID.randomUUID()), Identity.nil()}) {
            for (MessageType type : MessageType.values()) {
                if (identity.equals(Identity.nil()) && type.equals(MessageType.SYSTEM)) {
                    continue;
                }
                tests.add(verifyDoesNotSupport(identity, type));
            }
        }
        return tests.stream();
    }

}
