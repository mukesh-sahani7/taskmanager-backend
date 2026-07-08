package com.example.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.taskmanager", 
    "com.example.taskmanager.controller", 
    "com.example.taskmanager.Config"
})
public class TaskmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);

	}

}
