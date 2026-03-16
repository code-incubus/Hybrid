package api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    // static block runs when the class load first time
    static {
        try {
            FileInputStream file = new FileInputStream(
                    "src/test/resources/config.properties"
            );
            properties = new Properties();
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read config.properties!", e);
        }
    }

    // Reads from file
    public static String get(String key) {
        return properties.getProperty(key);
    }

    // Cita ENV prvo, pa fajl ako ENV ne postoji
    public static String getOrEnv(String key, String envName) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return properties.getProperty(key);
    }
}
