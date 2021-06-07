module space.arim.api.env {
    exports space.arim.api.env;
    exports space.arim.api.env.adventure;
    exports space.arim.api.env.annote;
    exports space.arim.api.env.concurrent;

    requires transitive space.arim.omnibus;
    requires transitive net.kyori.adventure;
    requires static org.checkerframework.checker.qual;
}