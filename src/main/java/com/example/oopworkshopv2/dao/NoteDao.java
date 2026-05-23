package com.example.oopworkshopv2.dao;

import com.example.oopworkshopv2.db.Db;
import com.example.oopworkshopv2.model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NoteDao {

    public List<Note> findAll() throws SQLException {
        String sql = "select id, title, content, created_at from notes order by created_at desc";
        try (Connection connection = Db.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<Note> notes = new ArrayList<>();
            while (resultSet.next()) {
                notes.add(mapRow(resultSet));
            }
            return notes;
        }
    }

    public Optional<Note> findById(UUID id) throws SQLException {
        String sql = "select id, title, content, created_at from notes where id = ?";
        try (Connection connection = Db.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(resultSet));
            }
        }
    }

    public Note insert(Note note) throws SQLException {
        String sql = "insert into notes (title, content) values (?, ?) returning id, created_at";
        try (Connection connection = Db.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, note.getTitle());
            statement.setString(2, note.getContent());

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                note.setId((UUID) resultSet.getObject("id"));
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                if (createdAt != null) {
                    note.setCreatedAt(createdAt.toInstant());
                }
                return note;
            }
        }
    }

    public void update(Note note) throws SQLException {
        String sql = "update notes set title = ?, content = ? where id = ?";
        try (Connection connection = Db.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, note.getTitle());
            statement.setString(2, note.getContent());
            statement.setObject(3, note.getId());

            statement.executeUpdate();
        }
    }

    public void delete(UUID id) throws SQLException {
        String sql = "delete from notes where id = ?";
        try (Connection connection = Db.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);
            statement.executeUpdate();
        }
    }

    private static Note mapRow(ResultSet resultSet) throws SQLException {
        UUID id = (UUID) resultSet.getObject("id");
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        Instant createdAtInstant = (createdAt == null) ? null : createdAt.toInstant();
        return new Note(id, title, content, createdAtInstant);
    }
}
