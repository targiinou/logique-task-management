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
        return GenericView.ok(taskService.getAllTasks());
    }

    @PutMapping("/{taskId}/update-status")
    public GenericView<TaskDTO> updateTaskStatus(@PathVariable Integer taskId, @RequestParam String newStatus) {
        return GenericView.ok(taskService.updateTaskStatus(taskId, TaskStatus.valueOf(newStatus)));
    }

    @DeleteMapping("/{taskId}")
    public GenericView<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return GenericView.ok();
    }

    @PutMapping("/{taskId}/archive")
    public GenericView<TaskDTO> archiveTask(@PathVariable Integer taskId) {
        return GenericView.ok(taskService.archiveTask(taskId));
    }

}