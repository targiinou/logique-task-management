package com.targinou.taskmanagement.task;

import com.targinou.taskmanagement.commom.exception.ValidationException;
import com.targinou.taskmanagement.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository,
                       UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        taskDTO.setStatus(TaskStatus.NOT_STARTED);
        var taskSaved = taskRepository.save(Task.from(taskDTO));
        taskDTO.setId(taskSaved.getId());
        return taskDTO;
    }

    public List<TaskDTO> getAllTasks() {
        //var user = userService.getCurrentUser().orElseThrow();

        return taskRepository.findAllTaskDTOByStatusNotAndUserId(1);
    }

    public Task updateTaskStatus(Integer taskId, TaskStatus newStatus) {

        //hasPermission(taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ValidationException("Tarefa não encontrada"));

        TaskStatus currentStatus = task.getStatus();

        if (currentStatus == TaskStatus.ARCHIVED) {
            throw new ValidationException("Tarefa arquivada não pode ser atualizada");
        }

        if (newStatus == TaskStatus.ARCHIVED) {
            task.setStatus(TaskStatus.ARCHIVED);
        } else if (currentStatus == TaskStatus.NOT_STARTED) {
            if (newStatus == TaskStatus.IN_PROGRESS || newStatus == TaskStatus.FINISHED) {
                task.setStatus(newStatus);
            } else {
                throw new ValidationException("Transição de estado inválida");
            }
        } else if (currentStatus == TaskStatus.IN_PROGRESS) {
            if (newStatus == TaskStatus.NOT_STARTED || newStatus == TaskStatus.FINISHED) {
                task.setStatus(newStatus);
            } else {
                throw new ValidationException("Transição de estado inválida");
            }
        } else if (currentStatus == TaskStatus.FINISHED) {
            throw new ValidationException("Tarefa finalizada não pode ser atualizada");
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Integer taskId) {
        hasPermission(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ValidationException("Tarefa não encontrada"));

        TaskStatus currentStatus = task.getStatus();

        if (currentStatus == TaskStatus.ARCHIVED || currentStatus == TaskStatus.FINISHED) {
            throw new ValidationException("Tarefa não pode ser excluída");
        }

        taskRepository.delete(task);
    }

    public Task archiveTask(Integer taskId) {
        //hasPermission(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ValidationException("Tarefa não encontrada"));

        if (task.getStatus() != TaskStatus.ARCHIVED) {
            task.setStatus(TaskStatus.ARCHIVED);
            return taskRepository.save(task);
        }

        return task;
    }

    private void hasPermission(Integer taksId) {
        var user = userService.getCurrentUser().orElseThrow();

        if (!taskRepository.existsByIdAndUser_Id(taksId, user.getId())) {
            throw new ValidationException("O usuário logado não tem permissão para modificar essa tarefa.");
        }
    }
}

