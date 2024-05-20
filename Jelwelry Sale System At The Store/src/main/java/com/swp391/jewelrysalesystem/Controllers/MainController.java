package com.swp391.jewelrysalesystem.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = "com.swp391.jewelrysalesystem")
@RestController
public class MainController {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		SpringApplication.run(MainController.class, args);
	}
}
