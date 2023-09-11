package com.targinou.taskmanagement.task;

import com.targinou.taskmanagement.user.User;
import com.targinou.taskmanagement.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    private static User user;
    private static Task task;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testArchiveTask() {
        generateObjects();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(Optional.of(user));
        when(taskRepository.existsByIdAndUser_Id(task.getId(), user.getId())).thenReturn(true);
        when(taskRepository.save(task)).thenReturn(task);

        TaskDTO archivedTaskDTO = taskService.archiveTask(task.getId());

        verify(taskRepository, times(1)).save(task);
        assertSame(TaskStatus.ARCHIVED, archivedTaskDTO.getStatus());
    }

    @Test
    public void testUpdateTaskStatus() {
        generateObjects();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(Optional.of(user));
        when(taskRepository.existsByIdAndUser_Id(task.getId(), user.getId())).thenReturn(true);
        when(taskRepository.save(task)).thenReturn(task);

        TaskDTO updatedTaskDTO = taskService.updateTaskStatus(task.getId(), TaskStatus.IN_PROGRESS);

        verify(taskRepository, times(1)).save(task);
        assertSame(TaskStatus.IN_PROGRESS, updatedTaskDTO.getStatus());
    }

    @Test
    public void testGetAllTasks() {
        generateObjects();

        when(userService.getCurrentUser()).thenReturn(Optional.of(user));

        taskService.getAllTasks();

        verify(taskRepository, times(1)).findAllTaskDTOByStatusNotAndUserId(user.getId());
    }

    @Test
    public void testCreateTask() {
        var taskDTO = TaskDTO.builder()
                .title("Tarefa teste")
                .description("Teste unitário")
                .build();

        generateObjects();

        when(userService.getCurrentUser()).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(1);
            return task;
        });
        when(taskRepository.existsByIdAndUser_Id(task.getId(), user.getId())).thenReturn(true);

        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);

        verify(taskRepository, times(1)).save(any(Task.class));
        assertEquals(1, createdTaskDTO.getId());
        assertEquals(TaskStatus.NOT_STARTED, createdTaskDTO.getStatus());
    }

    private static void generateObjects() {
        user = User.builder()
                .id(1)
                .email("teste@testelogique.com")
                .name("Teste")
                .password("123456")
                .build();

        task = Task.builder()
                .id(1)
                .status(TaskStatus.NOT_STARTED)
                .title("Tarefa teste")
                .description("Teste unitário")
                .user(user)
                .build();
    }
}
