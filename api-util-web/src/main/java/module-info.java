/**
 * Web utility. The only exposed package is {@link space.arim.api.util.web}
 */
module space.arim.api.web {
    exports space.arim.api.util.web;

    requires transitive java.net.http;
    requires space.arim.omnibus;
    requires com.google.gson;
}