module space.arim.api.env {
    requires transitive net.kyori.adventure;
    requires static org.checkerframework.checker.qual;
    requires static space.arim.managedwaits;
    requires transitive space.arim.omnibus;
    exports space.arim.api.env;
    exports space.arim.api.env.annote;
    exports space.arim.api.env.concurrent;
}