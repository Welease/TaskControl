package com.example.TaskControl;

import com.example.TaskControl.config.MvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@Import(MvcConfig.class)
public class TaskControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskControlApplication.class, args);
	}

}
