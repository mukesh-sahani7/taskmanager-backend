package com.example.taskmanager.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data // it is Lombok annotation tha generate Getter, Setter and toString
public class task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false) // field can't be empty
    private String title;

    private String description;

    @Column(nullable = false)
    private String status; // ex: PENDING, IN_PROGRESS, COMPLETED

    private String priority; // ex: HIGH, MEDIUM, LOW

    //task kis user ka hai many to one mapping
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Har task kisi ek user se juda hoga

    private LocalDateTime reminderTime; // User is waqt par reminder chahta hai
}

