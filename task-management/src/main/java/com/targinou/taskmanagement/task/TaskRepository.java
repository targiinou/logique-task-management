package com.targinou.taskmanagement.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.ArrayList;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query(value = "SELECT id, description, status, title FROM task WHERE status != :status AND user_id = :userId", nativeQuery = true)
    List<Object[]> findAllByStatusNotAndUserId(String status, Integer userId);

    default List<TaskDTO> findAllTaskDTOByStatusNotAndUserId(String status, Integer userId) {
        List<Object[]> results = findAllByStatusNotAndUserId(status, userId);
        List<TaskDTO> taskDTOs = new ArrayList<>();

        for (Object[] row : results) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId((Integer) row[0]);
            taskDTO.setDescription((String) row[1]);
            taskDTO.setStatus(TaskStatus.valueOf((String) row[2]));
            taskDTO.setTitle((String) row[3]);
            taskDTOs.add(taskDTO);
        }

        return taskDTOs;
    }
    boolean existsByIdAndUser_Id(Integer taskId, Integer userId);
}
