module space.arim.api.util.testing {
    requires io.github.classgraph;
    requires jakarta.inject;
    requires org.junit.jupiter.api;
    requires org.mockito;
    exports space.arim.api.util.testing;
    exports space.arim.api.util.testing.mockito;
}