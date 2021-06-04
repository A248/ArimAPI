/**
 * The json chat API. Developed primarily to support the JSON.sk format. <br>
 * <br>
 * Interoperates with the Adventure library to parse JSON.sk messages as
 * {@code Component}s. {@link space.arim.api.jsonchat.adventure} will form
 * the basis of most user code.
 */
module space.arim.api.jsonchat {
    exports space.arim.api.jsonchat;
    exports space.arim.api.jsonchat.adventure;
    exports space.arim.api.jsonchat.adventure.util;
    requires transitive net.kyori.adventure;
    requires static transitive org.checkerframework.checker.qual;
}