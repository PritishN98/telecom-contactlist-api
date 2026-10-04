package com.telecom.api.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility to read framework configurations from config.properties.
 */
public class ConfigReader {

    private static Properties properties;

    static {
        try {
            properties = new Properties();
            String path = System.getProperty("user.dir") + "/src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(path);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.err.println("Failed to load config.properties, using fallback defaults: " + e.getMessage());
            properties = new Properties();
            properties.setProperty("baseUrl", "https://thinking-tester-contact-list.herokuapp.com");
            properties.setProperty("defaultPassword", "myPassword");
            properties.setProperty("updatedPassword", "myNewPassword");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return properties.getProperty("baseUrl", "https://thinking-tester-contact-list.herokuapp.com");
    }

    public static String getDefaultPassword() {
        return properties.getProperty("defaultPassword", "myPassword");
    }

    public static String getUpdatedPassword() {
        return properties.getProperty("updatedPassword", "myNewPassword");
    }
}
