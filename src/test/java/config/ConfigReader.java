package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            String configPath = System.getProperty("user.dir") + "/src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(configPath);
            properties.load(fis);
            fis.close();
            System.out.println("✓ Config loaded successfully from: " + configPath);
        } catch (IOException e) {
            System.out.println("✗ Error loading config.properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get Naukri Username from config.properties or environment variables
     * IMPORTANT: NO DEFAULT VALUE for sensitive data!
     * If not found, throws RuntimeException to force configuration
     */
    public String getUsername() {
        String username = properties.getProperty("naukri.username");

        // Support environment variable substitution (e.g., ${NAUKRI_USERNAME})
        if (username != null && username.startsWith("${") && username.endsWith("}")) {
            String envVar = username.substring(2, username.length() - 1);
            username = System.getenv(envVar);
        }

        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException(
                    "CONFIGURATION ERROR: 'naukri.username' is NOT configured in config.properties or environment variables\n" +
                            "Please add: naukri.username=your_email@example.com or set NAUKRI_USERNAME environment variable"
            );
        }
        return username;
    }

    /**
     * Get Naukri Password from config.properties or environment variables
     * IMPORTANT: NO DEFAULT VALUE for sensitive data!
     * If not found, throws RuntimeException to force configuration
     */
    public String getPassword() {
        String password = properties.getProperty("naukri.password");

        // Support environment variable substitution (e.g., ${NAUKRI_PASSWORD})
        if (password != null && password.startsWith("${") && password.endsWith("}")) {
            String envVar = password.substring(2, password.length() - 1);
            password = System.getenv(envVar);
        }

        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException(
                    "CONFIGURATION ERROR: 'naukri.password' is NOT configured in config.properties or environment variables\n" +
                            "Please add: naukri.password=your_actual_password or set NAUKRI_PASSWORD environment variable"
            );
        }
        return password;
    }

    /**
     * Get Naukri URL from config.properties
     * Non-sensitive value - CAN have a default fallback
     */
    public String getNaukriURL() {
        return properties.getProperty("naukri.url", "https://www.naukri.com");
    }

    /**
     * Get browser headless mode from config.properties
     * Non-sensitive value - CAN have a default fallback
     */
    public boolean isHeadlessBrowser() {
        String headless = System.getenv("BROWSER_HEADLESS");
        return headless != null && headless.equalsIgnoreCase("true");
    }

    /**
     * Get browser timeout from config.properties
     * Non-sensitive value - CAN have a default fallback
     */
    public int getBrowserTimeout() {
        return Integer.parseInt(properties.getProperty("browser.timeout", "30000"));
    }
}

