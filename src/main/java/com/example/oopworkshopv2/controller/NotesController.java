package com.example.oopworkshopv2.controller;

import com.example.oopworkshopv2.dao.NoteDao;
import com.example.oopworkshopv2.model.Note;
import com.example.oopworkshopv2.service.NoteService;
import com.example.oopworkshopv2.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotesController {

    @FXML
    private TableView<NoteRow> notesTable;

    @FXML
    private TableColumn<NoteRow, String> titleColumn;

    @FXML
    private TableColumn<NoteRow, String> createdAtColumn;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label detailsTitleLabel;

    @FXML
    private TextArea detailsContentArea;

    private final ObservableList<NoteRow> noteRows = FXCollections.observableArrayList();
    private final NoteService noteService = new NoteService(new NoteDao());

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        notesTable.setItems(noteRows);

        notesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            boolean hasSelection = newValue != null;
            editButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
            showDetails(newValue);
        });

        refresh();
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    @FXML
    private void onAdd() {
        openEditor(null);
    }

    @FXML
    private void onEdit() {
        NoteRow selected = notesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        openEditor(selected.note());
    }

    @FXML
    private void onDelete() {
        NoteRow selected = notesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        boolean ok = Alerts.confirm("Delete Note", "Delete \"" + selected.getTitle() + "\"?");
        if (!ok) {
            return;
        }

        try {
            noteService.deleteNote(selected.note().getId());
            refresh();
        } catch (SQLException e) {
            Alerts.error("Database Error", "Failed to delete note.", e);
        }
    }

    private void refresh() {
        try {
            List<Note> notes = noteService.getAllNotes();
            noteRows.setAll(notes.stream().map(NoteRow::from).toList());
            if (!noteRows.isEmpty() && notesTable.getSelectionModel().getSelectedItem() == null) {
                notesTable.getSelectionModel().selectFirst();
            } else {
                showDetails(notesTable.getSelectionModel().getSelectedItem());
            }
        } catch (SQLException e) {
            Alerts.error("Database Error", "Failed to load notes.\nCheck your .env and Supabase connection.", e);
        }
    }

    private void openEditor(Note noteToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oopworkshopv2/note-editor.fxml"));
            Scene scene = new Scene(loader.load(), 520, 360);

            NoteEditorController controller = loader.getController();
            controller.setNote(noteToEdit);
            controller.setOnSaved(this::refresh);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(noteToEdit == null ? "New Note" : "Edit Note");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            Alerts.error("UI Error", "Failed to open editor window.", e);
        }
    }

    private void showDetails(NoteRow row) {
        if (detailsTitleLabel == null || detailsContentArea == null) {
            return;
        }
        if (row == null) {
            detailsTitleLabel.setText("Select a note to see details");
            detailsContentArea.setText("");
            return;
        }

        Note note = row.note();
        detailsTitleLabel.setText(note.getTitle());
        detailsContentArea.setText(note.getContent() == null ? "" : note.getContent());
    }

    public static final class NoteRow {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());

        private final Note note;
        private final String title;
        private final String createdAt;

        private NoteRow(Note note, String title, String createdAt) {
            this.note = note;
            this.title = title;
            this.createdAt = createdAt;
        }

        public static NoteRow from(Note note) {
            String created = formatCreatedAt(note.getCreatedAt());
            return new NoteRow(note, note.getTitle(), created);
        }

        private static String formatCreatedAt(Instant createdAt) {
            if (createdAt == null) {
                return "";
            }
            return FORMATTER.format(createdAt);
        }

        public Note note() {
            return note;
        }

        public String getTitle() {
            return title;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
