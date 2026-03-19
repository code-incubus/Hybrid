package api.utils;

import api.exceptions.ConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties;

    static {
        try {
            FileInputStream file = new FileInputStream(
                    "src/test/resources/config.properties"
            );
            properties = new Properties();
            properties.load(file);
        } catch (IOException e) {
            throw new ConfigurationException("Cannot read config.properties!", e);
        }
    }

    /**
     * For NON-SENSITIVE values that always exist in config.properties
     * Throws exception if key is missing
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException(
                    "Missing config key: '" + key + "' in config.properties"
            );
        }
        return value;
    }

    /**
     * For SENSITIVE values that must be overridden via ENV or -D
     * <p>
     * Priority:
     * 1. System property  → -DKEY=value (Maven/JVM)
     * 2. ENV variable     → OS/Docker/GitHub Actions
     * 3. config.properties → fallback (should be CHANGE_ME)
     * <p>
     * Throws exception if value is missing or still CHANGE_ME
     */
    public static String getOrEnv(String key, String envName) {

        // 1. Maven -D parameter — highest priority
        String sysProp = System.getProperty(envName);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }

        // 2. OS / Docker / GitHub Actions ENV variable
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        // 3. Fallback — config.properties
        String propValue = properties.getProperty(key);
        if (propValue == null || propValue.equals("CHANGE_ME")) {
            throw new ConfigurationException(key, envName);
        }

        return propValue;
    }
}