package com.example.taskmanager.service;

import com.example.taskmanager.model.task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RemainderService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Yeh method har 60000 milliseconds (yaani har 1 minute) me automatic chalega
    @Scheduled(fixedRate = 60000)
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        // Database se saare PENDING tasks nikalenge
        List<task> tasks = taskRepository.findAll();

        for (task task : tasks) {
            if (task.getReminderTime() != null && task.getStatus().equals("PENDING")) {
                LocalDateTime taskTime = task.getReminderTime().truncatedTo(ChronoUnit.MINUTES);

                // Agar abhi ka time aur reminder ka time match kar jaye
                if (now.equals(taskTime)) {
                    sendEmail(task);
                }
            }
        }
    }

    private void sendEmail(task task) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(task.getUser().getEmail()); // User ka email automatically pick hoga
            message.setSubject("⏰ Task Reminder: " + task.getTitle());
            message.setText("Hello " + task.getUser().getName() + ",\n\n" +
                    "It's your Task Reminder:\n" +
                    "🔹 Title: " + task.getTitle() + "\n" +
                    "🔹 Description: " + task.getDescription() + "\n\n" +
                    "Check Your Dashboard To Complete the Task.\n\n" +
                    "Regards,\nTaskWorkspace Team");

            mailSender.send(message);
            System.out.println("Email successfully sent to: " + task.getUser().getEmail());
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }
}

