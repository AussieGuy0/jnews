package dev.anthonybruno.jnews.util;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesPropertyAccessorTest {

    @Test
    void getsPropertyThatExists() {
        var properties = new Properties();
        properties.setProperty("test", "123");
        var accessor = new PropertiesPropertyAccessor(properties);
        assertThat(accessor.get("test")).isEqualTo("123");
    }

    @Test
    void getPropertyThatDoesNotExist() {
        var properties = new Properties();
        var accessor = new PropertiesPropertyAccessor(properties);
        assertThat(accessor.get("test")).isNull();
    }

    @Test
    void getOrElseFallsBackIfNull() {
        var properties = new Properties();
        var accessor = new PropertiesPropertyAccessor(properties);
        assertThat(accessor.getOrElse("test", "fallback")).isEqualTo("fallback");
    }

    @Test
    void getOrElseDoesNotFallBackIfNonNull() {
        var properties = new Properties();
        properties.setProperty("test", "123");
        var accessor = new PropertiesPropertyAccessor(properties);
        assertThat(accessor.getOrElse("test", "fallback")).isEqualTo("123");
    }
}
