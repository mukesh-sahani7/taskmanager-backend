package com.example.taskmanager.repository;

import com.example.taskmanager.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserIdOrderByCreatedAtDesc(Long userId); // Sirf specific user ke notes, naye wale sabse upar
}