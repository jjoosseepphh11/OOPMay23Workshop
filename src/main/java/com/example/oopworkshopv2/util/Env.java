package com.example.oopworkshopv2.util;

import io.github.cdimascio.dotenv.Dotenv;

public final class Env {
    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private Env() {
    }

    public static String getRequired(String key) {
        String value = DOTENV.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }
        return value;
    }

    public static String getOptional(String key, String defaultValue) {
        String value = DOTENV.get(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
