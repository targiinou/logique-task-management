package com.targinou.taskmanagement.task;

import com.targinou.taskmanagement.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;
    private String description;
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Task from(TaskDTO form) {
        return Task.builder()
                .id(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .status(form.getStatus())
                .build();
    }

}
