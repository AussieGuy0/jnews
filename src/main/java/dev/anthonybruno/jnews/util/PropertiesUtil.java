package dev.anthonybruno.jnews.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties readProperties(InputStream propertiesFile) {
        Properties properties = new Properties();
        try {
            properties.load(propertiesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
