package ru.job4j.todo.service.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.task.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByDone(boolean done) {
        return taskRepository.findByDone(done);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public boolean complete(int id) {
        return taskRepository.complete(id);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }

}
