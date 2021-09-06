package dev.anthonybruno.jnews.util;

import org.jetbrains.annotations.Nullable;

public class SystemPropertyAccessor implements PropertyAccessor {

    @Override
    public @Nullable String get(String propertyName) {
        var normalisedPropertyName = normalisePropertyName(propertyName);
        return System.getProperty(normalisedPropertyName);
    }

    @Override
    public String getNonNull(String propertyName) {
        var normalisedPropertyName = normalisePropertyName(propertyName);
        var value = System.getProperty(normalisedPropertyName);
        if (value == null) {
            throw new RuntimeException("System property '" + normalisedPropertyName + "' missing!");
        }
        return value;
    }

    private static String normalisePropertyName(String propertyName) {
        return propertyName.toUpperCase().replaceAll("\\.", "_");
    }
}
