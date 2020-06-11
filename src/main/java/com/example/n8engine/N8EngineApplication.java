package com.example.n8engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class N8EngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(N8EngineApplication.class, args);
	}
}
