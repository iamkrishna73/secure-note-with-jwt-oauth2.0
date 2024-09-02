package io.iamkrishna73.notes.service;

import io.iamkrishna73.notes.entity.Note;

import java.util.List;

public interface INoteService {
    Note createNoteForOwner(String username, String content);
    Note updateNoteForUser(Long noteId, String content, String username);
    void deleteNoteForUser(Long noteId, String userName);
    List<Note> getNotesForUser(String username);

}
