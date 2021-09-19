package dev.anthonybruno.jnews.util;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesPropertyAccessor implements PropertyAccessor {
    private final Properties properties;

    public PropertiesPropertyAccessor(Properties properties) {
        this.properties = properties;
    }

    public static PropertiesPropertyAccessor from(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PropertiesPropertyAccessor(properties);
    }

    @Override
    public @Nullable String get(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
