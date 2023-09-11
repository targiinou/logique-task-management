package com.targinou.taskmanagement.task;

import com.targinou.taskmanagement.commom.view.GenericView;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public GenericView<TaskDTO> createTask(@RequestBody TaskDTO task) {
        return GenericView.ok(taskService.createTask(task));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/list")
    public GenericView<List<TaskDTO>> getAllTasks() {
        return GenericView.ok(taskService.getAllTasks());
    }

    @PutMapping("/{taskId}/update-status")
    public GenericView<TaskDTO> updateTaskStatus(@PathVariable Integer taskId, @RequestParam String newStatus) {
        return GenericView.ok(taskService.updateTaskStatus(taskId, TaskStatus.valueOf(newStatus)));
    }

    @PutMapping("/{taskId}/archive")
    public GenericView<TaskDTO> archiveTask(@PathVariable Integer taskId) {
        return GenericView.ok(taskService.archiveTask(taskId));
    }

}