/**
 * The json chat API. Developed primarily to support the JSON.sk format. <br>
 * <br>
 * Interoperates with the Adventure library to parse JSON.sk messages as
 * {@code Component}s. {@link space.arim.api.jsonchat.adventure} will form
 * the basis of most user code. <br>
 * <br>
 * Note that {@code space.arim.api.chat} and sub-packages are terminally deprecated.
 * They are part of an independent API which does not depend on Adventure.
 * Such API is slated for removal.
 *
 */
module space.arim.api.jsonchat {
    exports space.arim.api.jsonchat;
    exports space.arim.api.jsonchat.adventure;
    exports space.arim.api.jsonchat.adventure.util;
    requires transitive net.kyori.adventure;
    requires static transitive org.checkerframework.checker.qual;

    // Deprecated
    exports space.arim.api.chat;
    exports space.arim.api.chat.manipulator;
    requires transitive java.desktop;
    exports space.arim.api.chat.serialiser;
}