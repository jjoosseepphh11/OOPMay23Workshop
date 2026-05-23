package com.example.oopworkshopv2.controller;

import com.example.oopworkshopv2.dao.NoteDao;
import com.example.oopworkshopv2.model.Note;
import com.example.oopworkshopv2.service.NoteService;
import com.example.oopworkshopv2.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NoteEditorController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private Button saveButton;

    private final NoteService noteService = new NoteService(new NoteDao());

    private Note note;
    private Runnable onSaved = () -> {};

    public void setNote(Note note) {
        this.note = note;
        if (note != null) {
            titleField.setText(note.getTitle());
            contentArea.setText(note.getContent());
        }
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = (onSaved == null) ? () -> {} : onSaved;
    }

    @FXML
    private void onSave() {
        String title = titleField.getText() == null ? "" : titleField.getText().trim();
        String content = contentArea.getText() == null ? "" : contentArea.getText().trim();

        if (title.isBlank()) {
            Alerts.error("Validation", "Title is required.", null);
            return;
        }

        try {
            if (note == null) {
                noteService.createNote(title, content);
            } else {
                noteService.updateNote(note, title, content);
            }
            onSaved.run();
            close();
        } catch (SQLException e) {
            Alerts.error("Database Error", "Failed to save note.", e);
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
