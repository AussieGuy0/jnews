package dev.anthonybruno.jnews.util;

import org.jetbrains.annotations.Nullable;

public class EnvVarPropertyAccessor implements PropertyAccessor {

    @Override
    public @Nullable String get(String propertyName) {
        var normalisedPropertyName = normalisePropertyName(propertyName);
        return System.getenv(normalisedPropertyName);
    }

    @Override
    public String getNonNull(String propertyName) {
        var normalisedPropertyName = normalisePropertyName(propertyName);
        var value = System.getenv(normalisedPropertyName);
        if (value == null) {
            throw new RuntimeException("System property '" + normalisedPropertyName + "' missing!");
        }
        return value;
    }

    private static String normalisePropertyName(String propertyName) {
        return propertyName.toUpperCase().replaceAll("\\.", "_");
    }
}
