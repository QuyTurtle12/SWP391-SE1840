package com.swp391.jewelrysalesystem.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.swp391.jewelrysalesystem")
@SpringBootApplication
@RestController
public class MainController {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		SpringApplication.run(MainController.class, args);
	}
}
