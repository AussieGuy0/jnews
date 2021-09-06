package dev.anthonybruno.jnews.util;

import org.jetbrains.annotations.Nullable;

public class SystemPropertyAccessor implements PropertyAccessor {
    @Override
    public @Nullable String get(String propertyName) {
        var normalisedPropertyName = normalisePropertyName(propertyName);
        return System.getProperty(normalisedPropertyName);
    }

    private static String normalisePropertyName(String propertyName) {
        return propertyName.toUpperCase().replaceAll("\\.", "_");
    }
}
