package com.example.oopworkshopv2.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class Alerts {
    private Alerts() {
    }

    public static void error(String title, String message, Throwable error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(buildDetails(error));
        alert.showAndWait();
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static String buildDetails(Throwable error) {
        if (error == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Throwable current = error;
        int depth = 0;
        while (current != null && depth < 4) {
            if (depth > 0) {
                sb.append("\nCaused by: ");
            }
            sb.append(current.getClass().getSimpleName());
            String msg = current.getMessage();
            if (msg != null && !msg.isBlank()) {
                sb.append(": ").append(msg);
            }
            current = current.getCause();
            depth++;
        }
        return sb.toString();
    }
}
