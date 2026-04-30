package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task add(Task task);

    List<Task> findAll();

    List<Task> findByDone(boolean done);

    Optional<Task> findById(int id);

    boolean update(Task task);

    boolean complete(int id);

    boolean deleteById(int id);

}
