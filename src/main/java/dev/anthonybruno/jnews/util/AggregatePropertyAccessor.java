package dev.anthonybruno.jnews.util;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class AggregatePropertyAccessor implements PropertyAccessor {
    private final List<PropertyAccessor> propertyAccessors;

    public AggregatePropertyAccessor(List<PropertyAccessor> propertyAccessors) {
        this.propertyAccessors = propertyAccessors;
    }

    @Override
    public @Nullable String get(String propertyName) {
        return propertyAccessors.stream()
                .map(propertyAccessor -> propertyAccessor.get(propertyName))
                .filter(Predicate.not(Objects::isNull))
                .findFirst().orElse(null);
    }
}
