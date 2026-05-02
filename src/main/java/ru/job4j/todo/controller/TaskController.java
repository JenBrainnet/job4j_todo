package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@Controller
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("filter", "all");
        return "tasks/list";
    }

    @GetMapping("/done")
    public String getDone(Model model) {
        model.addAttribute("tasks", taskService.findByDone(true));
        model.addAttribute("filter", "done");
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNew(Model model) {
        model.addAttribute("tasks", taskService.findByDone(false));
        model.addAttribute("filter", "new");
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Task with the specified ID not found");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Task with the specified ID not found");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Task with the specified ID not found");
            return "errors/404";
        }
        return "redirect:/tasks/" + task.getId();
    }

    @GetMapping("/complete/{id}")
    public String complete(Model model, @PathVariable int id) {
        var isCompleted = taskService.complete(id);
        if (!isCompleted) {
            model.addAttribute("message", "Task with the specified ID not found");
            return "errors/404";
        }
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Task with the specified ID not found");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

}
