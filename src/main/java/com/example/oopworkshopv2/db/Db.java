package com.example.oopworkshopv2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.example.oopworkshopv2.util.Env;

public final class Db {
    private Db() {
    }

    public static Connection connect() throws SQLException {
        String host = sanitizeHost(Env.getRequired("SUPABASE_DB_HOST"));
        String port = Env.getOptional("SUPABASE_DB_PORT", "5432");
        String database = Env.getRequired("SUPABASE_DB_NAME");
        String user = Env.getRequired("SUPABASE_DB_USER");
        String password = Env.getRequired("SUPABASE_DB_PASSWORD");
        String sslMode = Env.getOptional("SUPABASE_DB_SSLMODE",
                Env.getOptional("SUPABASE_DB_SSL_MODE", "require"));

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?sslmode=" + sslMode;

        
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        return DriverManager.getConnection(url, properties);
    }

    private static String sanitizeHost(String host) {
        String trimmed = host.trim();
        if ((trimmed.startsWith("\"") && trimmed.endsWith("\"")) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        if (trimmed.startsWith("https://")) {
            trimmed = trimmed.substring("https://".length());
        } else if (trimmed.startsWith("http://")) {
            trimmed = trimmed.substring("http://".length());
        }
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        if (trimmed.isBlank()) {
            throw new IllegalStateException("SUPABASE_DB_HOST is blank after sanitizing. Check your .env value.");
        }
        return trimmed;
    }
}
