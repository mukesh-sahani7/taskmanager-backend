package com.example.taskmanager.controller;

import com.example.taskmanager.model.Note;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.NoteRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Get all notes for logged-in user
    @GetMapping
    public List<Note> getUserNotes(@RequestParam Long userId) {
        return noteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 2. Add a new note
    @PostMapping
    public Note createNote(@RequestBody Note note, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        note.setUser(user);
        return noteRepository.save(note);
    }

    // 3. Delete a note
    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
        return "Note deleted successfully";
    }
    // 4. Update Note
    @PutMapping("/{id}")
    public com.example.taskmanager.model.Note updateNote(
            @PathVariable Long id,
            @RequestBody com.example.taskmanager.model.Note noteDetails) {

        // 1. Database se pehle purana note dhoodhenge uski ID se
        com.example.taskmanager.model.Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));

        // 2. Sirf uska content badlenge, ID wahi purani rahegi
        existingNote.setContent(noteDetails.getContent());

        // 3. Save karne par agar ID match karegi toh Hibernate update karega, naya nahi banayega
        return noteRepository.save(existingNote);
    }
}