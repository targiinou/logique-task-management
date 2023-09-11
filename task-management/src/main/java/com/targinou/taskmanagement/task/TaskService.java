package com.targinou.taskmanagement.task;

import com.targinou.taskmanagement.commom.exception.ValidationException;
import com.targinou.taskmanagement.commom.util.Util;
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
        validateDTO(taskDTO);
        taskDTO.setStatus(TaskStatus.NOT_STARTED);
        var task = Task.from(taskDTO);
        task.setUser(userService.getCurrentUser().orElseThrow());
        var taskSaved = taskRepository.save(task);
        taskDTO.setId(taskSaved.getId());
        return taskDTO;
    }

    public List<TaskDTO> getAllTasks() {
        var user = userService.getCurrentUser().orElseThrow();
        return taskRepository.findAllTaskDTOByStatusNotAndUserId(user.getId());
    }

    public TaskDTO updateTaskStatus(Integer taskId, TaskStatus newStatus) {

        hasPermission(taskId);

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

        return TaskDTO.from(taskRepository.save(task));
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

    public TaskDTO archiveTask(Integer taskId) {
        hasPermission(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ValidationException("Tarefa não encontrada"));

        if (task.getStatus() == TaskStatus.ARCHIVED) {
            throw new ValidationException("Essa tarefa já está arquivada.");
        }

        task.setStatus(TaskStatus.ARCHIVED);
        return TaskDTO.from(taskRepository.save(task));
    }

    private void validateDTO(TaskDTO taskDTO) {

        if (Util.isEmpty(taskDTO.getTitle())){
            throw new ValidationException("O titulo da tarefa não pode ser nulo.");
        }

        if (Util.isEmpty(taskDTO.getDescription())){
            throw new ValidationException("A descrição da tarefa não pode ser nulo.");
        }

        if (taskDTO.getTitle().length() > 30) {
            throw new ValidationException("O titulo pode conter no máximo 30 caracteres.");
        }

        if (taskDTO.getDescription().length() > 255) {
            throw new ValidationException("A descrição pode conter no máximo 255 caracteres.");
        }

    }

    private void hasPermission(Integer taksId) {
        var user = userService.getCurrentUser().orElseThrow();

        if (!taskRepository.existsByIdAndUser_Id(taksId, user.getId())) {
            throw new ValidationException("O usuário logado não tem permissão para modificar essa tarefa.");
        }
    }

}

