package com.sp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SpAppMain {
	
	public static void main(String[] args) {
		System.out.println("blabla");
		SpringApplication.run(SpAppMain.class,args);
	}
}
