package com.example.TaskControl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Integer id;

    public Task(String description, Date beginDate, Date endDate, String priority, String status, Employee executor) {
        this.description = description;
        this.beginDate = beginDate;
        this.endDate= endDate;
        this.priority = priority;
        this.status = status;
        this.executor = executor;
    }

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private Date beginDate;

    @Setter
    @Getter
    private Date endDate;

    @Setter
    @Getter
    private String priority;

    @Setter
    @Getter
    private String status;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private Employee executor;
}
