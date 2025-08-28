package com.example.todo;

import org.springframework.data.jpa.repository.JpaRepository;

// Repository-interface för att hantera Task-entiteter i databasen
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Här kan du lägga egna query-metoder vid behov, t.ex:
    // List<Task> findByDone(boolean done);
}
