package com.targinou.taskmanagement.task;

import com.targinou.taskmanagement.commom.view.GenericView;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public GenericView<TaskDTO> createTask(@RequestBody TaskDTO task) {
        return GenericView.ok(taskService.createTask(task));
    }

    @GetMapping("/list")
    public GenericView<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return GenericView.ok(tasks);
    }

    @PutMapping("/{taskId}/update-status")
    public GenericView<Task> updateTaskStatus(@PathVariable Integer taskId, @RequestParam String newStatus) {
        Task updatedTask = taskService.updateTaskStatus(taskId, TaskStatus.valueOf(newStatus));
        return GenericView.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public GenericView<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return GenericView.ok();
    }

    @PutMapping("/{taskId}/archive")
    public GenericView<Task> archiveTask(@PathVariable Integer taskId) {
        Task archivedTask = taskService.archiveTask(taskId);
        return GenericView.ok(archivedTask);
    }

}