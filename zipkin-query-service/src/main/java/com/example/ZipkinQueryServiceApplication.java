package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

@EnableZipkinStreamServer
@SpringBootApplication
public class ZipkinQueryServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(ZipkinQueryServiceApplication.class, args);
	}
}
