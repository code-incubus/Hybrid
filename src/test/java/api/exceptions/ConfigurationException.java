package api.exceptions;

/**
 * Thrown when framework configuration is missing or invalid.
 * <p>
 * Common causes:
 * - Required property not set in config.properties
 * - Environment variable not set (still CHANGE_ME)
 * - config.properties file not found
 */
public class ConfigurationException extends FrameworkException {

    // Missing value — tells user exactly how to fix it
    public ConfigurationException(String key, String envName) {
        super("Missing configuration: '" + key + "'" +
                "\nSet via one of:" +
                "\n  - Maven:  -D" + envName + "=value" +
                "\n  - ENV:    " + envName + "=value" +
                "\n  - Config: " + key + "=value");
    }

    // File not found
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}