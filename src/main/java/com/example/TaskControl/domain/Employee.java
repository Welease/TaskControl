package com.example.TaskControl.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Employee(String name, String login, String password, String role) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Employee (String name, String surName, String middleName, Long tabNumber, String phone, String login, String password, String role) {
        this.name = name;
        this.surName = surName;
        this.middleName = middleName;
        this.tabNumber = tabNumber;
        this.phone = phone;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String surName;

    @Getter
    @Setter
    private String middleName;

    @Getter
    @Setter
    private Long tabNumber;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    @OneToMany(mappedBy = "executor")
    private Set<Task> tasks;
}
