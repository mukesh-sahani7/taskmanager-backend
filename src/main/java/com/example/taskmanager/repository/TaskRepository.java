package com.example.taskmanager.repository;

import com.example.taskmanager.model.task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<task, Long> {
    // 👈 Yeh naya method jodein: Spring Data JPA apne aap user.id ke hisab se filter karega
    List<task> findByUserId(Long userId);
}


