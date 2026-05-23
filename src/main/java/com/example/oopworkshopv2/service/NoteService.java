package com.example.oopworkshopv2.service;

import com.example.oopworkshopv2.dao.NoteDao;
import com.example.oopworkshopv2.model.Note;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class NoteService {
    private final NoteDao noteDao;

    public NoteService(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public List<Note> getAllNotes() throws SQLException {
        return noteDao.findAll();
    }

    public Note createNote(String title, String content) throws SQLException {
        return noteDao.insert(new Note(title, content));
    }

    public void updateNote(Note note, String title, String content) throws SQLException {
        note.setTitle(title);
        note.setContent(content);
        noteDao.update(note);
    }

    public void deleteNote(UUID id) throws SQLException {
        noteDao.delete(id);
    }
}
