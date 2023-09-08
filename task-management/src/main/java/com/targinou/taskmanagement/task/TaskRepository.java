package com.targinou.taskmanagement.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByStatusNotAndUser_Id(TaskStatus status, @Param("userId") Integer userId);
    boolean existsByIdAndUser_Id(Integer taskId, Integer userId);
}
