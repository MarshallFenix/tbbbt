package io.c_project.tgbot0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"config","io.c_project.tgbot0","model","service"})
public class Tgbot0Application {

	public static void main(String[] args) {
		SpringApplication.run(Tgbot0Application.class, args);
	}

}
