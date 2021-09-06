package dev.anthonybruno.jnews.util;

import org.jetbrains.annotations.Nullable;

public interface PropertyAccessor {

    @Nullable
    String get(String propertyName);

    default String getOrElse(String propertyName, String fallback) {
        var value = get(propertyName);
        if (value != null) {
            return value;
        }
        return fallback;
    }

    default String getNonNull(String propertyName) {
        var value = get(propertyName);
        if (value == null) {
            throw new RuntimeException("Property '" + propertyName + "' missing!");
        }
        return value;
    }
}
